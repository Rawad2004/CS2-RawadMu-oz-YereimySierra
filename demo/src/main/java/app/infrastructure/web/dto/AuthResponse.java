package app.infrastructure.web.dto;

public record AuthResponse(String token, String role, String fullName) {}
