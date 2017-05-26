package com.smsserver.auth.users;

import java.util.LinkedHashMap;
import java.util.Map;

public class AuthenticatedUsers {

	static LinkedHashMap<String, User> authUsers;

	static {
		authUsers = createLRUMap(100);
	}

	public static void put(User u) {
		if (authUsers.containsKey(u.username))
			return;
		authUsers.put(u.username, u);
	}

	public static User get(String username) {
		return authUsers.get(username);
	}

	public static <K, V> LinkedHashMap<K, V> createLRUMap(final int maxEntries) {
		return new LinkedHashMap<K, V>(maxEntries * 10 / 7, 0.7f, true) {
			@Override
			protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
				return size() > maxEntries;
			}
		};
	}

}
