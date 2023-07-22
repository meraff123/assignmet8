package com.coderscampus.assignment;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Assignment8 {
    private List<Integer> numbers = null;
    private AtomicInteger i = new AtomicInteger(0);
    private Object lock = new Object(); // A lock for synchronization

    public Assignment8() {
        try {
            // Make sure you download the output.txt file for Assignment 8
            // and place the file in the root of your Java project
            numbers = Files.readAllLines(Paths.get("output.txt"))
                    .stream()
                    .map(n -> Integer.parseInt(n))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Integer> getNumbers() {
        int start, end;
        synchronized (lock) {
            start = i.get();
            end = i.addAndGet(1000);

            System.out.println("Starting to fetch records " + start + " to " + (end));
        }
        // force thread to pause for half a second to simulate actual Http / API traffic delay
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }

        List<Integer> newList = new ArrayList<>();
        synchronized (lock) {
            IntStream.range(start, end)
                    .forEach(n -> {
                        if (n < numbers.size()) { // Ensure we don't go beyond the list's size
                            newList.add(numbers.get(n));
                        }
                    });
        }
        System.out.println("Done Fetching records " + start + " to " + (end));
        return newList;
    }
}
