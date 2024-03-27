package com.shandilya.codes.ai.controller;

import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/v1/ai")
public class BasicChatController {

    private final OpenAiChatClient chatClient;

    @Autowired
    public BasicChatController(OpenAiChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/chat")
    public Map<String, String> chat(@RequestParam(value = "message", defaultValue = "Tell me a random fact") String message) {
        return Map.of("generated", chatClient.call(message));
    }
}