package com.app.login.common.utils;

import org.springframework.stereotype.Component;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.util.Set;

/**
 * ValidationFacade
 * @authr megadotnet
 * @date 2022/5/22
 */
@Component
public class ValidationFacade {

    private final Validator validator;

    public ValidationFacade(Validator validator) {
        this.validator = validator;
    }

    /**
     * 验证给定对象是否符合指定的约束条件
     *
     * @param object 待验证的对象
     * @param groups 验证约束的组，可变参数
     * @param <T> 泛型参数，表示待验证对象的类型
     *
     * 此方法使用Bean Validation API来验证给定对象是否符合指定的约束条件如果对象不符合约束，
     * 则抛出ConstraintViolationException异常，包含所有的约束违规信息
     *
     * 为什么这么做：通过在代码中使用验证注解（例如@NotNull、@Size等），我们可以确保数据的完整性
     * 和有效性，而不需要在代码中手动编写大量的if语句来检查数据的合法性这种方式提高了代码的可读性
     * 和可维护性
     *
     * 特别之处：此方法使用了泛型和可变参数，使其能够灵活地应用于不同的场景和对象类型
     */
    public <T> void validate(T object, Class<?>... groups) {
        // 执行对象的验证，根据其字段上的注解和指定的组来检查是否符合规范
        Set<ConstraintViolation<T>> violations = validator.validate(object, groups);
        // 如果存在验证错误，抛出异常，将所有的约束违规信息传递给调用者
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}