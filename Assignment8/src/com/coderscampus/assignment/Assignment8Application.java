package com.coderscampus.assignment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Assignment8Application {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Assignment8 baseCode = new Assignment8();
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        int fetchTimes = 1000;

        List<CompletableFuture<List<Integer>>> futures = new ArrayList<>();

        for (int i = 0; i < fetchTimes; i++) {
            CompletableFuture<List<Integer>> future = CompletableFuture.supplyAsync(baseCode::getNumbers, cachedPool);
            futures.add(future);
        }

        List<Integer> listOfNumbers = futures.stream()
                .flatMap(future -> future.join().stream())
                .collect(Collectors.toList());

        Map<Integer, Long> countOfNumbers = listOfNumbers.parallelStream()
                .collect(Collectors.groupingByConcurrent(Function.identity(), Collectors.counting()));

        countOfNumbers.forEach((k, v) -> System.out.println(k + "=" + v));
    }
}
