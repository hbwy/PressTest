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
 * @data:2015��5��4�� ����11:24:17
 * @description: ������ҵ
 */
public class MyJob implements Job {

	@Override
	public void execute(JobExecutionContext context) {
		// ȡ���̳߳ض���
		ThreadPool threadPool = ThreadPool.getInstance();
		// ��ȡ�����ļ��еĽӿ��б�
		Map<String, String> inters = PropertiesReader.getInters();
		List<Object> keys = PropertiesReader.getKeys();
		Random rm = new Random();
	    int max = Integer.parseInt((String) keys.get(keys.size()-1));
	    System.out.println("max----------"+max);
		// ɨ��һ�������ļ�N���ӿ�,����N������
		for (int i = 0; i < keys.size(); i++) {
			//�������1..sum֮�����
			int number = rm.nextInt(max+1);
			//����Ȩֵ�б��ҵ�������������Сֵ,���Ѹ�ֵ��Ӧ�Ľӿ�������Ϊ����Ľӿ�
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
