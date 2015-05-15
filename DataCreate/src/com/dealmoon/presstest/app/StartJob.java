package com.dealmoon.presstest.app;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SchedulerMetaData;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;

import com.dealmoon.presstest.utils.PropertiesReader;
import com.dealmoon.presstest.utils.ThreadPool;

public class StartJob {

	private static Map<String, String> config = PropertiesReader.getConfig();

	public void run() throws Exception {
		System.out.println("------------- init Scheduler object -------------");

		// ��� Scheduler ����   
		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler scheduler = sf.getScheduler();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

		long currentTime = System.currentTimeMillis();
		long runTime = Integer.parseInt(config.get("runTime"));
		Date startTime = new Date(currentTime + 5000L);//ָ��5�����Ժ�ʼ
		Date endTime = new Date(currentTime + runTime + 5000L); //runTime������+5���Ժ����

		// ����һ�� job ���󲢰�����д��  MyJob ��   
		// ����ִ�е����񲢲���Job�ӿڵ�ʵ���������÷���ķ�ʽʵ������һ��JobDetailʵ��    
		JobDetail job = newJob(MyJob.class).withIdentity("job1", "group1").build();

		System.out.println("-------------start_time: " + sdf.format(startTime) + ",end_time: " + sdf.format(endTime)+"------------");

		// ����һ����������startAt��������������Ӧ����ʼ��ʱ�� .����һ����������ִ��  
		SimpleTrigger trigger = (SimpleTrigger) newTrigger().withIdentity("trigger1", "group1").startAt(startTime)
				.endAt(endTime).withSchedule(simpleSchedule().withIntervalInSeconds(2).repeatForever()).build();

		// ע�Ტ���е���    
		Date ft = scheduler.scheduleJob(job, trigger);

		// ����  
		scheduler.start();
		System.out.println(" ----------------- job starting -----------------");

		try {
			//endTime 5���ֹͣ����
			Thread.sleep(runTime + 10000L);

		} catch (Exception e) {
		}
		//�����̳߳�
		ThreadPool.getInstance().destroy();
		//������ֹͣ  
		scheduler.shutdown(true);
		System.out.println(" ----------------- job fishing -----------------");

		SchedulerMetaData metaData = scheduler.getMetaData();
		System.out.println("Executed " + metaData.getNumberOfJobsExecuted() + " jobs.");
	}

	public static void main(String[] args) {
		StartJob start = new StartJob();
		try {
			start.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
