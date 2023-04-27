package example.end_course;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class EndCourseApplication {

	public static void main(String[] args) {
		SpringApplication.run(EndCourseApplication.class, args);
	}

}
