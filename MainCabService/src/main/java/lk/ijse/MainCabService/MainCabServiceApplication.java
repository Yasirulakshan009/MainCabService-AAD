package lk.ijse.MainCabService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MainCabServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MainCabServiceApplication.class, args);
	}

}
