package dev.gusales.server.controllers;

import dev.gusales.server.controllers.dto.LoginRequest;
import dev.gusales.server.controllers.dto.LoginResponse;
import dev.gusales.server.repositories.PlayerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
public class TokenController {
    private final JwtEncoder jwtEncoder;
    private final PlayerRepository playerRepository;
    private BCryptPasswordEncoder passwordEncoder;

    public TokenController(
            JwtEncoder jwtEncoder,
            PlayerRepository playerRepository,
            BCryptPasswordEncoder passwordEncoder
            ) {
        this.jwtEncoder = jwtEncoder;
        this.playerRepository = playerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){
        var player = playerRepository.findByNickname(loginRequest.nickname());
        // Verificando se o player existe dentro do banco de dados
        if (player.isEmpty()) throw new BadCredentialsException("Invalid credentials");

        // Verificando se as senhas são iguais ou diferentes
        if (!player.get().isPasswordMatches(loginRequest, passwordEncoder)) throw new BadCredentialsException("Invalid credentials");

        var expiresIn = (60 * 60 * 24 * 30); // 30 Days
        var now = Instant.now();

        var claims = JwtClaimsSet
                .builder()
                .issuer("LoginSystemWithFlutterAndJavaSpringBoot")
                .subject(player.get().getPlayerId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return ResponseEntity.ok(new LoginResponse(jwtValue, expiresIn));
    }
}
