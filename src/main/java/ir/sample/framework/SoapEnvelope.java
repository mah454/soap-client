package ir.sample.framework;

public class SoapEnvelope {
    private static final String SOAP_NS = "http://schemas.xmlsoap.org/soap/envelope/";

    public static String generateSoapBody(Object o) throws Exception {
        String body = SoapParser.toXml(o);
        return wrap(body);
    }

    public static String wrap(String bodyXml) {
        return """
            <soapenv:Envelope xmlns:soapenv="%s">
              <soapenv:Header/>
              <soapenv:Body>
                %s
              </soapenv:Body>
            </soapenv:Envelope>
        """.formatted(SOAP_NS, bodyXml);
    }

    public static String extractBody(String xml) {
        return xml.replaceAll("(?s).*<soapenv:Body>(.*)</soapenv:Body>.*", "$1");
    }
}
