package com.tf1997.configAnnotation;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author tf1997
 * @date 2023/10/18 17:31
 **/

public class TestCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        //一些逻辑
        return false;
    }
}
