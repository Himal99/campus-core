package com.sb.file.compressor.core.uploads;

import com.sb.file.compressor.core.utils.SystemUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Himal Rai on 2/1/2024
 * Sb Solutions Nepal pvt.ltd
 * Project filecompressor-backend.
 */

@Component
@Slf4j
public class FileUploadUtils {
    private static String url;


    public static String uploadFile(byte[] fileInByte, String folderName,
                                    String documentName) throws IOException {

        final byte[] bytes = fileInByte;

        Path path = Paths.get(SystemUtils.getOSPath() + "images/" + folderName);
        if (!Files.exists(path)) {
            new File(SystemUtils.getOSPath() + "images/" + folderName).mkdirs();
        }
        // check if file under same name exits, if exists delete it
        File dir = path.toFile();
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            for (File f : files) {
                // remove file if exists
                if (f.getName().toLowerCase().contains(documentName.toLowerCase())) {
                    try {
                        f.delete();
                    } catch (Exception e) {
                        log.error("Failed to delete file {} {}", f, e);
                    }
                }
            }

        }
        url = "images/" + folderName + "/" + documentName;

        path = Paths.get(SystemUtils.getOSPath() + url);
        Files.write(path, bytes);
        return url;

    }
}
