package com.sb.file.compressor.core.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


/**
 * @author Sunil Babu Shrestha on 12/31/2018
 */
public interface BaseService<T> {

    /**
     *
     */
    List<T> findAll();

    /**
     *
     */
    T findOne(Long id);

    /**
     *
     */
    T save(T t);

    /**
     *
     */
    Page<T> findAllPageable(Object t, Pageable pageable);

    /**
     *
     */
    List<T> saveAll(List<T> list);


}
