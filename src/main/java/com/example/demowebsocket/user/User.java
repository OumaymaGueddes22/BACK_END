package com.example.demowebsocket.user;

import com.example.demowebsocket.conversation.Conversation;
import com.example.demowebsocket.mesg.ChatMessage;
import com.example.demowebsocket.token.Token;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User implements UserDetails {

    @Id
    private String id;
    private String firstname;
    private String lastname;

    private String phoneNumber;
    private String email;
    private String password;

    public String getFirstname() {

        return this.firstname;
    }

    @DBRef
    private List<Conversation> conversation = new ArrayList<>();

    @DBRef
    private List<ChatMessage> chatMessages = new ArrayList<>();

    private String role;
    private String resetCode;

    private byte[] image;

    private List<Token> tokens;

    public List<ChatMessage> getChatMessages() {
        return chatMessages;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
