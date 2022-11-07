package com.tuhalang.sqleditor.controller;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tuhalang.sqleditor.controller.dto.QueryRequest;
import com.tuhalang.sqleditor.controller.res.QueryResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/console/editor")
@RequiredArgsConstructor
public class EditorController {

    private final JdbcTemplate jdbcTemplate;

    @ModelAttribute("queryRequest")
    public QueryRequest queryRequest() {
        return new QueryRequest();
    }

    @GetMapping
    public String view(ModelMap model) {
        return "console/editor";
    }

    @PostMapping
    public String runQuery(QueryRequest request, BindingResult result, ModelMap model) {
        if (request.getQuery() == null) {
            result.rejectValue("query", "query.error.require-query");
        } else {
            final QueryResult response = new QueryResult();
            try {
                request.valid();
                log.info("run query: {}", request.getQuery());
                final List<Map<String, Object>> rows = jdbcTemplate.queryForList(request.getQuery());
                if (!rows.isEmpty()) {
                    response.setHeader(rows.get(0).keySet());
                    response.setRows(rows);
                }
                for (Map<String, Object> row : rows) {
                    for (Entry<String, Object> entry : row.entrySet()) {
                        log.info("Result: {}: {}", entry.getKey(), entry.getValue());
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                response.setError(e.getMessage());
            } finally {
                model.addAttribute("result", response);
            }
        }
        return view(model);
    }
}
