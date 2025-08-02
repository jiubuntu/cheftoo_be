package jwhs.cheftoo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CheftooApplication {

	public static void main(String[] args) {
		SpringApplication.run(CheftooApplication.class, args);
	}

}
