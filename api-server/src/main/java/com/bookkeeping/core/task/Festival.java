package com.bookkeeping.core.task;

import com.bookkeeping.core.util.RedisKey;
import com.bookkeeping.entity.deepseek.ChatRequest;
import com.bookkeeping.entity.deepseek.ChatResponse;
import com.bookkeeping.service.impl.DeepSeekLegacyClient;
import com.bookkeeping.service.impl.StringRedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author bookkeeping
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class Festival {
    private final DeepSeekLegacyClient client;
    private final StringRedisService stringRedisService;

    // 每60秒执行一次
    @Scheduled(fixedRate = 60000)
    public void run() {

        String redisKey = RedisKey.getFestival();


        if (stringRedisService.hashKey(redisKey).equals(Boolean.TRUE)) {
            log.info("{} 节日数据已存在", redisKey);
            return;
        }
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String prompt = """
                今天是 {today}，请按以下格式返回重要日期：
                    今天是：{yyyy-MM-dd格式的日期}
                    中国节日
                    - 节日名称1（类型）：简要说明
                    - 节日名称2（类型）：简要说明
                   \s
                    国际节日
                    - 节日名称（国际组织/来源）：简要说明
                   \s
                    要求：
                    1. 仅返回真实存在的节日，不要虚构
                    2. 中国传统节气需标注（节气）
                    3. 国际节日需注明发起组织（如联合国）
                    4. 不要给我返回AI注意事项提示信息
                    5. 返回内容用英语返回
                """.replace("{today}", date);
        ChatRequest request = ChatRequest.builder()
                .model("deepseek-chat")
                .messages(List.of(
                        new ChatRequest.Message("system", prompt)
                ))
                .build();

        try {
            ChatResponse response = client.chatCompletion(request);
            stringRedisService.setValueWithExpire(redisKey, response.getChoices().get(0).getMessage().getContent(), 2, TimeUnit.DAYS);
            log.info("节日数据获取成功");
        } catch (RuntimeException e) {
            log.error("节日数据获取失败", e);
        }

    }
}
