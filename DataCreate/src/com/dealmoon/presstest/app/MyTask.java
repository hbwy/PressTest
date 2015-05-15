package com.dealmoon.presstest.app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.dealmoon.presstest.utils.MyUtils;
import com.dealmoon.presstest.utils.PropertiesReader;
import com.dealmoon.presstest.utils.Task;

/**
 * @author: WY
 * @data:2015��5��15�� ����10:04:04
 * @description: ����post
 */
public class MyTask extends Task {

	private static Map<String, List<String>> reqAppData = PropertiesReader.getAppRequestData();
	private static Map<String, List<String>> reqBackData = PropertiesReader.getBackendRequestData();

	private static Logger logger = Logger.getLogger(MyTask.class);

	public static void main(String[] args) {
		MyTask m = new MyTask();
		m.run();
	}

	public void run() {
		//���post

		List<String> reqJsons = (List<String>) reqAppData.get("postcreate");
		String token = MyUtils.getRandomToken();
		String reqJson = "{" + token + reqJsons.get(1) + "}";
		// textMap���ڴ��ı�,fileMap���ڴ�ͼƬ
		Map<String, String> textMap = new HashMap<String, String>();
		Map<String, String> fileMap = new HashMap<String, String>();

		textMap.put("requestData", reqJson);
		String image_path1 = "image/1.jpg";
		fileMap.put("images", image_path1);
		String response = MyUtils.postUpload(textMap, fileMap, reqJson);
		//logger.debug(response);

		//���brand

		List<String> reqBackJsons = (List<String>) reqBackData.get("Brandcreat");
		String reqJson0 = reqBackJsons.get(0);
		reqJson0 = reqJson0.replace("test", "testbrand" + Math.random());
		String response1 = MyUtils.sendBackPost(reqJson0);
		//logger.debug(response1);

		//���tag 
		List<String> reqBackJsons1 = (List<String>) reqBackData.get("Tagcreat");
		String reqJson1 = reqBackJsons1.get(0);
		reqJson1 = reqJson1.replace("test", "testtag" + Math.random());
		String response2 = MyUtils.sendBackPost(reqJson1);

		//logger.debug(response2);
	}

	@Override
	public Task[] taskCore() throws Exception {
		return null;
	}

	@Override
	protected boolean useDb() {
		return false;
	}

	@Override
	protected boolean needExecuteImmediate() {
		return true;
	}

	@Override
	public String info() {
		return String.valueOf(this.getTaskId());
	}
}
