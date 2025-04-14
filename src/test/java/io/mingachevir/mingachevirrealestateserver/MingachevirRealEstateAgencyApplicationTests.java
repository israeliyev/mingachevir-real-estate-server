package io.mingachevir.mingachevirrealestateserver;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = MingachevirRealEstateAgencyApplication.class, properties = {
		"spring.flyway.enabled=false"
})
class MingachevirRealEstateAgencyApplicationTests {

	@Test
	void contextLoads() {
	}

}
