package com.tf1997;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author tf1997
 */
public class StreamLearn {
    public static void main(String[] args) {
        //createStreams();
        //intermediateOperations();
        //terminalOperations();
        shortcircuitingOperations();
        parallelProcessing();
        lazyEvaluation();
    }

    public static void createStreams(){
        List<String> list = Arrays.asList("apple", "banana", "cherry");
        Stream<String> streamFromList = list.stream();

        Stream<Integer> streamFromArray = Arrays.stream(new Integer[] {1, 2, 3});

        Stream<String> streamFromGenerated = Stream.generate(() -> "element").limit(5);
    }
    public static void intermediateOperations(){
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);
        List<Integer> evenSquares = numbers.stream()
                .filter(n -> n % 2 == 0)//过滤， 'n -> n % 2 == 0'为Lambda 表达式
                .map(n -> n * n)//映射
                .collect(Collectors.toList());//收集
        evenSquares.forEach(System.out::println);
    }
    public static void terminalOperations(){
        List<String> fruits = Arrays.asList("apple", "banana", "cherry");
        long count = fruits.stream()
                .filter(fruit -> fruit.startsWith("a"))
                .count();
        System.out.println(count);

        String concatenated = fruits.stream()
                .collect(Collectors.joining(", "));
        System.out.println(concatenated);
    }
    public static void shortcircuitingOperations(){
        /**
         * findFirst 和 findAny:
         * findFirst 用于返回流中的第一个元素，
         * 而 findAny 则返回任意一个元素。当找到满足条件的元素后，流的处理会立即终止。
         */
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        Optional<Integer> firstEven = numbers.stream()
                .filter(n -> n % 2 == 0)
                .findFirst();
        System.out.println(firstEven);

        /**
         * allMatch、anyMatch 和 noneMatch:
         * 这三个操作都是检查流中的元素是否满足某个条件。
         * allMatch 判断是否所有元素都满足条件，
         * anyMatch 判断是否至少有一个元素满足条件，
         * 而 noneMatch 则判断是否没有元素满足条件。一旦满足条件，流的处理会立即终止。
         */
        boolean allEven = numbers.stream()
                .allMatch(n -> n % 2 == 0);

        boolean anyEven = numbers.stream()
                .anyMatch(n -> n % 2 == 0);

        boolean noneEven = numbers.stream()
                .noneMatch(n -> n % 2 == 0);

        /**
         *Limit: limit 操作用于限制流中元素的数量，一旦达到指定的数量，流的处理会立即终止。
         */
        List<Integer> firstThreeEven = numbers.stream()
                .filter(n -> n % 2 == 0)
                .limit(3)
                .collect(Collectors.toList());

        /**
         * skip: skip 操作用于跳过指定数量的元素，然后继续处理剩余的元素。
         */
        List<Integer> afterSkippingFirstTwo = numbers.stream()
                .skip(2)
                .collect(Collectors.toList());

    }

    public static void parallelProcessing(){
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        int sum = numbers.parallelStream()
                .reduce(0, Integer::sum);
        System.out.println(sum);
    }

    public static void lazyEvaluation(){
        Stream<Integer> stream = Stream.of(1, 2, 3, 4, 5)
                .filter(n -> n % 2 == 0); // 并没有执行过滤操作

        List<Integer> evenNumbers = stream.collect(Collectors.toList()); // 执行过滤操作并收集结果
        evenNumbers.forEach(System.out::println);

    }

}