package org.phoenix.redis;

public interface FailableConsumer<T, E extends Throwable> {

	void accept(T t) throws E;
	
}
