package com.kuafuweb.llm.chat;

import com.kuafuweb.llm.model.ChatResponse;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface Chat {

    void callApiStream(String query, String conversationId, String userId, SseEmitter sseEmitter);

    ChatResponse callApiBlock(String query, String conversationId, String userId);
}





