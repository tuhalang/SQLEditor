package com.tuhalang.sqleditor.controller.res;

import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QueryResult {
    private Set<String> header;
    private List<Map<String, Object>> rows;
    private String error;
}
