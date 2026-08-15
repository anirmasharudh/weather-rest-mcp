package com.anirudh.payments.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.util.*;

public class HeaderInjectingRequestWrapper extends HttpServletRequestWrapper {

    private final Map<String, String> extraHeaders = new HashMap<>();

    public HeaderInjectingRequestWrapper(HttpServletRequest request, String userId, String roles) {
        super(request);
        extraHeaders.put("X-User-Id", userId);
        extraHeaders.put("X-User-Roles", roles);
    }

    @Override
    public String getHeader(String name) {
        String value = extraHeaders.get(name);
        return value != null ? value : super.getHeader(name);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        String value = extraHeaders.get(name);
        return value != null ? Collections.enumeration(List.of(value)) : super.getHeaders(name);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        var names = new java.util.HashSet<String>(extraHeaders.keySet());
        Collections.list(super.getHeaderNames()).forEach(names::add);
        return Collections.enumeration(names);
    }
}