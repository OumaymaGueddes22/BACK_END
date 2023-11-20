package com.example.demowebsocket.auth;

import com.example.demowebsocket.Service.StorageService;
import com.example.demowebsocket.config.JwtService;
import com.example.demowebsocket.conversation.Conversation;
import com.example.demowebsocket.conversation.ConversationRep;
import com.example.demowebsocket.token.Token;
import com.example.demowebsocket.token.TokenRepository;
import com.example.demowebsocket.token.TokenType;
import com.example.demowebsocket.user.User;
import com.example.demowebsocket.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;


@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final ConversationRep conversationRep;
    private final PasswordEncoder passwordEncoder;
    private final StorageService storage;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request, MultipartFile file) {
        if (file != null) {
            String filename = storage.CreateNameCv(file);
            storage.store(file, filename);
            request.setImage(filename);
        }

        if (repository.existsByPhoneNumber(request.getPhoneNumber())) {

            throw new RuntimeException("Le numéro de téléphone est déjà utilisé.");
        }
        var user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .phoneNumber(request.getPhoneNumber())
                .image(request.getImage()) // Assuming the image is set in the request
                .build();

        user.setConversation(new ArrayList<>());

        var savedUser = repository.save(user);
     /*   Conversation existingPayementConversation = conversationRep.findConversationByTypeConv("payment");

        if (existingPayementConversation != null) {
            existingPayementConversation.getUser().add(user);
            conversationRep.save(existingPayementConversation);
            savedUser.getConversation().add(existingPayementConversation);
        }else{*/
            var paymentConversation = Conversation.builder()
                    .isgroup(false)
                    .typeConv("payment")
                    .user(Collections.singletonList(savedUser))
                    .messages(new ArrayList<>())
                    .build();
            conversationRep.save(paymentConversation);
            savedUser.getConversation().add(paymentConversation);

      /*  Conversation existingReclamationConversation = conversationRep.findConversationByTypeConv("reclamation");

        if (existingReclamationConversation != null) {
            // If it exists, add the user to the existing conversation
            existingReclamationConversation.getUser().add(user);
            conversationRep.save(existingReclamationConversation);
            savedUser.getConversation().add(existingReclamationConversation);
        }else{*/

            var reclamationConversation = Conversation.builder()
                    .isgroup(false)
                    .typeConv("reclamation")
                    .user(Collections.singletonList(savedUser))
                    .messages(new ArrayList<>())
                    .build();

            conversationRep.save(reclamationConversation);
            savedUser.getConversation().add(reclamationConversation);

        repository.save(savedUser);

        var jwtToken = jwtService.generateToken(savedUser);
        var refreshToken = jwtService.generateRefreshToken(savedUser);

        saveUserToken(savedUser, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .build();
    }




    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getPhoneNumber(), // Utilisez le numéro de téléphone au lieu de l'e-mail
                        request.getPassword()
                )
        );
        var user = repository.findByPhoneNumber(request.getPhoneNumber()) // Utilisez findByPhoneNumber au lieu de findByEmail
                .orElseThrow(() -> new UsernameNotFoundException("User not found with phone number: " + request.getPhoneNumber()));
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .id(user.getId())
                .image(user.getImage())
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .build();
    }


    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.repository.findByPhoneNumber(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }

}

    public CheckTokenResponse checkToken(String token) {
        CheckTokenResponse response = new CheckTokenResponse();
        response.setValid(false);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        /*if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            String username = getUsernameFromToken(token);
            response.setValid(username.equals(currentUserName) && !isTokenExpired(token));
        }*/
        response.setValid(!jwtService.isTokenExpired(token));
        return response;
    }


}
