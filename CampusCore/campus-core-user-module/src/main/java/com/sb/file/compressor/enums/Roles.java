package com.sb.file.compressor.enums;

/**
 * @author Himal Rai on 1/14/2024
 * Sb Solutions Nepal pvt.ltd
 * Project sb-back-core.
 */
public enum Roles {
    ADMIN("ADMIN"),USER("USER"),EDITOR("EDITOR"),API_USER("API_USER");

    private String value;

    Roles(String value){
        this.value = value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
