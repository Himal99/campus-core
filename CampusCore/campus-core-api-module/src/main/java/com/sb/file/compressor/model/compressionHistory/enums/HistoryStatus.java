package com.sb.file.compressor.model.compressionHistory.enums;

/**
 * @author Himal Rai on 2/18/2024
 * Sb Solutions Nepal pvt.ltd
 * Project filecompressor-backend.
 */
public enum HistoryStatus {
    STARTED("Started"),COMPLETED("Completed");
    String value;

    HistoryStatus(String value) {
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
