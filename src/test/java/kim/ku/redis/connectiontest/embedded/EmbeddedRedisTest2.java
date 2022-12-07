package kim.ku.redis.connectiontest.embedded;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import kim.ku.redis.config.EmbeddedRedisConfig;
import kim.ku.redis.connectiontest.domain.Point;
import kim.ku.redis.connectiontest.domain.PointRedisRepository;
import kim.ku.redis.config.RedisRepositoryConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Import({EmbeddedRedisConfig.class, RedisRepositoryConfig.class})
@ActiveProfiles("embedded")
class EmbeddedRedisTest2 {

	@Autowired
	private PointRedisRepository pointRedisRepository;

	@AfterEach
	void tearDown() {
		pointRedisRepository.deleteAll();
	}

	@Test
	void 기본_조회() {
		pointRedisRepository.save(new Point("1", 100L, LocalDateTime.now()));

		Point point = pointRedisRepository.findById("1").get();

		assertThat(point.getAmount()).isEqualTo(100L);
	}
}
