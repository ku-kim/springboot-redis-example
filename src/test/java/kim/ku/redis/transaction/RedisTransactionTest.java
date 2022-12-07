package kim.ku.redis.transaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
class RedisTransactionTest {

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	private TransactionService transactionService;

	@AfterEach
	void setUp() {
		stringRedisTemplate.execute(new RedisCallback() {
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				connection.flushAll();
				return null;
			}
		});
	}

	@Test
	void multi_exec_트랜잭션확인() {

		// given
		stringRedisTemplate.execute(new SessionCallback() {
			@Override
			public Object execute(RedisOperations operations) throws DataAccessException {
				operations.multi(); // 트랜잭션 시작

				operations.opsForValue().set("key1", "value1");
				operations.opsForValue().set("key2", "value2");

				return operations.exec(); // 종료
			}
		});

		String value1 = stringRedisTemplate.opsForValue().get("key1");
		String value2 = stringRedisTemplate.opsForValue().get("key2");

		assertThat(value1).isEqualTo("value1");
		assertThat(value2).isEqualTo("value2");
	}

	@Test
	void multi_exec_명령어_트랜잭션_확인2() {

		// given : 중간에 비정상적으로 실패하면 데이터 업데이트 적용안됨
		assertThatThrownBy(() -> stringRedisTemplate.execute(new SessionCallback() {
			@Override
			public Object execute(RedisOperations operations) throws DataAccessException {
				operations.multi(); // 트랜잭션 시작

				operations.opsForValue().set("key1", "value1");
				operations.opsForValue().set("key2", "value2");

				if (true) {
					throw new RuntimeException("예외 발생");
				}
				return operations.exec(); // 종료
			}
		})).isInstanceOf(RuntimeException.class);

		String value1 = stringRedisTemplate.opsForValue().get("key1");
		String value2 = stringRedisTemplate.opsForValue().get("key2");

		assertThat(value1).isNull();
		assertThat(value2).isNull();
	}

	@Test
	void transaction_어노테이션활용하여_트랜잭션_확인_stringredistemplate() {
		assertThatThrownBy(() -> transactionService.stringRedisTemplateCheckRollback()).isInstanceOf(
			RuntimeException.class);

		String value1 = stringRedisTemplate.opsForValue().get("key11");
		String value2 = stringRedisTemplate.opsForValue().get("key22");

		assertThat(value1).isNull();
		assertThat(value2).isNull();
	}

	@Test
	void transaction_어노테이션활용하여_트랜잭션_확인_redistemplate() {
		assertThatThrownBy(() -> transactionService.redisTemplateCheckRollback()).isInstanceOf(
			RuntimeException.class);

		Object value1 = redisTemplate.opsForValue().get("key11");
		Object value2 = redisTemplate.opsForValue().get("key22");

		assertThat(value1).isNull();
		assertThat(value2).isNull();
	}
}
