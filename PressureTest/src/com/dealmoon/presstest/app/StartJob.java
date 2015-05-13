package com.dealmoon.presstest.app;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.quartz.DateBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SchedulerMetaData;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;

import com.dealmoon.presstest.utils.PropertiesReader;

public class StartJob {

	private static Map<String, String> config = PropertiesReader.getConfig();

	public void run() throws Exception {
		System.out.println("------------- 初始化 获得 Scheduler 对象 -------------");

		// 获得 Scheduler 对象   
		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler scheduler = sf.getScheduler();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

		long currentTime = System.currentTimeMillis();
		long runTime = Integer.parseInt(config.get("runTime"));
		Date startTime = new Date(currentTime + 5000L);//指定5秒钟以后开始
		Date endTime = new Date(currentTime + runTime + 5000L); //runTime毫秒数+5秒以后结束

		// 定义一个 job 对象并绑定我们写的  MyJob 类   
		// 真正执行的任务并不是Job接口的实例，而是用反射的方式实例化的一个JobDetail实例    
		JobDetail job = newJob(MyJob.class).withIdentity("job1", "group1").build();

		System.out.println("开始时间: " + sdf.format(startTime) + ",结束时间: " + sdf.format(endTime));

		// 定义一个触发器，startAt方法定义了任务应当开始的时间 .即下一个整数分钟执行  
		SimpleTrigger trigger = (SimpleTrigger) newTrigger().withIdentity("trigger1", "group1").startAt(startTime)
				.endAt(endTime).withSchedule(simpleSchedule().withIntervalInSeconds(5).repeatForever()).build();

		// 注册并进行调度    
		Date ft = scheduler.scheduleJob(job, trigger);

		// 启动  
		scheduler.start();
		System.out.println(" ----------------- 任务调度已经启动 -----------------");

		try {
			//总时间加上 10秒
			Thread.sleep(currentTime + runTime + 10000L);

		} catch (Exception e) {
		}

		//调度器停止  
		scheduler.shutdown(true);
		System.out.println(" ----------------- 任务调度结束 -----------------");

		SchedulerMetaData metaData = scheduler.getMetaData();
		System.out.println("Executed " + metaData.getNumberOfJobsExecuted() + " jobs.");
	}

	public static void main(String[] args){
		StartJob start = new StartJob();
		try {
			start.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
