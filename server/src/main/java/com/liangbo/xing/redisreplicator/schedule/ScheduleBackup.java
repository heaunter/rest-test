package com.liangbo.xing.redisreplicator.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.stream.IntStream;

/**
 * @author xingliangbo
 * @version $Id: v 0.1 17/12/27 下午9:00 xingliangbo Exp $
 */
@EnableScheduling
@Configuration
public class ScheduleBackup {

    private final Logger logger = LoggerFactory.getLogger(ScheduleBackup.class);

    public static final String REDIS_KEY = "exam_eid";

    public static final String HASH_EY = "name";

    private static int cursor = 0;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Scheduled(cron = "0 0/2 * * * ?")
    public void rdbBackUp() {
        logger.info("cursor:" + cursor);
        if (cursor > 0) {
            return;
        }

        IntStream.range(1, 11).forEach(value -> {
            String uuid = UUID.randomUUID().toString();
            String hashKey = HASH_EY + value + uuid;
            redisTemplate.opsForHash().put(REDIS_KEY, hashKey, value);
        });
        cursor++;
    }

}
