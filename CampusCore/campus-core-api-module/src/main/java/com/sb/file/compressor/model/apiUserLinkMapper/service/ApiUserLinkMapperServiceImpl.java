package com.sb.file.compressor.model.apiUserLinkMapper.service;

import com.sb.file.compressor.model.apiUserLinkMapper.entity.ApiUserLinkMapper;
import com.sb.file.compressor.model.apiUserLinkMapper.repo.ApiUserLinkMapperRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApiUserLinkMapperServiceImpl implements ApiUserLinkMapperService{
    private final ApiUserLinkMapperRepo apiUserLinkMapperRepo;

    public ApiUserLinkMapperServiceImpl(ApiUserLinkMapperRepo apiUserLinkMapperRepo) {
        this.apiUserLinkMapperRepo = apiUserLinkMapperRepo;
    }

    @Override
    public List<ApiUserLinkMapper> findAll() {
        return this.apiUserLinkMapperRepo.findAll();
    }

    @Override
    public ApiUserLinkMapper findOne(Long id) {
        return this.apiUserLinkMapperRepo.findById(id).get();
    }

    @Override
    public ApiUserLinkMapper save(ApiUserLinkMapper apiUserLinkMapper) {
        return this.apiUserLinkMapperRepo.save(apiUserLinkMapper);
    }

    @Override
    public Page<ApiUserLinkMapper> findAllPageable(Object t, Pageable pageable) {
        return null;
    }

    @Override
    public List<ApiUserLinkMapper> saveAll(List<ApiUserLinkMapper> list) {
        return null;
    }

    @Override
    public Optional<ApiUserLinkMapper> findByTokenIdAndSessionId(String id, String sessionId) {
        return apiUserLinkMapperRepo.findByTokenIdAndSessionId(id, sessionId);
    }
}
