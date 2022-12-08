package kim.ku.redis.connection.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash("point")
public class Point implements Serializable {

	@Id
	private final String id;
	private final Long amount;
	private final LocalDateTime refreshTime;

	public Point(String id, Long amount, LocalDateTime refreshTime) {
		this.id = id;
		this.amount = amount;
		this.refreshTime = refreshTime;
	}

}
