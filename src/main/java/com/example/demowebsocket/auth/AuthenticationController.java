package com.example.demowebsocket.auth;

import com.example.demowebsocket.Service.StorageService;
import com.example.demowebsocket.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;
    private final StorageService storage;
    private final SimpMessagingTemplate messagingTemplate;



    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@ModelAttribute RegisterRequest request) throws IOException {
        Optional<MultipartFile> file = request.getFile();
        if (file.isPresent()) {
            String filename = storage.CreateNameCv(file.get());
            storage.store(file.get(), filename);
            request.setImage(filename);
        }

        return ResponseEntity.ok(service.register(request));
    }


    @GetMapping("/users/files/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = storage.loadFile(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
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
