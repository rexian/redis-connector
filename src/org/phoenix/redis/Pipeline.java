package org.phoenix.redis;

import java.io.IOException;
import java.util.List;

/**
 * 
 * @author surajitpaul
 *
 */
public abstract class Pipeline {

	public abstract Pipeline call(String... args) throws IOException;
	public abstract List<Object> read() throws IOException;
	
}
