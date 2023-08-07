package com.tf1997;

@FunctionalInterface
interface MathOperation {
    int operate(int a, int b);
}

public class FunctionalInterfaceExample {
    public static void main(String[] args) {
        //MathOperation addition = (a, b) -> a + b;
        MathOperation addition = Integer::sum;
        System.out.println(addition.operate(5, 3)); // 输出 8
    }
}
