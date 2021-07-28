package com.liangbo.xing.redisreplicator.schedule;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
public class RedisController {
    private final Logger logger = LoggerFactory.getLogger(RedisController.class);

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @GetMapping("/redis")
    public String test(@RequestParam("value") String match) throws IOException {

        logger.info("scan pattern:{}", match);
        ScanOptions scanOptions = ScanOptions.scanOptions().match(match).build();
        Cursor scan = redisTemplate.opsForHash().scan(ScheduleBackup.REDIS_KEY, scanOptions);
        while (scan.hasNext()) {
            Object next = scan.next();
            logger.info("result[scan pattern:{}]: {}", match, JSONObject.toJSON(next));
        }
        scan.close();
        return "success";
    }

}
