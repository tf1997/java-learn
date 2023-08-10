package com.tf1997;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

@FunctionalInterface
interface MathOperation {
    int operate(int a, int b);
}

public class FunctionalInterfaceExample {
    public static void main(String[] args) {
        //MathOperation addition = (a, b) -> a + b;
        MathOperation addition = Integer::sum;
        System.out.println(addition.operate(5, 3)); // 输出 8
        //下面是一些提供好的函数式接口
        /**
         * Function<T, R>
         * Function 接口定义了一个抽象方法 R apply(T t)，它接受一个类型为 T 的参数，返回一个类型为 R 的结果。
         */
        //Function<String, Integer> strToInt = (s)-> Integer.parseInt(s);
        Function<String, Integer> strToInt = Integer::parseInt;
        int number = strToInt.apply("123");
        System.out.println(number);
        /**
         * Predicate<T>：
         * Predicate 接口定义了一个抽象方法 boolean test(T t)，用于测试给定的输入是否满足条件
         */
        Predicate<Integer> isEven = num -> num % 2 == 0;
        boolean result = isEven.test(6); // 判断数字6是否为偶数
        System.out.println(result);
        /**
         * Consumer<T>：
         * Consumer 接口定义了一个抽象方法 void accept(T t)，用于执行一些操作，不返回结果。
         */
        Consumer<String> printUpperCase = str -> System.out.println(str.toUpperCase());
        printUpperCase.accept("hello"); // 将字符串"hello"打印为大写形式
        /**
         * Supplier<T>：
         * Supplier 接口定义了一个抽象方法 T get()，用于提供一个值，不接受任何参数。
         */
        Supplier<Double> randomDouble = Math::random;
        double value = randomDouble.get(); // 获取一个随机的双精度浮点数
        System.out.println(value);
    }
}
