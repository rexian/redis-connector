package org.phoenix.redis;

import java.io.IOException;
import java.net.Socket;

public abstract class RedisConnection extends Redis implements AutoCloseable {

	public RedisConnection(Socket socket) throws IOException {
		super(socket);
	}

	public abstract void close() throws IOException;

}
