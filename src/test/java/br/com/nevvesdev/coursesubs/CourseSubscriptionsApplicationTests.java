package br.com.nevvesdev.coursesubs;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class CourseSubscriptionsApplicationTests {

	@Test
	void contextLoads() {
	}
}