package com.example.demo.config;

import com.example.demo.exception.InvalidApiKeyException;
import com.example.demo.service.AppService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ApiKeyFilter implements Filter {
    private final AppService appService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String path = req.getRequestURI();

        if (path.startsWith("/logs") && req.getMethod().equals("POST")) {
            String apiKey = req.getHeader("X-API-KEY");
            if (apiKey == null || apiKey.isBlank()) {
                res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing X-API-KEY");
                return;
            }
            try {
                appService.validarApiKey(apiKey);
            } catch (InvalidApiKeyException e) {
                res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid API Key");
                return;
            }
        }
        chain.doFilter(request, response);
    }
}
