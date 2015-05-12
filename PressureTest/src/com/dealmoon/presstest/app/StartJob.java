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
		System.out.println("------------- ��ʼ�� ��� Scheduler ���� -------------");

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

		System.out.println("��ʼʱ��: " + sdf.format(startTime) + ",����ʱ��: " + sdf.format(endTime));

		// ����һ����������startAt��������������Ӧ����ʼ��ʱ�� .����һ����������ִ��  
		SimpleTrigger trigger = (SimpleTrigger) newTrigger().withIdentity("trigger1", "group1").startAt(startTime)
				.endAt(endTime).withSchedule(simpleSchedule().withIntervalInSeconds(5).repeatForever()).build();

		// ע�Ტ���е���    
		Date ft = scheduler.scheduleJob(job, trigger);

		// ����  
		scheduler.start();
		System.out.println(" ----------------- ��������Ѿ����� -----------------");

		try {
			//��ʱ����� 10��
			Thread.sleep(currentTime + runTime + 10000L);

		} catch (Exception e) {
		}

		//������ֹͣ  
		scheduler.shutdown(true);
		System.out.println(" ----------------- ������Ƚ��� -----------------");

		SchedulerMetaData metaData = scheduler.getMetaData();
		System.out.println("Executed " + metaData.getNumberOfJobsExecuted() + " jobs.");
	}

	public static void main(String[] args) throws Exception {
		StartJob start = new StartJob();
		start.run();
	}
}
