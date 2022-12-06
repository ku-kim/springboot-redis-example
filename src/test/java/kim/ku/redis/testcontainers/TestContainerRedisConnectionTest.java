package kim.ku.redis.testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import kim.ku.redis.config.RedisRepositoryConfig;
import kim.ku.redis.domain.Point;
import kim.ku.redis.domain.PointRedisRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@Import(RedisRepositoryConfig.class)
@ActiveProfiles("testcontainers")
class TestContainerRedisConnectionTest extends TestContainersRedisTest{

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
