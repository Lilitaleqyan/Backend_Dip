package org.example.backend_dip.controller;

import org.example.backend_dip.entity.Message;
import org.example.backend_dip.service.ChatService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;
    private final List<Message> messages = new ArrayList<>();

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public Map<String, Object> chatGemini(@RequestBody Map<String, String> request) {
        String message = request.get("message");

        messages.add(new Message("you", message));
        String reply = chatService.sendMessage(message);
        messages.add(new Message("AI", reply));

        return Map.of(
                "reply", reply,
                "messages", messages
        );
    }
}

