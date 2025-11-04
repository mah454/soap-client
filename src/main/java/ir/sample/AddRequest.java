package ir.sample;

import ir.sample.framework.SoapField;
import ir.sample.framework.SoapRoot;

@SoapRoot(namespace = "http://tempuri.org/", value = "Add")
public class AddRequest {

    @SoapField(value = "intA", prefix = "tem", namespace = "http://tempuri.org/")
    private int intA;

    @SoapField(value = "intB", prefix = "tem", namespace = "http://tempuri.org/")
    private int intB;

    public AddRequest() {}

    public AddRequest(int intA, int intB) {
        this.intA = intA;
        this.intB = intB;
    }

    public int getIntA() {
        return intA;
    }

    public int getIntB() {
        return intB;
    }
}
