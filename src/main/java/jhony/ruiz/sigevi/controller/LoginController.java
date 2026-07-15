package jhony.ruiz.sigevi.controller;

import jhony.ruiz.sigevi.security.JwtRequest;
import jhony.ruiz.sigevi.security.JwtResponse;
import jhony.ruiz.sigevi.security.JwtUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class LoginController {

    private final JwtUserDetailsService jwtUserDetailsService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest req) throws Exception {
        return ResponseEntity.ok(jwtUserDetailsService.login(req));
    }
}