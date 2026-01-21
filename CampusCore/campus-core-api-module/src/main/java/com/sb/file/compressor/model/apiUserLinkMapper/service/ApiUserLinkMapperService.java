package com.sb.file.compressor.model.apiUserLinkMapper.service;

import com.sb.file.compressor.core.service.BaseService;
import com.sb.file.compressor.model.apiUserLinkMapper.entity.ApiUserLinkMapper;

import java.util.List;
import java.util.Optional;

public interface ApiUserLinkMapperService extends BaseService<ApiUserLinkMapper> {
    Optional<ApiUserLinkMapper>findByTokenIdAndSessionId(String id, String sessionId);
}
