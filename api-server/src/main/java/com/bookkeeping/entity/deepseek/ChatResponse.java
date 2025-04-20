package com.bookkeeping.entity.deepseek;

import lombok.Data;

import java.util.List;

/**
 * 响应体
 * @author bookkeeping
 **/
@Data
public class ChatResponse {
    private String id;
    private String object;
    private long created;
    private List<Choice> choices;

    @Data
    public static class Choice {
        private Message message;
        private int index;
        private String finish_reason;

        @Data
        public static class Message {
            private String role;
            private String content;
        }
    }
}
