package com.dealmoon.presstest.app;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.quartz.Job;
import org.quartz.JobExecutionContext;

import com.dealmoon.presstest.utils.PropertiesReader;
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
		// 读取配置文件中的接口列表
		Map<String, String> inters = PropertiesReader.getInters();
		List<Object> keys = PropertiesReader.getKeys();
		Random rm = new Random();
		// 扫描一次配置文件N个接口,产生N个请求
		for (int i = 0; i < keys.size(); i++) {
			//随机数从1..sum之间产生
			int number = rm.nextInt(keys.size() + 1);
			//遍历权值列表找到比随机数大的最小值,并把该值对应的接口名字作为请求的接口
			for (int j = 0; j < keys.size(); j++) {
				int key = Integer.parseInt((String) keys.get(j));
				if (number <= key) {
					threadPool.addTask(new MyTask(Pressure.class, inters.get(String.valueOf(key))));
					break;
				}
			}
		}
	}
}
