package projectsem4.medcare_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MedcareServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MedcareServerApplication.class, args);
	}
}
