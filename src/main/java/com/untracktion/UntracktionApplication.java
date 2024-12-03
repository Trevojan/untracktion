package com.untracktion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.untracktion.config.FirebaseInitializer;

@SpringBootApplication
public class UntracktionApplication {

	public static void main(String[] args) {
		FirebaseInitializer.InitializeFirebase();
		SpringApplication.run(UntracktionApplication.class, args);
	}
}
