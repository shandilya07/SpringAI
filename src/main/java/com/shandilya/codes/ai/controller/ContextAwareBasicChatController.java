package com.shandilya.codes.ai.controller;

import com.shandilya.codes.ai.service.ConversationalContextService;
import com.shandilya.codes.ai.service.ConversationalContextServiceImpl;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/v2/ai")
public class ContextAwareBasicChatController {

    private final OpenAiChatClient chatClient;
    private final ConversationalContextService contextService;

    @Autowired
    public ContextAwareBasicChatController(OpenAiChatClient chatClient,
                                           ConversationalContextServiceImpl contextService) {
        this.chatClient = chatClient;
        this.contextService = contextService;
    }

    @GetMapping("/chat")
    public Map<String, String> chat(@RequestParam(value = "contextId", defaultValue = "") String contextId,
                                    @RequestParam(value = "message", defaultValue = "Tell me a random fact") String message) {
        if (contextId.isEmpty()) {
            contextId = UUID.randomUUID().toString();
        }

        String prompt = contextService.preparePromptWithContextHistory(contextId, message);
        return Map.of("generated", chatClient.call(prompt));
    }

    @GetMapping("/contexts")
    public List<String> fetchAllContextIds() {
        return contextService.fetchAllContextIds();
    }
}