package com.sb.file.compressor.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sb.file.compressor.core.utils.DateManipulator;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Optional;

@Component
public class SocketIOClientComponent {
    private static final Logger logger = LoggerFactory.getLogger(SocketIOClientComponent.class);

    private final RealTimeLogService realTimeLogService;

    private Socket socket;

    @Value("${compress.api}")
    private String baseUri;

    public SocketIOClientComponent(RealTimeLogService realTimeLogService) {
        this.realTimeLogService = realTimeLogService;
    }

    @PostConstruct
    public void init() {
        try {
            socket = IO.socket(baseUri); // Adjust the URL to your Socket.IO server
            socket.connect();

            socket.on(Socket.EVENT_CONNECT, args -> {
                System.out.println("Connected to Socket.IO Server");
            });
            socket.on("server-image", objects -> {
                ObjectMapper mapper = new ObjectMapper();
                Optional<Object> objectOptional = Arrays.stream(objects).findFirst();
                if (objectOptional.isPresent()){
                    String jsonString = objectOptional.get().toString();
                    try {
                        LogMessage logMessage = mapper.readValue(jsonString, LogMessage.class);
                        this.realTimeLogService.sendToSubscribers(logMessage.getSessionId(), logMessage);
                        logger.info("::: REAL TIME LOG FOR {} RECEIVED :::", logMessage.getSessionId());

                    } catch (JsonProcessingException e) {
                        logger.error("::: ERROR WHILE RECEIVING REAL TIME LOG  :::");
                        throw new RuntimeException(e);
                    }

                }
            });
            // Add more listeners as needed

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public Socket getSocket() {
        return socket;
    }
}
