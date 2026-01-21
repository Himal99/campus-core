package com.sb.file.compressor.compress.converter;

import com.sb.file.compressor.core.utils.SystemUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @author Himal Rai on 1/16/2024
 * Sb Solutions Nepal pvt.ltd
 * Project fileCompressorPocBackend.
 */
public class MultipartFileConverter {
    public static FileSystemResource convert(MultipartFile multipartFile) throws IOException {
        File f = new File(SystemUtils.getOSPath() + "images\\temp\\" + multipartFile.getOriginalFilename());

        if (!f.exists()) {
            new File(SystemUtils.getOSPath() + "images\\temp\\").mkdirs();
        }

        multipartFile.transferTo(f);

        FileSystemResource fileSystemResource = new FileSystemResource(f);

        return fileSystemResource;
    }
}
