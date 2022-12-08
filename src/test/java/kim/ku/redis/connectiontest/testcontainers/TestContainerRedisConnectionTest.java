package kim.ku.redis.connectiontest.testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import kim.ku.redis.connection.domain.Point;
import kim.ku.redis.connection.domain.PointRedisRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("testcontainers")
class TestContainerRedisConnectionTest extends TestContainersRedis {

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
