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
				threadPoolExecutor = new ThreadPoolExecutor(
						corePoolSize,//核心线程数
						maximumPoolSize,//最大线程数
						keepAliveTime,//存活时间
						TimeUnit.MILLISECONDS,//存活时间单位
						new LinkedBlockingQueue<Runnable>(),//任务的排队的队列
						Executors.defaultThreadFactory(),//创建线程的工厂
						new AbortPolicy()//线程池处理不了任务的时候,异常的处理方式
				);
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
