package com.sb.file.compressor.core.utils;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * @author Himal Rai on 2/8/2024
 * Sb Solutions Nepal pvt.ltd
 * Project filecompressor-backend.
 */
@Data
public class PaginationUtils {
    public static final int DEFAULT_PAGE = 1;
    public static final int DEFAULT_PAGE_SIZE = 10;

    public static final String PAGE = "page";
    public static final String SIZE = "size";
    public static final String SORT_BY = "sortBy";
    public static final String SORT_ORDER = "sortOrder";

    public static Pageable pageable(int page, int size) {
        return PageRequest.of(page - 1, size);
    }

}
