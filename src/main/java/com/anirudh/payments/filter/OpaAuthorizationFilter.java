package com.anirudh.payments.filter;

import com.anirudh.payments.dto.OpaDecision;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

public class OpaAuthorizationFilter extends OncePerRequestFilter {

    private final RestClient opaClient;

    public OpaAuthorizationFilter(RestClient.Builder builder, @Value("${opa.url}") String opaUrl) {
        this.opaClient = builder.baseUrl(opaUrl).build();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        Map<String, Object> body = Map.of("input", Map.of(
                "method", request.getMethod(),
                "path", request.getRequestURI(),
                "userId", String.valueOf(request.getHeader("X-User-Id")),
                "roles", String.valueOf(request.getHeader("X-User-Roles"))
        ));

        try {
            OpaDecision decision = opaClient.post()
                    .uri("/v1/data/gateway/authz/allow")
                    .body(body)
                    .retrieve()
                    .body(OpaDecision.class);

            if (decision != null && Boolean.TRUE.equals(decision.result())) {
                chain.doFilter(request, response);
            } else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }
        } catch (Exception e) {
            // fail closed if OPA is unreachable
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }
}