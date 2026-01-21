package com.sb.file.compressor.compress.utils;

import com.sb.file.compressor.core.exception.CustomException;
import com.sb.file.compressor.core.utils.DateManipulator;
import com.sb.file.compressor.core.utils.SystemUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Himal Rai on 1/21/2024
 * Sb Solutions Nepal pvt.ltd
 * Project fileCompressorPocBackend.
 */

@Data
@Slf4j
public class FileSaver {
    private static String url;
    private static final Logger logger = LoggerFactory.getLogger(FileSaver.class);




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


    public static void createFolderIfNotExist(String folderPath) throws  CustomException{
        Path path = Paths.get(folderPath);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
                logger.info("::: DIRECTORY CREATED ::: {}", folderPath);
                System.out.println("Directory created: " + path);
            } catch (IOException e) {
                logger.info("::: CANNOT CREATE DIRECTORY CREATED ::: {}", folderPath);
                throw new CustomException(" CANNOT CREATE FOLDER ");
            }
        }
    }

}
