package kim.ku.redis.connectiontest.domain;

import org.springframework.data.repository.CrudRepository;

public interface PersonRedisRepository extends CrudRepository<Person, String> {

}

