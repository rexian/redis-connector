package org.phoenix.redis;

import java.io.IOException;

public class ServerError extends IOException {

	private static final long serialVersionUID = 1L;

	ServerError(String msg) {
		super(msg);
	}
	
	ServerError(Throwable cause) {
		super(cause);
	}
	
	ServerError(String msg, Throwable cause) {
		super(msg, cause);
	}
}
