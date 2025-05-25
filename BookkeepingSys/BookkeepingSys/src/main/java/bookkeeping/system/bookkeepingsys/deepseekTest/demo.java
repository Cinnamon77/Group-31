package bookkeeping.system.bookkeepingsys.deepseekTest;

import okhttp3.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class demo {
    private static final String API_URL = "https://api.siliconflow.cn/v1/chat/completions";
    private static final String API_KEY = "sk-dpnjccuoklfxdoqwiadriofbanaqvucymzovpymkvlfiilzb"; // 实际项目应从安全配置读取

    public static void main(String[] args) {
        try {
            String response = callSiliconFlowAPI();
            System.out.println("API Response: " + response);
        } catch (IOException e) {
            System.err.println("API调用失败: " + e.getMessage());
        }
    }

    public static String callSiliconFlowAPI() throws IOException {
        // 1. 构建请求体
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "Qwen/Qwen2.5-VL-72B-Instruct");
        requestBody.put("stream", false);
        requestBody.put("max_tokens", 512);
        requestBody.put("enable_thinking", true);
        requestBody.put("thinking_budget", 4096);
        requestBody.put("min_p", 0.05);
        requestBody.put("temperature", 0.7);
        requestBody.put("top_p", 0.7);
        requestBody.put("top_k", 50);
        requestBody.put("frequency_penalty", 0.5);
        requestBody.put("n", 1);
        requestBody.put("stop", new String[0]);

        Map<String, String>[] messages = new Map[2];
        messages[0] = Map.of("role", "system", "content", "你是一个专业的医生助手，请用专业且友善的语气回答用户的健康问题。给出的建议要具体且实用。如果遇到严重的症状，建议就医。将内容以html的格式输出");
        messages[1] = Map.of("role", "user", "content", "怎么预防感冒呢");
        requestBody.put("messages", messages);

        Map<String, String> responseFormat = Map.of("type", "text");
        requestBody.put("response_format", responseFormat);

        String jsonBody = mapper.writeValueAsString(requestBody);

        // 2. 创建HTTP客户端
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .build();

        // 3. 构建请求
        Request request = new Request.Builder()
                .url(API_URL)
                .post(RequestBody.create(jsonBody, MediaType.parse("application/json")))
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Content-Type", "application/json")
                .build();

        // 4. 发送请求并处理响应
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response.code() + ": " + response.body().string());
            }
            return response.body().string();
        }
    }
}