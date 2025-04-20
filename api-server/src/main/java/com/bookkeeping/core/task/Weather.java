package com.bookkeeping.core.task;

import cn.hutool.http.HttpUtil;
import com.bookkeeping.core.util.RedisKey;
import com.bookkeeping.entity.deepseek.ChatRequest;
import com.bookkeeping.entity.deepseek.ChatResponse;
import com.bookkeeping.service.impl.DeepSeekLegacyClient;
import com.bookkeeping.service.impl.StringRedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author bookkeeping
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class Weather {
    private final DeepSeekLegacyClient client;
    private final StringRedisService stringRedisService;

    // 每60秒执行一次
    @Scheduled(fixedRate = 60000)
    public void run() {

        String redisKey = RedisKey.getWeatherKey();

        String ip = HttpUtil.get("https://cdid.c-ctrip.com/model-poc2/h");

        if (stringRedisService.hashKey(redisKey).equals(Boolean.TRUE)) {
            log.info("{} 天气数据已存在", redisKey);
            return;
        }
        ChatRequest request = ChatRequest.builder()
                .model("deepseek-chat")
                .messages(List.of(
                        new ChatRequest.Message("system", "请分析以下气候数据并给出人类可读的报告：\n\n2025-04-21\nIP地址："+ip+"\n\n要求不要给我返回AI提示的注意事项并且地址根据IP来计算，并且返回英语格式的数据。\n返回格式要求如下：\n地址：{根据IP得到的地址，如北京}\n1.温度分析\n指标\t数值\t出现时间\n最高温度\t24°C\t14:00 - 15:00\n最低温度\t16°C\t05:00 - 06:00\n昼夜温差\t8°C\t\n\n2.降水概率\n全天无雨：降水概率 <10%\n\n湿度范围：65% (早晨) → 45% (午后)\n\n云量覆盖：30%（多云间晴）\n\n3. 穿衣建议\n推荐搭配：\n\n上午/夜晚：长袖T恤 + 薄外套（防风）\n\n午后：短袖 + 防晒衣（紫外线指数中等）\n\n特殊提示：建议携带折叠伞，尽管降水概率低，但春季天气多变")
                ))
                .build();

        try {
            ChatResponse response = client.chatCompletion(request);
            stringRedisService.setValueWithExpire(redisKey, response.getChoices().get(0).getMessage().getContent(), 2, TimeUnit.DAYS);
            log.info("天气数据获取成功");
        } catch (RuntimeException e) {
            log.error("天气数据获取失败", e);
        }

    }
}
