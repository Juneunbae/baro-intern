package com.sparta.barointern.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import com.sparta.barointern.entity.User;

@Repository
public class UserRepository {
	private static final Map<Long, User> userTable = new HashMap<>();
	private static final AtomicLong id = new AtomicLong(0);

	public User save(User user) {
		if (user.getId() == null) {
			user.setId(id.incrementAndGet());
		}

		userTable.put(user.getId(), user);
		return user;
	}

	public Optional<User> findById(Long id) {
		return Optional.ofNullable(userTable.get(id));
	}

	public Optional<User> findByUsername(String username) {
		return userTable.values().stream()
			.filter(user -> user.getUsername().equals(username))
			.findFirst();
	}

	public void clearUserTable() {
		userTable.clear();
	}
}