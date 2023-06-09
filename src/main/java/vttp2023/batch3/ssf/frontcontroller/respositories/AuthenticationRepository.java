package vttp2023.batch3.ssf.frontcontroller.respositories;

import java.time.Duration;
import java.util.Optional;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AuthenticationRepository {

	RedisTemplate<String, String> template;

	public AuthenticationRepository(RedisTemplate<String, String> template) {
		this.template = template;
	}

	// TODO Task 5
	// Use this class to implement CRUD operations on Redis
	public void disableUser(String username) {

		// template.opsForValue().set(username, "true", Duration.ofSeconds(30));
		System.out.println("disabling user " + username);
		template.opsForValue().set(username, "true", Duration.ofMinutes(30));
		// System.out.println(template.opsForValue().get(username));
	}

	public boolean isLocked(String username) {

		Optional<String> opt = Optional.ofNullable(template.opsForValue().get(username));

		if (opt.isPresent()) {
			String locked = opt.get();
			if (locked.equals("true"))
				return true;
		}

		return false;
	}

}
