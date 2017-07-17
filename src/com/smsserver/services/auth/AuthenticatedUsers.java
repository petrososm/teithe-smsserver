package com.smsserver.services.auth;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;

import net.jodah.expiringmap.ExpiringMap;

@Singleton
public class AuthenticatedUsers {

	private Map<String, User>  authUsers;

	@PostConstruct
	public void init(){
		authUsers = ExpiringMap.builder()
				  .maxSize(300)
				  .expiration(2, TimeUnit.DAYS)
				  .build();
		put(new User("it123853","zz","STUD"));
		put(new User("peris","doesntmatter","STAFF"));
	}

	public  void put(User u) {
		authUsers.putIfAbsent(u.getUsername(), u);
	}

	public User get(String username) {
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
