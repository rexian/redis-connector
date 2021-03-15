package org.phoenix.redis;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author surajitpaul
 *
 */
public class MessageReader {

	private final InputStream input;

	public MessageReader(InputStream input) {
		this.input = input;
	}

	public Object parse() throws IOException, ConnectionError {
		Object ret;
		int read = this.input.read();
		switch (read) {
			case '+':
				ret = this.parseSimpleString();
				break;
			case '-':
				throw new ServerError(new String(this.parseSimpleString()));
			case ':':
				ret = this.parseNumber();
				break;
			case '$':
				ret = this.parseBulkString();
				break;
			case '*':
				long len = this.parseNumber();
				if (len == -1) {
					ret = null;
				} else {
					List<Object> arr = new LinkedList<>();
					for (long i = 0; i < len; i++) {
						arr.add(this.parse());
					}
					ret = arr;
				}
				break;
			case -1:
				return null;
			default:
				throw new ConnectionError("Unexpected input: " + (byte) read);
		}

		return ret;
	}

	private byte[] parseBulkString() throws IOException {
		final long expectedLength = parseNumber();
		if (expectedLength == -1) {
			return null;
		}
		if (expectedLength > Integer.MAX_VALUE) {
			throw new ConnectionError("Unsupported value length for bulk string");
		}
		final int numBytes = (int) expectedLength;
		final byte[] buffer = new byte[numBytes];
		int read = 0;
		while (read < expectedLength) {
			read += input.read(buffer, read, numBytes - read);
		}
		if (input.read() != '\r') {
			throw new ConnectionError("Expected CR");
		}
		if (input.read() != '\n') {
			throw new ConnectionError("Expected LF");
		}

		return buffer;
	}

	private byte[] parseSimpleString() throws IOException {
		return scanCr();
	}

	private long parseNumber() throws IOException {
		return Long.parseLong(new String(scanCr()));
	}

	private byte[] scanCr() throws IOException {
		int size = 1024;
		int idx = 0;
		int ch;
		byte[] buffer = new byte[size];
		while ((ch = input.read()) != '\r') {
			buffer[idx++] = (byte) ch;
			if (idx == size) {
				// increase buffer size.
				size *= 2;
				buffer = java.util.Arrays.copyOf(buffer, size);
			}
		}
		if (input.read() != '\n') {
			throw new ConnectionError("Expected LF");
		}

		return Arrays.copyOfRange(buffer, 0, idx);
	}
}