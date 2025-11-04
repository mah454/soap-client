package ir.sample;

import ir.sample.framework.SoapField;
import ir.sample.framework.SoapRoot;

@SoapRoot(namespace = "http://tempuri.org/", value = "AddResponse")
public class AddResponse {
    @SoapField(value = "AddResult", prefix = "tem", namespace = "http://tempuri.org/")
    private int result;

    public int getResult() { return result; }
    public void setResult(int result) { this.result = result; }
}