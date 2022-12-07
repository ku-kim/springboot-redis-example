package kim.ku.redis.ratelimit.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RateLimitService {

	public static final int LIMIT_MINUTES = 5;
	public static final int API_MAXIMUN_COUNT = 30;
	private final StringRedisTemplate redisTemplate;

	@Transactional
	public boolean isAllowed(String clientIp) {
		long apiCount = getApiCount(clientIp);

		if (apiCount >= API_MAXIMUN_COUNT) {
			return false;
		} else {
			incrementApiCount(clientIp);
			return true;
		}

	}

	private void incrementApiCount(String clientIp) {
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
		redisTemplate.opsForValue().increment(clientIp + ":" + df.format(date));
		redisTemplate.expire(clientIp + ":" + df.format(date), LIMIT_MINUTES, TimeUnit.MINUTES);
	}

	private long getApiCount(String clientIp) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());

		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");

		List<String> keys = new ArrayList<>();
		for (int i = 0; i < LIMIT_MINUTES; i++) {
			keys.add(clientIp + ":" + df.format(cal.getTime()));
			cal.add(Calendar.MINUTE, -1);
		}

		ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
		List<String> values = valueOperations.multiGet(keys);
		long count = 0;
		for (String value : Objects.requireNonNull(values)) {
			if (Objects.nonNull(value)) {
				return count + Integer.parseInt(value);
			}
		}

		return count;
	}
}
