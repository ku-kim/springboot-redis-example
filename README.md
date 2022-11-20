# springboot-redis-example
이 저장소는 springboot에서 redis를 사용하고 테스트하는 예제입니다.  

Java : 17  
Spring Boot : 2.7.5  

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
