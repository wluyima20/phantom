/*
 * Add Copyright
 */
package com.amiyul.phantom.driver;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import lombok.Getter;
import lombok.Setter;

/**
 * Base class for delayed requests
 * 
 * @see DelayedRequest
 */
public abstract class BaseDelayedRequest<T, R> implements DelayedRequest<T, R> {
	
	@Getter
	private T requestData;
	
	private LocalDateTime startTime;
	
	@Getter
	@Setter
	private R result;
	
	public BaseDelayedRequest(T requestData, LocalDateTime startTime) {
		this.requestData = requestData;
		this.startTime = startTime;
	}
	
	@Override
	public long getDelay(TimeUnit unit) {
		return Duration.between(startTime, LocalDateTime.now()).toMillis();
	}
	
	@Override
	public int compareTo(Delayed delayed) {
		LocalDateTime otherStartTime = ((BaseDelayedRequest) delayed).startTime;
		return this.startTime.compareTo(otherStartTime);
	}
	
	@Override
	public String toString() {
		return "{" + "requestData=" + requestData + ", startTime=" + startTime + "}";
	}
	
}
