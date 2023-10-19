package com.tf1997.configAnnotation;

import org.springframework.beans.factory.FactoryBean;

/**
 * @author tf1997
 * @date 2023/10/18 19:04
 **/

public class TestFactoryBean implements FactoryBean<TestBean> {
    @Override
    public TestBean getObject() throws Exception {
        return new TestBean();
    }

    @Override
    public Class<?> getObjectType() {
        return TestBean.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
