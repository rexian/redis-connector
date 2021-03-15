package org.phoenix.redis;

import java.io.IOException;

/**
 * 
 * @author surajitpaul
 *
 */
public class ConnectionError extends IOException {

	private static final long serialVersionUID = 1L;
	
	ConnectionError(String msg) {
		super(msg);
	}
	ConnectionError(Throwable cause) {
		super(cause);
	}
	ConnectionError(String msg, Throwable cause) {
		super(msg, cause);
	}
}
