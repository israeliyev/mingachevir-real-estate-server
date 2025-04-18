package io.mingachevir.mingachevirrealestateserver.controller;

import io.mingachevir.mingachevirrealestateserver.configuration.JwtUtil;
import io.mingachevir.mingachevirrealestateserver.model.entity.UserDevice;
import io.mingachevir.mingachevirrealestateserver.model.request.LoginRequest;
import io.mingachevir.mingachevirrealestateserver.model.response.LoginResponse;
import io.mingachevir.mingachevirrealestateserver.repository.UserDeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDeviceRepository deviceRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            List<UserDevice> devices = deviceRepository.findByUsername(request.getUsername());
            if (devices.size() >= 3 && devices.stream().noneMatch(d -> d.getDeviceId().equals(request.getDeviceId()))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Maximum 3 devices allowed");
            }

            String token = jwtUtil.generateToken(request.getUsername(), request.getDeviceId());

            UserDevice userDevice = devices.stream()
                    .filter(d -> d.getDeviceId().equals(request.getDeviceId()))
                    .findFirst()
                    .orElse(new UserDevice());

            userDevice.setUsername(request.getUsername());
            userDevice.setDeviceId(request.getDeviceId());
            userDevice.setToken(token);
            deviceRepository.save(userDevice);

            return ResponseEntity.ok(Map.of("token", token));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Email və ya şifrə tapılmadı");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Giriş zamanı xəta: " + e.toString());
        }
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            Optional<UserDevice> device = deviceRepository.findByToken(token);
            if (device.isPresent()) {
                deviceRepository.delete(device.get());
                return ResponseEntity.ok(Map.of("Uğurla çıxış edildi", token));
            }
        }
        return ResponseEntity.status(400).body("Token duzgun deyil");
    }
}
