package kim.ku.redis.testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
class TestContanersRedisTest {

	@Container
	public GenericContainer redis = new GenericContainer("redis:7.0.5")
			.withExposedPorts(6379);

	@Test
	void testContainers_실행확인() {
		assertThat(redis.getHost()).isEqualTo("localhost");
		assertThat(redis.getExposedPorts()).contains(6379);
	}
}
