package kim.ku.redis.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.util.StringUtils;
import redis.embedded.RedisServer;

@TestConfiguration
public class EmbeddedRedisConfig {

	@Value("${spring.redis.port}")
	private int redisPort;

	private RedisServer redisServer;

	@PostConstruct
	public void redisServer() throws IOException {
		int port = isRedisRunning() ? getRandomPort() : redisPort;
		redisServer = new RedisServer(port);
		redisServer.start();
	}

	@PreDestroy
	public void stopRedis() {
		redisServer.stop();
	}

	private boolean isRedisRunning() throws IOException {
		return isRunning(executeGrepProcessCommand(redisPort));
	}

	/**
	 * 해당 port를 사용중인 프로세스 확인하는 sh 실행
	 */
	private Process executeGrepProcessCommand(int port) throws IOException {
		String command = String.format("netstat -nat | grep LISTEN|grep %d", port);
		String[] shell = {"/bin/sh", "-c", command};
		return Runtime.getRuntime().exec(shell);
	}

	/**
	 * 해당 Process가 현재 실행중인지 확인
	 */
	private boolean isRunning(Process process) {
		String line;
		StringBuilder pidInfo = new StringBuilder();

		try (BufferedReader input = new BufferedReader(
			new InputStreamReader(process.getInputStream()))) {

			while ((line = input.readLine()) != null) {
				pidInfo.append(line);
			}

		} catch (Exception ignored) {
		}

		return StringUtils.hasLength(pidInfo.toString());
	}

	/**
	 * 랜덤 포트를 할당받아서 리턴
	 * ref : <a href="https://github.com/apache/curator/blob/7a148288603ae5db0c232142f7dc07d43e01bea3/curator-test/src/main/java/org/apache/curator/test/InstanceSpec.java#L86">...</a>
	 */
	private int getRandomPort() {
		ServerSocket server = null;
		try {
			server = new ServerSocket(0);
			server.setReuseAddress(true);
			return server.getLocalPort();
		} catch (IOException e) {
			throw new Error(e);
		} finally {
			if (server != null) {
				try {
					server.close();
				} catch (IOException ignore) {
					// ignore
				}
			}
		}
	}
}
