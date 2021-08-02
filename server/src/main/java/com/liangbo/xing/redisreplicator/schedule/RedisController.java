package com.liangbo.xing.redisreplicator.schedule;

import com.alibaba.fastjson.JSONObject;
import com.liangbo.xing.redisreplicator.model.ExamPaper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RestController
public class RedisController {
    private final Logger logger = LoggerFactory.getLogger(RedisController.class);

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private Validator validator;

    @GetMapping("/redis")
    public String redis(@RequestParam("value") String match) throws IOException {

        logger.info("scan pattern:{}", match);
        ScanOptions scanOptions = ScanOptions.scanOptions().match(match).count(100000).build();
        Cursor<Map.Entry<Object, Object>> scan = redisTemplate.opsForHash().scan(ScheduleBackup.REDIS_MAPPING_KEY, scanOptions);
        while (scan.hasNext()) {
            Object next = scan.next();
            logger.info("result[scan pattern:{}]: {}", match, JSONObject.toJSONString(next, true));
        }
        scan.close();
        return "success";
    }

    @GetMapping("/set")
    public String set() throws IOException {
        Set<Object> container = new HashSet<>();

        Map<String, Object> first = new HashMap<>();
        first.put("name", "zhangsan");
        first.put("age", "20");

        container.add(first);

        Map<String, Object> second = new HashMap<>();
        second.put("age", "20");
        second.put("name", "zhangsan");
        container.add(second);

        logger.info("size:" + container.size());

        return "success";
    }

    @GetMapping("/valid")
    public String valid() {
        ExamPaper paper = new ExamPaper("zhangsan", "fwefk", 345L, -23);
        Set<ConstraintViolation<ExamPaper>> validate = validator.validate(paper);
        validate.forEach(violation -> {
            logger.info(violation.getMessage() + " " + violation.getPropertyPath());
        });

        return "success";
    }

}
