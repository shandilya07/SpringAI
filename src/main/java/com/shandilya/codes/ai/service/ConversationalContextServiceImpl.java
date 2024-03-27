package com.shandilya.codes.ai.service;

import com.shandilya.codes.ai.constants.PromptConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConversationalContextServiceImpl implements ConversationalContextService {
    private static final Logger LOG = LoggerFactory.getLogger(ConversationalContextServiceImpl.class);

    Map<String, List<String>> contextStore = new HashMap<>();

    @Override
    public List<String> fetchAllContextIds() {
        return new ArrayList<>(contextStore.keySet());
    }

    @Override
    public String preparePromptWithContextHistory(String contextId, String message) {
        LOG.info("Preparing prompt for contextID {} with message {} ", contextId, message);
        if (!contextStore.containsKey(contextId)) {
            List<String> history = new ArrayList<>();
            history.add(message);
            contextStore.put(contextId, history);
            return message;
        } else {
            List<String> history = contextStore.get(contextId);
            history.add(message);
            contextStore.put(contextId, history);
        }

        StringBuilder prompt = new StringBuilder();
        List<String> history = contextStore.get(contextId);
        prompt.append(PromptConstants.PROMPT_WHAT_WERE_WE_TALKING_ABOUT);

        for(int i = 0; i < history.size()-1; i++) {
            prompt.append(padStringWithDelimiter(history.get(i)));
        }

        prompt.append(PromptConstants.PROMPT_DELIMITER_FOR_HISTORICAL_CONTEXT);
        prompt.append(PromptConstants.PROMPT_USE_CONTEXT_IF_NEEDED);
        prompt.append(PromptConstants.PROMPT_THE_CURRENT_QUESTION);
        prompt.append(history.get(history.size()-1));
        LOG.info("Prompt for contextID {} is {}", contextId, prompt);
        return prompt.toString();
    }

    private String padStringWithDelimiter(final String message) {
        return PromptConstants.PROMPT_DELIMITER + message + PromptConstants.PROMPT_DELIMITER;
    }
}