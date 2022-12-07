package kim.ku.redis.ratelimit.controller;

import static org.assertj.core.api.Assertions.assertThat;

import kim.ku.redis.config.EmbeddedRedisConfig;
import kim.ku.redis.ratelimit.service.RateLimitService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DisplayName("RateLimitControllerTest 클래스")
@SpringBootTest
@Import({EmbeddedRedisConfig.class})
@ActiveProfiles("embedded")
class RateLimitControllerTest {

	@Autowired
	RateLimitService rateLimitService;

	@Test
	void rateLimit_정상인경우() {
		String clientIp = "192.168.0.11";
		String clientIp2 = "10.12.0.53";

		for (int i = 0; i < 29; i++) {
			rateLimitService.isAllowed(clientIp);
			rateLimitService.isAllowed(clientIp2);
		}

		assertThat(rateLimitService.isAllowed(clientIp)).isTrue();
	}

	@Test
	void rateLimit_범위넘은경우() {
		String clientIp = "192.168.0.11";

		for (int i = 0; i < 30; i++) {
			rateLimitService.isAllowed(clientIp);
		}

		assertThat(rateLimitService.isAllowed(clientIp)).isFalse();
	}
}
