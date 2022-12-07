package kim.ku.redis.connectiontest.domain;

import org.springframework.data.repository.CrudRepository;

public interface PointRedisRepository extends CrudRepository<Point, String> {

}
