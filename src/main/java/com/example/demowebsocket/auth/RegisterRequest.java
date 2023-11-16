package com.example.demowebsocket.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

  private String fullName;
 // private String lastname;
  private String email;
  private String password;
  private String phoneNumber;
  private String role;
  private String image;
  private Optional<MultipartFile> file;

  public Optional<MultipartFile> getFile() {
     return file;
  }

  public void setFile(Optional<MultipartFile> file) {
     this.file = file;
  }

}
