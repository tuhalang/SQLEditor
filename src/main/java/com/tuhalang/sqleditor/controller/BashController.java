package com.tuhalang.sqleditor.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tuhalang.sqleditor.controller.dto.BashRequest;
import com.tuhalang.sqleditor.controller.dto.QueryRequest;
import com.tuhalang.sqleditor.controller.res.BashResult;
import com.tuhalang.sqleditor.controller.res.QueryResult;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/console/bash")
public class BashController {

    @ModelAttribute("bashRequest")
    public BashRequest bashRequest() {
        return new BashRequest();
    }

    @GetMapping
    public String view(ModelMap model) {
        return "console/bash";
    }

    @PostMapping
    public String runQuery(BashRequest request, BindingResult result, ModelMap model) {
        if (request.getScript() == null) {
            result.rejectValue("query", "query.error.require-query");
        } else {
            final BashResult response = new BashResult();
            try {
                log.info("run command: {}", request.getScript());
                ProcessBuilder processBuilder = new ProcessBuilder();
                processBuilder.command("bash", "-c", request.getScript());
                Process process = processBuilder.start();

                StringBuilder output = new StringBuilder();

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(process.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line + "\n");
                }
                reader.close();
                int exitVal = process.waitFor();
                response.setOutput(line);
                log.info("Result: {}", line);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                response.setOutput(e.getMessage());
            } finally {
                model.addAttribute("result", response);
            }
        }
        return view(model);
    }
}
