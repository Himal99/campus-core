package com.sb.file.compressor.core.utils;

import lombok.Data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;

/**
 * @author Himal Rai on 1/22/2024
 * Sb Solutions Nepal pvt.ltd
 * Project sb-back-core.
 */

@Data
public class SystemUtils {
    public static String getOSPath() {
        final String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return "C:\\";
        } else {
            return "\\var\\";
        }
    }

    public static String getDeviceName() throws Exception {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            return localHost.getHostName();

        } catch (UnknownHostException e) {
            e.printStackTrace();
            throw new UnknownHostException();
        }
    }

    private static void getWindowsLaptopModel() {
        try {
            Process process = new ProcessBuilder("wmic", "csproduct", "get", "name").start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                // Extracting the laptop model from the output
                if (!line.trim().isEmpty()) {
                    System.out.println("Laptop Model: " + line.trim());
                }
            }

            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static double getSizeInMbFromBytes(byte[] bytes){
        long fileSizeInBytes = bytes.length;
        double fileSizeInKB = fileSizeInBytes / 1024.0;
        double fileSizeInMB = fileSizeInKB / 1024.0;
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String formattedNumber = decimalFormat.format(fileSizeInMB);
        double result = Double.parseDouble(formattedNumber);

        return result;
    }

    public static String getSizeInMbFromBytes(long fileSizeBytes) {
        double fileSizeKB = (double) fileSizeBytes / 1024;
        if (fileSizeKB < 1024) {
            return String.format("%.2f KB", fileSizeKB);
        }

        double fileSizeMB = fileSizeKB / 1024;
        if (fileSizeMB < 1024) {
            return String.format("%.2f MB", fileSizeMB);
        }

        double fileSizeGB = fileSizeMB / 1024;
        return String.format("%.2f GB", fileSizeGB);
    }
}
