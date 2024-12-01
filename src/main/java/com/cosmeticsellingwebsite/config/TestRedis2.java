package com.cosmeticsellingwebsite.config;

import com.cosmeticsellingwebsite.service.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class TestRedis2 {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    RedisService redisService;
    @GetMapping("/testredistao")
    public void createPasswordResetTokenForUser(String token) {
//        redisService.save("abc", "123", 1); // Lưu otp với thời gian hết hạn 1 giờ
//        redisTemplate.opsForValue().set("abc", "123", 1, TimeUnit.HOURS); // Lưu token với thời gian hết hạn 1 giờ
        redisTemplate.opsForValue().set("tt", "123", 10, TimeUnit.SECONDS);

        // ...
    }
    @GetMapping("/testredis")
    public Long validatePasswordResetToken() {
        return redisTemplate.getExpire("tt");
    }
}
