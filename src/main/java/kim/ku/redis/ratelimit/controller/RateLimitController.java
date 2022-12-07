package kim.ku.redis.ratelimit.controller;

import javax.servlet.http.HttpServletRequest;
import kim.ku.redis.ratelimit.service.RateLimitService;
import kim.ku.redis.ratelimit.util.ClientUtiles;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/rate-limit")
public class RateLimitController {

	private final RateLimitService rateLimitService;

	@GetMapping
	public ResponseEntity<?> rateLimit(HttpServletRequest request) {
		String clientIp = ClientUtiles.getIp(request);

		if (rateLimitService.isAllowed(clientIp)) {
			return ResponseEntity.ok("success");
		} else {
			return ResponseEntity.status(429).body("too many request");
		}
	}
}
