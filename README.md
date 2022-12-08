# springboot-redis-example
이 저장소는 springboot에서 redis를 사용하고 테스트하는 예제입니다.  

Java : 17  
Spring Boot : 2.7.5  

---

### docker-compose를 활용하여 redis 사용하기

```bash
# 도커 컴포즈를 활용하여 redis 컨테이너 실행
## 백그라운드 실행 시 -d 옵션 추가
## 레디스 옵션 설정하려면 ./container/conf/redis.conf 를 수정
docker-compose -f ./container/redis.yml up # -d

## 실행된 도커 프로세스 확인
docker ps     
CONTAINER ID   IMAGE         COMMAND                  CREATED         STATUS         PORTS                    NAMES
97213b72da1e   redis:7.0.5   "docker-entrypoint.s…"   6 seconds ago   Up 4 seconds   0.0.0.0:6379->6379/tcp   redis-server

# 레디스 컨테이너가 실행된 뒤 redis-cli를 통해 레디스에 접속
docker exec -it redis-server redis-cli

$127.0.0.1:6379>
```

---

### Testcontainers를 활용하여 redis 사용하기

1. Testcontainers 의존성 추가

```
# build.gradle

dependencies {
	// ...
  
	// TestContainers
	testImplementation 'org.testcontainers:junit-jupiter:1.17.4'
}
```

2. TestContainers에서 권장하는 logback 설정

```
# ./src/test/resources/logback-test.xml 
## ref : https://www.testcontainers.org/supported_docker_environment/logging_config/


<configuration>
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger - %msg%n</pattern>
        </encoder>
    </appender>

    <root level="info">
        <appender-ref ref="STDOUT"/>
    </root>

    <logger name="org.testcontainers" level="INFO"/>
    <logger name="com.github.dockerjava" level="WARN"/>
    <logger name="com.github.dockerjava.zerodep.shaded.org.apache.hc.client5.http.wire" level="OFF"/>
</configuration>
```

3. redis 컨테이너 실행과 확인

```java
// ./test/java/com/example/redis/RedisContainerTest.java
@Testcontainers
class RedisTestContaners {

	@Container
	public GenericContainer redis = new GenericContainer("redis:7.0.5")
			.withExposedPorts(6379);

	@Test
	void testContainers_실행확인() {
		assertThat(redis.getHost()).isEqualTo("localhost");
		assertThat(redis.getExposedPorts()).contains(6379);
	}
}
```

---

### embedded redis를 활용하여 redis & Spring Data Redis 사용하기

1. embedded redis & Spring Data Redis 의존성 추가


```
# build.gradle

dependencies {
	// ...
  
  	// Spring Data Redis
	implementation 'org.springframework.boot:spring-boot-starter-data-redis'
		
	// Embedded Redis DB : it.ozimov
	testImplementation 'it.ozimov:embedded-redis:0.7.3' exclude group: 'org.slf4j', module: 'slf4j-simple'

}
```

2. embedded redis 설정

```java
// src/test/java/kim/ku/redis/config/EmbeddedRedisConfig.java
// Embedded Redis 서버를 설정하고 실행한다. 해당 빈이 끝날 때 제거한다.

@TestConfiguration
public class EmbeddedRedisConfig {

	@Value("${spring.redis.port}")
	private int redisPort;

	private RedisServer redisServer;

	@PostConstruct
	public void redisServer() throws IOException {
		int port = isRedisRunning() ? getRandomPort() : redisPort;
		redisServer = new RedisServer(port);
		redisServer.start();
	}

	@PreDestroy
	public void stopRedis() {
		redisServer.stop();
	}

	// 생략
}
```

```yaml
# src/test/resources/application-embedded.yml

spring:
  redis:
    host: localhost
    port: 6379
```

3. Spring Data Redis 설정

```java 
// src/main/java/kim/ku/redis/config/RedisRepositoryConfig.java
// 클라이언트는 Lettuce를 사용한다.

@TestConfiguration
@EnableRedisRepositories
public class RedisRepositoryConfig {

	@Value("${spring.redis.host}")
	private String redisHost;

	@Value("${spring.redis.port}")
	private int port;

	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		return new LettuceConnectionFactory(redisHost, port);
	}

	@Bean
	public RedisTemplate<?, ?> redisTemplate() {
		RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory());
		return redisTemplate;
	}
}
```

---

[![Hits](https://hits.seeyoufarm.com/api/count/incr/badge.svg?url=https%3A%2F%2Fgithub.com%2Fku-kim%2Fspringboot-redis-example&count_bg=%2379C83D&title_bg=%23555555&icon=&icon_color=%23E7E7E7&title=hits&edge_flat=false)](https://hits.seeyoufarm.com)
