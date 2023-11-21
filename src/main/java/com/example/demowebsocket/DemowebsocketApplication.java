package com.example.demowebsocket;

import com.example.demowebsocket.auth.AuthenticationService;
import com.example.demowebsocket.auth.RegisterRequest;
import com.example.demowebsocket.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.CrossOrigin;


@CrossOrigin("*")
@SpringBootApplication
public class DemowebsocketApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemowebsocketApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(AuthenticationService service, UserRepository userRepository) {
		return args -> {
			if (!userRepository.existsByPhoneNumber("admin")) {
				var admin = RegisterRequest.builder()
						.fullName("Admin")
						.email("admin@mail.com")
						.password("admin")
						.phoneNumber("admin")
						.role("ADMIN")

						.build();

				System.out.println("Admin token: " + service.register(admin, null).getAccessToken());
			} else {
				System.out.println("Admin user exists");
			}
		};
	}

}


