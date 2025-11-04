package ir.sample.framework;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;

public class SoapParser {

    public static String toXml(Object obj) throws Exception {
        Class<?> cls = obj.getClass();
        if (!cls.isAnnotationPresent(SoapRoot.class))
            throw new IllegalArgumentException("Missing @SoapRoot");

        SoapRoot root = cls.getAnnotation(SoapRoot.class);
        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        DocumentBuilder b = f.newDocumentBuilder();
        Document doc = b.newDocument();

        // Create root element with prefix
        String prefix = "ns";
        Element rootEl = doc.createElementNS(root.namespace(), prefix + ":" + root.value());
        rootEl.setAttribute("xmlns:" + prefix, root.namespace());
        doc.appendChild(rootEl);

        for (Field field : cls.getDeclaredFields()) {
            if (!field.isAnnotationPresent(SoapField.class)) continue;
            field.setAccessible(true);
            SoapField ann = field.getAnnotation(SoapField.class);
            Object value = field.get(obj);
            if (value == null) continue;

            String ns = ann.namespace().isEmpty() ? root.namespace() : ann.namespace();
            String pfx = ann.prefix().isEmpty() ? prefix : ann.prefix();

            Element el = doc.createElementNS(ns, pfx + ":" + ann.value());
            el.setTextContent(value.toString());
            if (!ann.namespace().isEmpty())
                el.setAttribute("xmlns:" + pfx, ns);

            rootEl.appendChild(el);
        }

        Transformer t = TransformerFactory.newInstance().newTransformer();
//        t.setOutputProperty(OutputKeys.INDENT, "yes");
        t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        StringWriter writer = new StringWriter();
        t.transform(new DOMSource(doc), new StreamResult(writer));
        return writer.toString();
    }

    public static <T> T fromXml(String xml, Class<T> cls) throws Exception {
        SoapRoot root = cls.getAnnotation(SoapRoot.class);
        if (root == null)
            throw new IllegalArgumentException("Missing @SoapRoot");

        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        f.setNamespaceAware(true);
        DocumentBuilder b = f.newDocumentBuilder();
        Document doc = b.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

        Element rootEl = doc.getDocumentElement();
        T instance = cls.getDeclaredConstructor().newInstance();

        for (Field field : cls.getDeclaredFields()) {
            if (!field.isAnnotationPresent(SoapField.class)) continue;
            SoapField ann = field.getAnnotation(SoapField.class);
            String ns = ann.namespace().isEmpty() ? root.namespace() : ann.namespace();
            NodeList nodes = rootEl.getElementsByTagNameNS(ns, ann.value());
            if (nodes.getLength() > 0) {
                String value = nodes.item(0).getTextContent();
                field.setAccessible(true);
                if (field.getType() == int.class)
                    field.setInt(instance, Integer.parseInt(value));
                else if (field.getType() == double.class)
                    field.setDouble(instance, Double.parseDouble(value));
                else
                    field.set(instance, value);
            }
        }
        return instance;
    }
}
