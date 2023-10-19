package com.tf1997.configAnnotation;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author tf1997
 * @date 2023/10/18 17:45
 **/

public class TestImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        // 在这里根据条件判断，返回要导入的配置类的全限定类名
        // 这里简单返回一个示例配置类
        return new String[]{"com.tf1997.configAnnotation.TestBean"};
    }
}
