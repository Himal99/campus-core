package com.sb.file.compressor.compress.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Himal Rai on 1/21/2024
 * Sb Solutions Nepal pvt.ltd
 * Project fileCompressorPocBackend.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServerCompressRequestDto {
  private  String fileDirectory,  backUpDirectory,  endpointUrl;
}
