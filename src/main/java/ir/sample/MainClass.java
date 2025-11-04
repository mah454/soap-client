package ir.sample;

import ir.sample.framework.SoapEnvelope;

public class MainClass {
    static void main() throws Exception {
        AddRequest request = new AddRequest(12, 33);
        System.out.println("Start");
        System.out.println(SoapEnvelope.generateSoapBody(request));
        System.out.println("Finished");
    }
}