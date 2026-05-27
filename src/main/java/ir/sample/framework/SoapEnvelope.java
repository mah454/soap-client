package ir.sample.framework;

public class SoapEnvelope {
    private static final String SOAP_NS = "http://www.w3.org/2003/05/soap-envelope";

    public static String generateSoapBody(Object o) throws Exception {
        String body = SoapParser.toXml(o);
        return wrap(body);
    }

    public static String wrap(String bodyXml) {
        return """
            <soapenv:Envelope xmlns:soapenv="%s">
              <soap:Header/>
              <soap:Body>
                %s
              </soap:Body>
            </soap:Envelope>
        """.formatted(SOAP_NS, bodyXml);
    }

    public static String extractBody(String xml) {
        return xml.replaceAll("(?s).*<soapenv:Body>(.*)</soapenv:Body>.*", "$1");
    }
}
