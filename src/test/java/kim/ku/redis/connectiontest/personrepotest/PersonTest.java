package kim.ku.redis.connectiontest.personrepotest;

import static org.assertj.core.api.Assertions.assertThat;

import kim.ku.redis.connection.domain.Person;
import kim.ku.redis.connection.domain.PersonRedisRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PersonTest {

	@Autowired
	private PersonRedisRepository personRedisRepository;

	@Test
	void redisRepo작동테스트() {
		Person person = new Person("name", "email", 10, null);
		personRedisRepository.save(person);

		Person findPerson = personRedisRepository.findById(person.getId()).get();

		assertThat(findPerson.getAge()).isEqualTo(10);
	}

}
