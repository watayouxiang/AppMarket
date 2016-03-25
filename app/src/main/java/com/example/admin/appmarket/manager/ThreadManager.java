package com.example.admin.appmarket.manager;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.TimeUnit;

/**
 * 自定义线程池管理类
 */
public class ThreadManager {

	private static ThreadProxyPool threadProxyPool;
	private static Object object = new Object();

	public static ThreadProxyPool getThreadProxyPool() {
		synchronized (object) {
			if (threadProxyPool == null) {
				threadProxyPool = new ThreadProxyPool(10, 10, 5L);
			}
			return threadProxyPool;
		}
	}

	public static class ThreadProxyPool {
		private ThreadPoolExecutor threadPoolExecutor;
		private int corePoolSize;
		private int maximumPoolSize;
		private long keepAliveTime;

		public ThreadProxyPool(int corePoolSize, int maximumPoolSize, long keepAliveTime) {
			this.corePoolSize = corePoolSize;
			this.maximumPoolSize = maximumPoolSize;
			this.keepAliveTime = keepAliveTime;
		}

		// 将线程对象加入线程池
		public void execute(Runnable runnable) {
			if (runnable == null) {
				return;
			}
			if (threadPoolExecutor == null || threadPoolExecutor.isShutdown()) {
				threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime,
						TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), Executors.defaultThreadFactory(),
						new AbortPolicy());
			}

			threadPoolExecutor.execute(runnable);
		}

		// 将线程对象从线程池中移除
		public void cancel(Runnable runnable) {
			if (runnable != null && !threadPoolExecutor.isShutdown()) {
				threadPoolExecutor.getQueue().remove(runnable);
			}
		}
	}

}
