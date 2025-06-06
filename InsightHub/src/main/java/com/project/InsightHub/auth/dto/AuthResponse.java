package com.project.InsightHub.auth.dto;

public record AuthResponse(String token, String name, String email, boolean googleUser) {}