package com.royi.api.AsyncMergeTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.royi.api.dto.async.ProductDto;
import com.royi.api.dto.async.UserProductDto;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class CompletableFutureDtoTest {

    @Test
    void completableFutureDtoTest() {
        mergeResults().thenAccept(result -> {
            // Print the merged result
            System.out.println(result);
            printResultAsJson(result);
        }).join(); // Wait for the tasks to complete
    }

    private void printResultAsJson(List<UserProductDto> result) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            System.out.println("mapper.writeValueAsString(result) = " + mapper.writeValueAsString(result));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting result to JSON", e);
        }
    }

    private CompletableFuture<List<Map<String, String>>> queryDbAsync() {
        return CompletableFuture.supplyAsync(() -> {
            // Simulate DB query
            simulateDelay();
            System.out.println("DB 비동기");
            return Arrays.asList(
                    Map.of("userId", "A", "usernm", "이름1"),
                    Map.of("userId", "B", "usernm", "이름2")
            );
        });
    }

    private CompletableFuture<List<Map<String, String>>> callProcedureAsync() {
        return CompletableFuture.supplyAsync(() -> {
            // Simulate procedure call
            simulateDelay();
            System.out.println("프로시저 비동기");
            return Arrays.asList(
                    Map.of("userId", "A", "orderCnt", "2", "productNm", "마우스"),
                    Map.of("userId", "B", "orderCnt", "10", "productNm", "키보드")
            );
        });
    }

    private void simulateDelay() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
        }
    }

    private CompletableFuture<List<UserProductDto>> mergeResults() {
        // Run both tasks concurrently
        CompletableFuture<List<Map<String, String>>> dbFuture = queryDbAsync();
        CompletableFuture<List<Map<String, String>>> procFuture = callProcedureAsync();

        return dbFuture.thenCombine(procFuture, this::combineResults);
    }

    private List<UserProductDto> combineResults(List<Map<String, String>> dbResult, List<Map<String, String>> procResult) {
        // Create a map for easier access to procedure call results
        Map<String, ProductDto> procMap = procResult.stream()
                .collect(Collectors.toMap(
                        procEntry -> procEntry.get("userId"),
                        procEntry -> ProductDto.builder()
                                .productNm(procEntry.get("productNm"))
                                .orderCnt(procEntry.get("orderCnt"))
                                .build()
                ));

        // Combine DB results with procedure results
        return dbResult.stream()
                .map(dbEntry -> {
                    String userId = dbEntry.get("userId");
                    ProductDto product = procMap.get(userId);
                    return UserProductDto.builder()
                            .userId(userId)
                            .usernm(dbEntry.get("usernm"))
                            .product(product)
                            .build();
                })
                .collect(Collectors.toList());
    }

//    리팩토링 전
//    private CompletableFuture<List<UserProductDto>> mergeResults() {
//        // Run both tasks concurrently
//        CompletableFuture<List<Map<String, String>>> dbFuture = queryDbAsync();
//        CompletableFuture<List<Map<String, String>>> procFuture = callProcedureAsync();
//
//        return dbFuture.thenCombine(procFuture, (dbResult, procResult) -> {
//            // Merge the results based on userId
//            List<UserProductDto> mergedResult = new ArrayList<>();
//
//            // Create a map for easier access to procedure call results
//            Map<String, Map<String, String>> procMap = new HashMap<>();
//            for (Map<String, String> procEntry : procResult) {
//                String userId = procEntry.get("userId");
//                procMap.put(userId, procEntry);
//            }
//
//            // Combine DB results with procedure results
//            for (Map<String, String> dbEntry : dbResult) {
//                String userId = dbEntry.get("userId");
//                ProductDto product = null;
//
//                // Get product info if available
//                Map<String, String> productInfo = procMap.get(userId);
//                if (productInfo != null) {
//                    product = ProductDto.builder()
//                            .productNm(productInfo.get("productNm"))
//                            .orderCnt(productInfo.get("orderCnt"))
//                            .build();
//                }
//
//                // Create UserProductDTO and add to result
//                UserProductDto userProduct = UserProductDto.builder()
//                        .product(product)
//                        .userId(userId)
//                        .usernm(dbEntry.get("usernm"))
//                        .build();
//                mergedResult.add(userProduct);
//            }
//
//            return mergedResult;
//        });
//    }
}
