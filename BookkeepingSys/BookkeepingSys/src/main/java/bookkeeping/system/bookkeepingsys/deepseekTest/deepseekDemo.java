package bookkeeping.system.bookkeepingsys.deepseekTest;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class deepseekDemo {
    private static final String API_URL = "https://api.siliconflow.cn/v1/chat/completions";
    private static final String API_KEY = "sk-dpnjccuoklfxdoqwiadriofbanaqvucymzovpymkvlfiilzb";

    public static void main(String[] args) {
        try {
            String response = callSiliconFlowAPI();
            System.out.println("API Response:\n" + response);
        } catch (IOException e) {
            System.err.println("API调用失败: " + e.getMessage());
        }
    }

    public static String callSiliconFlowAPI() throws IOException {
        // 1. 创建JSON请求体
        String requestBody = buildRequestBody();

        // 2. 创建HTTP连接
        HttpURLConnection connection = createConnection();

        // 3. 发送请求
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        // 4. 处理响应
        return handleResponse(connection);
    }

    private static String buildRequestBody() {
        return """
                {
                    "model": "Qwen/Qwen2.5-VL-72B-Instruct",
                    "stream": false,
                    "max_tokens": 512,
                    "enable_thinking": true,
                    "thinking_budget": 4096,
                    "min_p": 0.05,
                    "temperature": 0.7,
                    "top_p": 0.7,
                    "top_k": 50,
                    "frequency_penalty": 0.5,
                    "n": 1,
                    "stop": [],
                    "messages": [
                        {
                            "role": "user",
                            "content": "你是谁啊"
                        }
                    ],
                    "response_format": {
                        "type": "text"
                    }
                }""";
    }

    private static HttpURLConnection createConnection() throws IOException {
        URL url = new URL(API_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);
        connection.setConnectTimeout(30000); // 30秒连接超时
        connection.setReadTimeout(60000);   // 60秒读取超时

        return connection;
    }

    private static String handleResponse(HttpURLConnection connection) throws IOException {
        int statusCode = connection.getResponseCode();

        try (InputStream is = statusCode == 200 ?
                connection.getInputStream() : connection.getErrorStream()) {

            String response = new String(is.readAllBytes(), StandardCharsets.UTF_8);

            if (statusCode != 200) {
                throw new IOException("API请求失败: HTTP " + statusCode + "\n" + response);
            }

            return response;
        }
    }
}