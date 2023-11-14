package com.example.demowebsocket.auth;

import com.example.demowebsocket.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;
    private final SimpMessagingTemplate messagingTemplate;


    @PostMapping("/register")
    public ResponseEntity<Pair<AuthenticationResponse, User>> register(@ModelAttribute RegisterRequest request) throws IOException {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }


    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        service.refreshToken(request, response);
    }

    @PostMapping("/check-token")
    public CheckTokenResponse checkToken(@RequestBody CheckTokenRequest tokenRequest) {
        String token = tokenRequest.getToken();
        return service.checkToken(token);
    }


}
