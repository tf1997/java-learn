package com.tf1997;

import com.tf1997.configAnnotation.ConfigTest;
import com.tf1997.configAnnotation.TestBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author tf1997
 * @date 2023/10/18 16:58
 **/

@SpringBootApplication
public class TestSpringApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(TestSpringApplication.class, args);
        TestBean testBean = applicationContext.getBean(TestBean.class);
        System.out.println(testBean);
    }
}
