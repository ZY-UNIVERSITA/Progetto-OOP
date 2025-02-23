package com.zysn.passwordmanager.model.utils.encoding;

public class EncodingClassForTesting {
    private String firstField;
    private int secondField;

    public EncodingClassForTesting(String firstField, int secondField) {
        this.firstField = firstField;
        this.secondField = secondField;
    }

    public EncodingClassForTesting() {
        
    }

    public String getFirstField() {
        return firstField;
    }

    public void setFirstField(String firstField) {
        this.firstField = firstField;
    }

    public int getSecondField() {
        return secondField;
    }

    public void setSecondField(int secondField) {
        this.secondField = secondField;
    }
    
    
}
