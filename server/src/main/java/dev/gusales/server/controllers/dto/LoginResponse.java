package dev.gusales.server.controllers.dto;

public record LoginResponse(String acessToken, int expiresIn) {
}
