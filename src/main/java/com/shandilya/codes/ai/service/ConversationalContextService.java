package com.shandilya.codes.ai.service;

import java.util.List;

public interface ConversationalContextService {
    List<String> fetchAllContextIds();
    String preparePromptWithContextHistory(final String contextId, final String message);
}