package com.sb.file.compressor.web;

import com.sb.file.compressor.auth.config.RequiredPermission;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/v1/realtime")
public class RealTimeLogController {
    private final RealTimeLogService realTimeLogService;

    public RealTimeLogController(RealTimeLogService realTimeLogService) {
        this.realTimeLogService = realTimeLogService;
    }

//    @RequiredPermission({"ADMIN", "USER"})
    @PostMapping("/notify/{id}")
    public void notifySubscribers(@PathVariable("id") String id, @RequestBody LogMessage message) {
        realTimeLogService.sendToSubscribers(id, message);
    }
//    @RequiredPermission({"ADMIN", "USER"})
    @GetMapping("/subscribe/{id}")
    public SseEmitter subscribe(@PathVariable("id") String id) {
        return realTimeLogService.createEmitter(id);
    }
}
