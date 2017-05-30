package com.smsserver.auth.users;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import net.jodah.expiringmap.ExpiringMap;

public class AuthenticatedUsers {

	static Map<String, User>  authUsers;

	static {
		authUsers = ExpiringMap.builder()
				  .maxSize(123)
				  .expiration(2, TimeUnit.DAYS)
				  .build();
		put(new User("it123853","STAFF"));
	}

	public static void put(User u) {
		authUsers.putIfAbsent(u.username, u);
	}

	public static User get(String username) {
		return authUsers.get(username);
	}

//	public static <K, V> LinkedHashMap<K, V> createLRUMap(final int maxEntries) {
//		return new LinkedHashMap<K, V>(maxEntries * 10 / 7, 0.7f, true) {
//			@Override
//			protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
//				return size() > maxEntries;
//			}
//		};
//	} wraios kwdikas gia hashmap p ligei

}
