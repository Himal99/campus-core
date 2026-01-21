package com.sb.file.compressor.core.enums;

/**
 * @author Himal Rai on 2/1/2024
 * Sb Solutions Nepal pvt.ltd
 * Project filecompressor-backend.
 */
public enum Status {
    ACTIVE("Active"),INACTIVE("Inactive");
    private String value;
    Status(String value){
        this.value = value;
    }
    public String getValue(){
        return value;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
