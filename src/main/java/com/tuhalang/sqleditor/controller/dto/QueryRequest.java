package com.tuhalang.sqleditor.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QueryRequest {
    private String query;
    private Long limit = 10L;

    public void valid() {
        if (query.contains("UPDATE") || query.contains("CREATE") || query.contains("DELETE")) {
            throw new RuntimeException("Invalid query! Only accept SELECT query!");
        }

        if (query.endsWith(";")) {
            query = query.substring(0, query.length() - 1);
        }
        if (!query.contains("WHERE")) {
            query += " WHERE ROWNUM < " + limit;
        }
    }
}
