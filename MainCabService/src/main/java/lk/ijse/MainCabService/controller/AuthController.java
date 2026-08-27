package lk.ijse.MainCabService.controller;

import lk.ijse.MainCabService.dto.AuthRequestDTO;
import lk.ijse.MainCabService.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequestDTO authRequestDTO) {
        String token = authService.authenticate(authRequestDTO);
        return ResponseEntity.ok(token);
    }
}
