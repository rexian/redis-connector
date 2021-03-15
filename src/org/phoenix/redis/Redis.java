package org.phoenix.redis;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author surajitpaul
 *
 */
public class Redis {

	private final MessageWriter writer;
	private final MessageReader reader;

	public Redis(Socket socket) throws IOException {
		this(socket, 1 << 16, 1 << 16);
	}

	public Redis(Socket socket, int inputBufferSize, int outputBufferSize) throws IOException {
		this(
			new BufferedInputStream(socket.getInputStream(), inputBufferSize),
			new BufferedOutputStream(socket.getOutputStream(), outputBufferSize)
		);
	}

	public Redis(InputStream inputStream, OutputStream outputStream) {
		this.reader = new MessageReader(inputStream);
		this.writer = new MessageWriter(outputStream);
	}

	public <T> T call(Object... args) throws IOException {
		writer.write(Arrays.asList((Object[]) args));
		writer.flush();
		return read();
	}

	@SuppressWarnings("unchecked")
	public <T> T read() throws IOException {
		return (T) reader.parse();
	}

	public Pipeline pipeline() {
		return new Pipeline() {
			private int n = 0;

			public Pipeline call(String... args) throws IOException {
				writer.write(Arrays.asList((Object[]) args));
				writer.flush();
				n++;
				return this;
			}

			public List<Object> read() throws IOException {
				List<Object> ret = new LinkedList<>();
				while (n-- > 0) {
					ret.add(reader.parse());
				}
				return ret;
			}
		};
	}

	public static void run(FailableConsumer<Redis, IOException> callback, String addr, int port) throws IOException {
		try (RedisConnection redis = connect(addr, port)) {
			callback.accept(redis);
		}
	}

	public static void run(FailableConsumer<Redis, IOException> callback, Socket s) throws IOException {
		callback.accept(new Redis(s));
	}

	public static RedisConnection connect(String host, int port) throws IOException {
		Socket s = new Socket(host, port);
		return new RedisConnection(s) {
			public void close() throws IOException {
				call("QUIT");
				s.close();
			}
		};
	}
}