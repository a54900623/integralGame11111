package com.party.game.common.utils;

import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

/**
 * @author wenqiang.luo date:16/4/12
 */
public final class ValidatorUtils {

    /**
     * 私有构造方法
     */
    private ValidatorUtils() {}

    /**
     * 获取默认验证器
     * @return javax.validation.Validator
     */
    public static Validator getDefaultValidator() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        return validatorFactory.getValidator();
    }

    /**
     * 验证Bean
     * @param bean 待验证Bean
     * @param <T> Bean模板
     * @return 违法描述集合
     */
    public static <T> Set<String> validate(T bean) {

        Set<String> violations = Sets.newHashSet();

        Validator validator = ValidatorUtils.getDefaultValidator();
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(bean);
        if (CollectionUtils.isNotEmpty(constraintViolations)) {
            for (ConstraintViolation<T> constraintViolation : constraintViolations) {
                violations.add(constraintViolation.getMessage());
            }
        }

        return violations;
    }
}
