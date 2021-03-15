package org.phoenix.redis;

import java.io.IOException;

public class RedisClientTest {

	public static void main(String[] args) {
		
		RedisClient c = new RedisClient("localhost", 6379);
		try {
			c.setString("X", "1");
			System.out.println(c.getString("X"));
			System.out.println(c.deleteString("X"));
			System.out.println(c.deleteString("Y"));
			System.out.println(c.getString("X"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
