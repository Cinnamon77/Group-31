package bookkeeping.system.bookkeepingsys.deepSeekUtils;

import com.fasterxml.jackson.databind.JsonNode;
import okhttp3.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TalkDeepSeek {
    private static final String API_URL = "https://api.siliconflow.cn/v1/chat/completions";
    private static final String API_KEY = "sk-dpnjccuoklfxdoqwiadriofbanaqvucymzovpymkvlfiilzb"; // 实际项目应从安全配置读取

    public static void main(String[] args) {
        try {
            String systemContent = "你是一个专业的会计师，请根据用户的支出和收入记录用专业且友善的语气回答用户的经济问题然后在给出一点建议。将内容以html的格式输出";
            String userContent = "我要怎么省钱呢";
            String response = callSiliconFlowAPI(systemContent, userContent);
//            String content = parseResponseContent(response);
            System.out.println(response);
        } catch (Exception e) {
            System.err.println("API调用失败: " + e.getMessage());
        }
    }

    public static String callSiliconFlowAPI(String systemContent, String userContent) throws IOException, ResponseParseException {
        // 1. 构建请求体
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "Qwen/Qwen2.5-VL-72B-Instruct");
        requestBody.put("stream", false);
        requestBody.put("max_tokens", 2048);
        requestBody.put("enable_thinking", true);
        requestBody.put("thinking_budget", 4096);
        requestBody.put("min_p", 0.05);
        requestBody.put("temperature", 0.7);
        requestBody.put("top_p", 0.7);
        requestBody.put("top_k", 50);
        requestBody.put("frequency_penalty", 0.5);
        requestBody.put("n", 1);
        requestBody.put("stop", new String[0]);

        // 创建消息数组，根据systemContent是否为空决定是否包含system消息
        @SuppressWarnings("unchecked")
        Map<String, String>[] messages;
        if (systemContent != null && !systemContent.isEmpty()) {
            messages = new Map[2];
            Map<String, String> systemMessage = new HashMap<>();
            systemMessage.put("role", "system");
            systemMessage.put("content", systemContent);
            messages[0] = systemMessage;

            Map<String, String> userMessage = new HashMap<>();
            userMessage.put("role", "user");
            userMessage.put("content", userContent);
            messages[1] = userMessage;
        } else {
            messages = new Map[1];
            Map<String, String> userMessage = new HashMap<>();
            userMessage.put("role", "user");
            userMessage.put("content", userContent);
            messages[0] = userMessage;
        }
        requestBody.put("messages", messages);

        Map<String, String> responseFormat = new HashMap<>();
        responseFormat.put("type", "text");
        requestBody.put("response_format", responseFormat);

        String jsonBody = mapper.writeValueAsString(requestBody);

        // 2. 创建HTTP客户端，设置更长的超时时间
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, java.util.concurrent.TimeUnit.SECONDS)  // 连接超时时间改为60秒
                .readTimeout(120, java.util.concurrent.TimeUnit.SECONDS)    // 读取超时时间改为120秒
                .build();

        // 3. 构建请求
        Request request = new Request.Builder()
                .url(API_URL)
                .post(RequestBody.create(jsonBody, MediaType.parse("application/json")))
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Content-Type", "application/json")
                .build();

        System.out.println("发送请求");
        // 4. 发送请求并处理响应
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response.code() + ": " + response.body().string());
            }
            String context = parseResponseContent(response.body().string());
            System.out.println(context);
            return context;
        }
    }

    /**
     * 从API响应中解析content内容
     * @param responseJson API返回的JSON字符串
     * @return 解析得到的content内容
     * @throws ResponseParseException 当解析失败时抛出
     */
    public static String parseResponseContent(String responseJson) throws ResponseParseException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(responseJson);

            // 检查是否有choices数组
            if (!rootNode.has("choices") || !rootNode.get("choices").isArray() || rootNode.get("choices").size() == 0) {
                throw new ResponseParseException("响应中缺少choices数组或choices为空");
            }

            JsonNode firstChoice = rootNode.get("choices").get(0);

            // 检查是否有message对象
            if (!firstChoice.has("message")) {
                throw new ResponseParseException("choices中缺少message对象");
            }

            JsonNode messageNode = firstChoice.get("message");

            // 检查是否有content字段
            if (!messageNode.has("content")) {
                throw new ResponseParseException("message中缺少content字段");
            }

            return messageNode.get("content").asText();
        } catch (IOException e) {
            throw new ResponseParseException("JSON解析失败: " + e.getMessage());
        }
    }

    /**
     * 自定义响应解析异常
     */
    public static class ResponseParseException extends Exception {
        public ResponseParseException(String message) {
            super(message);
        }
    }
}