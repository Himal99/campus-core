package com.sb.file.compressor.core.enums;

public enum CompressStatus {
    SUCCESS("Success"),FAIL("Fail");
//    SUCCESS("Success"),FAIL("Fail"),UNAUTHORIZED("Unauthorized");
    private String value;

    CompressStatus(String value) {
        this.value=value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "CompressStatus{" +
                "value='" + value + '\'' +
                '}';
    }
}
