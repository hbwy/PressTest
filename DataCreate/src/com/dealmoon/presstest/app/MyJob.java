package com.dealmoon.presstest.app;

import org.quartz.Job;
import org.quartz.JobExecutionContext;

import com.dealmoon.presstest.utils.ThreadPool;

/**
 * @author: WY
 * @data:2015��5��4�� ����11:24:17
 * @description: ������ҵ
 */
public class MyJob implements Job {

	@Override
	public void execute(JobExecutionContext context) {
		// ȡ���̳߳ض���
		ThreadPool threadPool = ThreadPool.getInstance();
		//ÿ��ִ�и÷������ᴴ��20������ŵ��̳߳�
		for (int i = 0; i < 20; i++) {
			threadPool.addTask(new MyTask());
		}
	}
}
