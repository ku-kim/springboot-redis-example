package kim.ku.redis.connectiontest.testcontainers;

import org.testcontainers.containers.GenericContainer;

class TestContainersRedis {

	static {
		GenericContainer redis = new GenericContainer("redis:7.0.5")
			.withExposedPorts(6381);
		redis.start();
		System.setProperty("spring.redis.host", redis.getHost());
		System.setProperty("spring.redis.port", redis.getMappedPort(6381).toString());
	}

}
