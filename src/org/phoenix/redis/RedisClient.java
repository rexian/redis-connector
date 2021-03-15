package org.phoenix.redis;

import java.io.IOException;
import java.util.Random;

/**
 * 
 * @author surajitpaul
 *
 */
public class RedisClient {
	
	private String host;
	private int port;
	private static String res = null;
	private static Long deleteStatus = 0L;
	
	
	public RedisClient(String host, int port) {
		super();
		this.host = host;
		this.port = port;
	}

	public void setString(String key, String value) throws IOException {
		
		byte[] bytes = new byte[1024];
		new Random().nextBytes(bytes);
		Redis.run((redis) -> {
			redis.<byte[]>call("SET", key, value);
		}, host, port);
	}
	
	public String getString(String key) throws IOException {
		
		byte[] bytes = new byte[1024];
		new Random().nextBytes(bytes);
		Redis.run((redis) -> {
			res = redis.<byte[]>call("GET", key) == null? null: new String(redis.<byte[]>call("GET", key));
		}, host, port);
		return res;
	}
	
	public boolean deleteString(String key) throws IOException {
		
		byte[] bytes = new byte[1024];
		new Random().nextBytes(bytes);
		Redis.run((redis) -> {
			deleteStatus = redis.call("DEL", key);
		}, host, port);
		return deleteStatus == 1? true: false;
	}

}
