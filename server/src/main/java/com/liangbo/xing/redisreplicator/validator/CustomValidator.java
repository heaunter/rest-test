package com.liangbo.xing.redisreplicator.validator;

import com.liangbo.xing.redisreplicator.constraints.CustomAnnotation;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Description 自定义校验器
 * @Author Mr.nobody
 * @Date 2021/3/11
 * @Version 1.0
 */
public class CustomValidator implements ConstraintValidator<CustomAnnotation, String> {

    // 是否强制校验
    private boolean required;

    @Override
    public void initialize(CustomAnnotation constraintAnnotation) {
        this.required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        if (required) {
            return !StringUtils.isEmpty(name) && name.startsWith("XXXX");
        }
        return false;
    }
}
