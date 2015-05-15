package com.dealmoon.presstest.app;

import org.quartz.Job;
import org.quartz.JobExecutionContext;

import com.dealmoon.presstest.utils.ThreadPool;

/**
 * @author: WY
 * @data:2015年5月4日 上午11:24:17
 * @description: 定义作业
 */
public class MyJob implements Job {

	@Override
	public void execute(JobExecutionContext context) {
		// 取得线程池对象
		ThreadPool threadPool = ThreadPool.getInstance();
		//每次执行该方法，会创建20个任务放到线程池
		for (int i = 0; i < 20; i++) {
			threadPool.addTask(new MyTask());
		}
	}
}
