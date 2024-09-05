package ro.msg.mobile_clone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MobileCloneApplication {

	public static void main(String[] args) {
		SpringApplication.run(MobileCloneApplication.class, args);
	}

}
