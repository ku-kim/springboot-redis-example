package kim.ku.redis.transaction;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionService {

	private final StringRedisTemplate stringRedisTemplate;
	private final RedisTemplate redisTemplate;

	@Transactional
	public void stringRedisTemplateCheckRollback() {
		stringRedisTemplate.opsForValue().set("key11", "value11");
		stringRedisTemplate.opsForValue().set("key22", "value22");
		System.out.println(stringRedisTemplate.opsForValue().get("key11")); // 레디스는 트랜잭션에서 null 나오는 것 확인용

		if (true) {
			throw new RuntimeException("예외 발생");
		}
	}

	@Transactional
	public void redisTemplateCheckRollback() {
		redisTemplate.opsForValue().set("key11", "value11");
		redisTemplate.opsForValue().set("key22", "value22");
		System.out.println(stringRedisTemplate.opsForValue().get("key11"));

		if (true) {
			throw new RuntimeException("예외 발생");
		}
	}

}
