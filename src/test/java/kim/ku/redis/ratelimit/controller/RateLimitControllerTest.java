package kim.ku.redis.ratelimit.controller;

import static org.assertj.core.api.Assertions.assertThat;

import kim.ku.redis.config.EmbeddedRedisConfig;
import kim.ku.redis.ratelimit.service.RateLimitService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ActiveProfiles;

/*
 	Rate Limit 기능 구현에 @Transcational 어노테이션이 추가되어 현재 테스트 코드에서 가상의 클라이언트가 n명이 api 요청하는 것처럼 작동하지 않음
 */
@DisplayName("RateLimitControllerTest 클래스")
@SpringBootTest
@Import({EmbeddedRedisConfig.class})
@ActiveProfiles("embedded")
class RateLimitControllerTest {

	@Autowired
	RateLimitService rateLimitService;

	@Autowired
	StringRedisTemplate stringRedisTemplate;

	@AfterEach
	void setUp() {
		stringRedisTemplate.execute(new RedisCallback() {
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				connection.flushAll();
				return null;
			}
		});
	}
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

//	@Test
//	void rateLimit_범위넘은경우() {
//		String clientIp = "192.168.0.15";
//
//		for (int i = 0; i < 31; i++) {
//			rateLimitService.isAllowed(clientIp);
//		}
//
//		assertThat(rateLimitService.isAllowed(clientIp)).isFalse();
//	}
}
