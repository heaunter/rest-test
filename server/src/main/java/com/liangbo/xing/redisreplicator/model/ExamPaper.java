package com.liangbo.xing.redisreplicator.model;


import com.liangbo.xing.redisreplicator.constraints.CustomAnnotation;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

public class ExamPaper {

    public ExamPaper() {
    }

    public ExamPaper(String name, String email, Long duration, int size) {
        this.name = name;
        this.email = email;
        this.duration = duration;
        this.size = size;
    }

    @NotBlank
    @CustomAnnotation
    private String name;

    @Email
    private String email;

    @Range(message = "超过范围", min = 500, max = 100000000)
    private Long duration;

    @Positive(message = "非负数要求")
    private int size;

}
