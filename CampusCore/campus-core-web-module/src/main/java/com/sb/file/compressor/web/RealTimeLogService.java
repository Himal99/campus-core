package com.sb.file.compressor.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sb.file.compressor.core.utils.SystemUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class RealTimeLogService {
    private final Map<String, List<SseEmitter>> emitters = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;

    public RealTimeLogService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public SseEmitter createEmitter(String id) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        this.emitters.computeIfAbsent(id, k -> new ArrayList<>()).add(emitter);

        emitter.onCompletion(() -> removeEmitter(id, emitter));
        emitter.onTimeout(() -> removeEmitter(id, emitter));

        return emitter;
    }

    public void removeEmitter(String id, SseEmitter emitter) {
        List<SseEmitter> userEmitters = this.emitters.getOrDefault(id, new ArrayList<>());
        userEmitters.remove(emitter);
        if (userEmitters.isEmpty()) {
            this.emitters.remove(id);
        }
    }

    public void sendToSubscribers(String id, LogMessage data) {

        log.info("log message::{}",data);

        List<SseEmitter> userEmitters = this.emitters.getOrDefault(id, new ArrayList<>());
        List<SseEmitter> deadEmitters = new ArrayList<>();

        userEmitters.forEach(emitter -> {
            try {
                emitter.send(data);
            } catch (IOException e) {
                deadEmitters.add(emitter);
            }
        });

        deadEmitters.forEach(deadEmitter -> removeEmitter(id, deadEmitter));
        List<LogMessage> logMessages = new ArrayList<>();
        File file = new File(SystemUtils.getOSPath() + "/images/serverLog/");
        if (!file.exists()) {
            file.mkdirs();
        }

        File file1 = new File(SystemUtils.getOSPath() + "/images/serverLog/"+data.getSessionId()+".txt");
        StringBuilder response = new StringBuilder();
        if (file1.exists()){
          try {
              BufferedReader reader = new BufferedReader(new FileReader(file1));
              String line = null;
              while ((line = reader.readLine()) != null) {
                  response.append(line);
              }

              List<LogMessage> messages = objectMapper.readValue(response.toString(), objectMapper.getTypeFactory().constructType(List.class, LogMessage.class));
              if (!messages.isEmpty()) {
                  logMessages.addAll(messages);
              }

          } catch (Exception exception) {

          }
        }
        logMessages.add(data);
        try {
            FileWriter writer = new FileWriter(new File(SystemUtils.getOSPath() + "/images/serverLog/"+data.getSessionId()+".txt"));
            writer.write(objectMapper.writeValueAsString(logMessages));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
