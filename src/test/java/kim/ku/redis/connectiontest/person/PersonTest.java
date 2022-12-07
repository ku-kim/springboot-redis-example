package kim.ku.redis.connectiontest.person;

import java.util.Optional;
import kim.ku.redis.connectiontest.domain.Person;
import kim.ku.redis.connectiontest.domain.PersonRedisRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@SpringBootTest
class PersonTest {

	@Autowired
	private PersonRedisRepository personRedisRepository;

	@Autowired
	private RedisTemplate redisTemplate;

	@Test
	void name() {
		Person person = new Person("name", "email", 10, null);

		personRedisRepository.save(person);

		long count = personRedisRepository.count();

		Optional<Person> result = personRedisRepository.findById(person.getId());

		System.out.println(result);
	}

	@Test
	void test2() {
		Person test = new Person("id2", "name", "email", 130, null);

		personRedisRepository.save(test);

		Optional<Person> result = personRedisRepository.findById(test.getId());
		System.out.println(result);
	}

	@Test
	void test3() {
		ValueOperations valueOperations = redisTemplate.opsForValue();
		valueOperations.set("stringTestKey", "stringValue");
	}
}
