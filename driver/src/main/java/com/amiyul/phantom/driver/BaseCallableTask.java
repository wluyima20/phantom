/*
 * Copyright [yyyy] Amiyul LLC
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.amiyul.phantom.driver;

import java.util.concurrent.Callable;

/**
 * Base class for {@link Callable} tasks
 */
public abstract class BaseCallableTask<T> extends BaseTask implements Callable<T> {
	
	public BaseCallableTask(String name) {
		super(name);
	}
	
	@Override
	public T call() throws Exception {
		String originalThreadName = Thread.currentThread().getName();
		
		try {
			Thread.currentThread().setName(originalThreadName + ":" + getName());
			return doCall();
		}
		finally {
			Thread.currentThread().setName(originalThreadName);
		}
	}
	
	/**
	 * Gets the result
	 *
	 * @return T the result
	 * @throws Exception
	 */
	abstract T doCall() throws Exception;
}
