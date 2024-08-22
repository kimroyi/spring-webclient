package com.royi.api.AsyncMergeTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class CompletableFutureMapTest {

    @Test
    void CompetableFutureMapTest() {
        mergeResults().thenAccept(result -> {
            // Print the merged result
            System.out.println(result);
            ObjectMapper mapper = new ObjectMapper();
            try {
                System.out.println("mapper.writeValueAsString(result) = " + mapper.writeValueAsString(result));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }).join(); // Wait for the tasks to complete
    }

    private CompletableFuture<List<Map<String, String>>> queryDbAsync() {
        return CompletableFuture.supplyAsync(() -> {
            // Simulate DB query
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
            List<Map<String, String>> result = Arrays.asList(
                    Map.of("userId", "A", "usernm", "이름1")
                    , Map.of("userId", "B", "usernm", "이름2")
            );

            return result;
        });
    }

    private CompletableFuture<List<Map<String, String>>> callProcedureAsync() {
        return CompletableFuture.supplyAsync(() -> {
            // Simulate procedure call
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
            List<Map<String, String>> result = Arrays.asList(
                    Map.of("userId", "A", "orderCnt", "2", "productNm", "마우스")
                    , Map.of("userId", "B", "orderCnt", "10", "productNm", "키보드")
            );
            return result;
        });
    }

    private CompletableFuture<List<Map<String, Object>>> mergeResults() {
        // Run both tasks concurrently
        CompletableFuture<List<Map<String, String>>> dbFuture = queryDbAsync();
        CompletableFuture<List<Map<String, String>>> procFuture = callProcedureAsync();

        return dbFuture.thenCombine(procFuture, (dbResult, procResult) -> {
            // Merge the results based on userId
            List<Map<String, Object>> mergedResult = new ArrayList<>();

            // Create a map for easier access to procedure call results
            Map<String, Map<String, String>> procMap = new HashMap<>();
            for (Map<String, String> procEntry : procResult) {
                String userId = procEntry.get("userId");
                procMap.put(userId, procEntry);
            }

            // Combine DB results with procedure results
            for (Map<String, String> dbEntry : dbResult) {
                String userId = dbEntry.get("userId");
                Map<String, Object> mergedEntry = new HashMap<>(dbEntry);

                // Get product info if available
                Map<String, String> productInfo = procMap.get(userId);
                if (productInfo != null) {
                    mergedEntry.put("product", Map.of(
                            "productNm", productInfo.get("productNm"),
                            "orderCnt", productInfo.get("orderCnt")
                    ));
                } else {
                    // If no product info, just add an empty product field
                    mergedEntry.put("product", Collections.emptyMap());
                }

                mergedResult.add(mergedEntry);
            }

            return mergedResult;
        });
    }
}
