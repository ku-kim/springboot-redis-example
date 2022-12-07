package kim.ku.redis.connectiontest.domain;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.redis.core.RedisHash;

@Getter
@ToString
@RedisHash(value = "person", timeToLive = 60)
@NoArgsConstructor
public class Person {

	private String id;
	private String name;
	private String email;
	private Integer age;
	private LocalDateTime createdAt;

	public Person(String name, String email, Integer age, LocalDateTime createdAt) {
		this.name = name;
		this.email = email;
		this.age = age;
		this.createdAt = createdAt;
	}

	public Person(String id, String name, String email, Integer age, LocalDateTime createdAt) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.age = age;
		this.createdAt = createdAt;
	}
}
