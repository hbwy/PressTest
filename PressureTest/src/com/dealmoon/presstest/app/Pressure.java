package com.dealmoon.presstest.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.dealmoon.presstest.utils.MyUtils;
import com.dealmoon.presstest.utils.PropertiesReader;
import com.sun.jersey.core.util.Base64;

public class Pressure {

	private static Logger logger = Logger.getLogger(Pressure.class);
	private static Map<String, List<String>> reqData = PropertiesReader.getAppRequestData();
	private static String url = "http://api2.apps.dealmoon.com";

	//�����б�ӿ�
	public void categorylist() {
		List<String> reqJsons = (List<String>) reqData.get("categorylist");
		String reqJson0 = "{" + reqJsons.get(0) + "}";
		String response = MyUtils.sendPost(url, reqJson0);
		logger.info(response);
	}

	//ɹ����Ϣ�ӿ�
	public void postinfo() {
		List<String> reqJsons = (List<String>) reqData.get("postinfo");
		// �����ȡһ�����µ�post
		List<Integer> postIds = MyUtils.getPostList(url, "new", 1, 20);
		int postId = postIds.get(new Random().nextInt(postIds.size()));

		String reqJson0 = "{" + reqJsons.get(0) + "}";
		reqJson0 = MyUtils.replaceIdinBackCommand(reqJson0, postId);
		String response = MyUtils.sendPost(url, reqJson0);
		logger.info(response);
	}

	//��ȡ��Ϣ�б�
	public void messagegetlist() {
		List<String> reqJsons = (List<String>) reqData.get("messagegetlist");

		String reqJson0 = "{" + MyUtils.getRandomToken() + reqJsons.get(new Random().nextInt(3)) + "}";
		String response = MyUtils.sendPost(url, reqJson0);
		logger.info(response);
	}

	//��ȡ�ղص�ɹ�����б�
	public void postgetfavoritelist() {
		List<String> reqJsons = (List<String>) reqData.get("postgetfavoritelist");

		String reqJson0 = "{" + MyUtils.getRandomToken() + reqJsons.get(0) + "}";
		String response = MyUtils.sendPost(url, reqJson0);
		logger.info(response);
	}

	//��ȡ�û�ϲ����ɹ�����б�
	public void postgetlikelist() {
		List<String> reqJsons = (List<String>) reqData.get("postgetlikelist");
		Map<String, String> user_token = MyUtils.getRandomIdToken();
		int userId = Integer.parseInt((String) user_token.get("id"));
		String token = (String) user_token.get("token");

		String reqJson0 = "{" + token + reqJsons.get(0) + "}";
		reqJson0 = MyUtils.replaceUserid(reqJson0, userId);
		String response = MyUtils.sendPost(url, reqJson0);
		logger.info(response);
	}

	// ϲ����Ʒ�Ƶ��б�
	public void brandlikelist() {
		List<String> reqJsons = (List<String>) reqData.get("brandlikelist");
		Map<String, String> user_token = MyUtils.getRandomIdToken();
		int userId = Integer.parseInt((String) user_token.get("id"));

		String reqJson0 = "{" + reqJsons.get(0) + "}";
		reqJson0 = MyUtils.replaceUserid(reqJson0, userId);
		String response = MyUtils.sendPost(url, reqJson0);
		logger.info(response);
	}

	// Ʒ���б�
	public void brandlist() {
		List<String> reqJsons = (List<String>) reqData.get("brandlist");
		String reqJson0 = "{" + reqJsons.get(1) + "}";
		String response = MyUtils.sendPost(url, reqJson0);
		logger.info(response);
	}

	//��ȡɹ���б�
	public void postgetlist() {
		List<String> reqJsons = (List<String>) reqData.get("postgetlist");
		String reqJson0 = "{" + reqJsons.get(0) + "}";
		String response = MyUtils.sendPost(url, reqJson0);
		logger.info(response);
	}

	//���ϲ��
	public void postaddlike() {
		List<String> reqJsons = (List<String>) reqData.get("postaddlike");
		//�����ȡһ�����µ�post
		List<Integer> postIds = MyUtils.getPostList(url, "new", 1, 20);
		int postId = postIds.get(new Random().nextInt(postIds.size()));

		String reqJson0 = "{" + MyUtils.getRandomToken() + reqJsons.get(0) + "}";
		reqJson0 = MyUtils.replaceIdinBackCommand(reqJson0, postId);
		String response = MyUtils.sendPost(url, reqJson0);
		logger.info(response);
	}

	//ɾ��ɹ������
	public void postdelcomment() {
		List<String> reqJsons = (List<String>) reqData.get("postdelcomment");
		String token = MyUtils.getRandomToken();

		//�����ȡһ�����µ�post
		List<Integer> postIds = MyUtils.getPostList(url, "new", 1, 20);
		int postId = postIds.get(new Random().nextInt(postIds.size()));

		//�����һ������ ��ɾ��
		List<String> _reqJsons = (List<String>) reqData.get("postaddcomment");
		String addJson = "{" + token + _reqJsons.get(0) + "}";

		addJson = MyUtils.replaceIdinBackCommand(addJson, postId);
		String addResponse = MyUtils.sendPost(url, addJson);
		//��ȡ����ӵ����۵�id
		int commentId = (int) JSONObject.fromObject(addResponse).getJSONObject("responseData").getJSONObject("comment")
				.get("id");
		String reqJson0 = "{" + token + reqJsons.get(0) + "}";
		reqJson0 = MyUtils.replaceIdinBackCommand(reqJson0, commentId);
		//����ɾ��ƽ�۵�����
		String response = MyUtils.sendPost(url, reqJson0);
		logger.info(response);
	}

	//ɾ��ɹ��
	public void postdelete() {
		List<String> reqJsons = (List<String>) reqData.get("postdelete");
		String token = MyUtils.getRandomToken();

		// ��ȡcreate post��ָ��
		List<String> addJsons = (List<String>) reqData.get("postcreate");
		String addJson = "{" + token + addJsons.get(0) + "}";

		// textMap���ڴ��ı�,fileMap���ڴ�ͼƬ
		Map<String, String> textMap = new HashMap<String, String>();
		Map<String, String> fileMap = new HashMap<String, String>();

		textMap.put("requestData", addJson);

		String image_path1 = "image/1.jpg";

		fileMap.put("images", image_path1);
		// ����create post����
		String response1 = MyUtils.postUpload(url + "/Post", textMap, fileMap, addJson);
		// ��ȡ�´�����post��id
		int postId = JSONObject.fromObject(response1).getJSONObject("responseData").getJSONObject("post").getInt("id");
		// ����ɾ����post��ָ��
		String delJson = "{" + token + reqJsons.get(0) + "}";
		delJson = MyUtils.replaceIdinBackCommand(delJson, postId);
		// ����ɾ����post������
		String response = MyUtils.sendPost(url, delJson);
		logger.info(response);
	}

	//ȡ���ղ�ɹ��
	public void postdelfavorite() {
		List<String> reqJsons = (List<String>) reqData.get("postdelfavorite");
		String token = MyUtils.getRandomToken();
		//�����ȡһ�����µ�post
		List<Integer> postIds = MyUtils.getPostList(url, "new", 1, 20);
		int postId = postIds.get(new Random().nextInt(postIds.size()));

		//���ȡ��һ��post������ղ�
		List<String> _reqJsons = (List<String>) reqData.get("postaddfavorite");
		String addJson = "{" + token + _reqJsons.get(0) + "}";
		addJson = MyUtils.replaceIdinBackCommand(addJson, postId);
		//�����ղ�post������
		MyUtils.sendPost(url, addJson);
		//��ȡɾ���ղص�ָ��
		String delJson = "{" + token + reqJsons.get(0) + "}";
		delJson = MyUtils.replaceIdinBackCommand(delJson, postId);
		//����ȡ���ղص�ָ��
		String response = MyUtils.sendPost(url, delJson);
		logger.info(response);
	}

	//ȡ��ϲ��ɹ��
	public void postdellike() {
		List<String> reqJsons = (List<String>) reqData.get("postdellike");
		String token = MyUtils.getRandomToken();
		//�����ȡһ�����µ�post
		List<Integer> postIds = MyUtils.getPostList(url, "new", 1, 20);
		int postId = postIds.get(new Random().nextInt(postIds.size()));

		//���ȡ��һ��post�����ϲ��
		List<String> _reqJsons = (List<String>) reqData.get("postaddlike");
		String addJson = "{" + token + _reqJsons.get(0) + "}";
		addJson = MyUtils.replaceIdinBackCommand(addJson, postId);
		//����ϲ��post������
		MyUtils.sendPost(url, addJson);
		//��ȡɾ��ϲ����ָ��
		String delJson = "{" + token + reqJsons.get(0) + "}";
		delJson = MyUtils.replaceIdinBackCommand(delJson, postId);
		//����ȡ��ϲ����ָ��
		String response = MyUtils.sendPost(url, delJson);
		logger.info(response);
	}

	//�ٱ�ɹ��
	public void postreport() {
		List<String> reqJsons = (List<String>) reqData.get("postreport");
		//�����ȡһ�����µ�post
		List<Integer> postIds = MyUtils.getPostList(url, "new", 1, 20);
		int postId = postIds.get(new Random().nextInt(postIds.size()));
		//��ѯpost��Ϣ
		String _response = MyUtils.getPostInfo(url, postId);
		//��ȡpost��author��Id
		int post_userId = JSONObject.fromObject(_response).getJSONObject("responseData").getJSONObject("post")
				.getJSONObject("author").getInt("id");

		String reqJson0 = "{" + MyUtils.getRandomToken(post_userId) + reqJsons.get(0) + "}";
		String response = MyUtils.sendPost(url, reqJson0);
		logger.info(response);
	}

	//����ɹ��
	public void postupdate() {
		List<String> reqJsons = (List<String>) reqData.get("postupdate");
		String token = MyUtils.getRandomToken();

		// ��ȡcreate post��ָ��
		List<String> addReqJsons = (List<String>) reqData.get("postcreate");
		String addJson = "{" + token + addReqJsons.get(4) + "}";

		// textMap���ڴ��ı�,fileMap���ڴ�ͼƬ
		Map<String, String> textMap = new HashMap<String, String>();
		Map<String, String> fileMap = new HashMap<String, String>();

		textMap.put("requestData", addJson);

		String image_path1 = "image/1.jpg";
		String image_path2 = "image/2.jpg";
		String image_path3 = "image/3.jpg";
		String image_path4 = "image/4.jpg";

		fileMap.put("images", image_path1);
		fileMap.put("images", image_path2);
		fileMap.put("images", image_path3);
		fileMap.put("images", image_path4);
		// ����create post����
		String response1 = MyUtils.postUpload(url, textMap, fileMap, addJson);
		// ��ȡ�´�����post��id
		int postId = JSONObject.fromObject(response1).getJSONObject("responseData").getJSONObject("post").getInt("id");
		JSONArray images = JSONObject.fromObject(response1).getJSONObject("responseData").getJSONObject("post")
				.getJSONArray("images");
		List<Integer> imagelist = new ArrayList<Integer>();
		for (Iterator iterator = images.iterator(); iterator.hasNext();) {
			JSONObject image = (JSONObject) iterator.next();
			imagelist.add(image.getInt("id"));
		}

		// textMap���ڴ��ı�,fileMap���ڴ�ͼƬ
		Map<String, String> textMap1 = new HashMap<String, String>();
		Map<String, String> fileMap1 = new HashMap<String, String>();

		String reqJson = "{" + token + reqJsons.get(0) + "}";
		reqJson = MyUtils.replaceIdinBackCommand(reqJson, postId);
		String deletePhotoIds = JSONObject.fromObject(reqJson).getJSONObject("commandInfo")
				.getJSONArray("deletePhotoIds").toString();
		String ids = "";
		for (int i = 0; i < imagelist.size(); i++) {
			if (i < imagelist.size() - 1) {
				ids = ids + imagelist.get(i).toString() + ",";
			} else {
				ids = ids + imagelist.get(i).toString();
			}

		}
		reqJson = reqJson.replace("\"deletePhotoIds\":" + deletePhotoIds, "\"deletePhotoIds\":[" + ids + "]");
		textMap1.put("requestData", reqJson);
		fileMap1.put("images", image_path1);
		// �����޸ĵ�Post�ӿڽṹ
		String response = MyUtils.postUpload(url, textMap1, fileMap1, reqJson);
		logger.info(response);
	}

	//�û���˿�б�
	public void userfanlist() {
		List<String> reqJsons = (List<String>) reqData.get("userfanlist");
		int userId = Integer.parseInt((String) MyUtils.getRandomIdToken().get("id"));

		String reqJson0 = "{" + reqJsons.get(0) + "}";
		reqJson0 = MyUtils.replaceIdinBackCommand(reqJson0, userId);
		String response = MyUtils.sendPost(url, reqJson0);
		logger.info(response);
	}

	//��ע�û�
	public void userfollow() {
		List<String> reqJsons = (List<String>) reqData.get("userfollow");
		//�õ�ִ�й�ע�������û�id
		Map id_token = MyUtils.getRandomIdToken();
		int id = Integer.parseInt((String) id_token.get("id"));
		String token = (String) id_token.get("token");
		//�õ�����ע�û���id
		int userId = MyUtils.getRandomUserId(id);

		String reqJson0 = "{" + token + reqJsons.get(0) + "}";
		reqJson0 = MyUtils.replaceIdinBackCommand(reqJson0, userId);
		String response = MyUtils.sendPost(url, reqJson0);
		logger.info(response);
	}

	//��ע���û����б�
	public void userfollowlist() {
		List<String> reqJsons = (List<String>) reqData.get("userfollowlist");
		int userId = Integer.parseInt((String) MyUtils.getRandomIdToken().get("id"));

		String reqJson0 = "{" + reqJsons.get(0) + "}";
		reqJson0 = MyUtils.replaceIdinBackCommand(reqJson0, userId);
		String response = MyUtils.sendPost(url, reqJson0);
		logger.info(response);
	}

	//��ȡɹ������
	public void postgetcomment() {
		List<String> reqJsons = (List<String>) reqData.get("postgetcomment");
		// �����ȡһ�����µ�post
		List<Integer> postIds = MyUtils.getPostList(url, "new", 1, 20);
		int postId = postIds.get(new Random().nextInt(postIds.size()));

		String reqJson0 = "{" + reqJsons.get(0) + "}";
		reqJson0 = MyUtils.replaceIdinBackCommand(reqJson0, postId);
		String response = MyUtils.sendPost(url, reqJson0);
		logger.info(response);
	}

	//��ȡ�Ƽ���tag
	public void hashtagrecommends() {
		List<String> reqJsons = (List<String>) reqData.get("hashtagrecommends");

		String reqJson0 = "{" + reqJsons.get(0) + "}";
		String response = MyUtils.sendPost(url, reqJson0);
		logger.info(response);
	}

	//����tag
	public void hashtagsearch() {
		List<String> reqJsons = (List<String>) reqData.get("hashtagsearch");

		String reqJson0 = "{" + reqJsons.get(1) + "}";
		String response = MyUtils.sendPost(url, reqJson0);
		logger.info(response);
	}

	//ɾ����Ϣ
	public void messagedelete() {
		List<String> reqJsons = (List<String>) reqData.get("messagedelete");
		String token = MyUtils.getRandomToken();
		//�����ȡ��Ϣ�б��е���Ϣ
		List<Integer> messageList = MyUtils.getMessageList(url, token);
		int size = messageList.size();
		int messageId = 0;
		if (size > 0) {
			messageId = messageList.get(new Random().nextInt(size));
		}
		//ɾ����Ϣ
		String reqJson0 = "{" + token + reqJsons.get(0) + "}";
		reqJson0 = MyUtils.replaceMessageid(reqJson0, messageId);
		String resposne = MyUtils.sendPost(url, reqJson0);
		logger.info(resposne);
	}

	//�ղ�ɹ��
	public void postaddfavorite() {
		List<String> reqJsons = (List<String>) reqData.get("postaddfavorite");
		String token = MyUtils.getRandomToken();
		//�����ȡһ�����µ�post
		List<Integer> postIds = MyUtils.getPostList(url, "new", 1, 20);
		int postId = postIds.get(new Random().nextInt(postIds.size()));

		String reqJson0 = "{" + token + reqJsons.get(0) + "}";
		reqJson0 = MyUtils.replaceIdinBackCommand(reqJson0, postId);
		String response = MyUtils.sendPost(url, reqJson0);
		logger.info(response);
	}

	//�������
	public void postaddcomment() {
		List<String> reqJsons = (List<String>) reqData.get("postaddcomment");
		//�����ȡһ�����µ�post
		List<Integer> postIds = MyUtils.getPostList(url, "new", 1, 20);
		int postId = postIds.get(new Random().nextInt(postIds.size()));

		String reqJson0 = "{" + MyUtils.getRandomToken() + reqJsons.get(new Random().nextInt(3)) + "}";
		reqJson0 = MyUtils.replaceIdinBackCommand(reqJson0, postId);
		String response = MyUtils.sendPost(url, reqJson0);
		logger.info(response);
	}

	//Tag����
	public void taginfo() {
		List<String> reqJsons = (List<String>) reqData.get("taginfo");
		String reqJson0 = "{" + reqJsons.get(new Random().nextInt(6)) + "}";
		String response = MyUtils.sendPost(url, reqJson0);
		logger.info(response);
	}

	//��עƷ��
	public void brandaddlike() {
		List<String> reqJsons = (List<String>) reqData.get("brandaddlike");
		// �����ȡһ��Ʒ��Id
		List<Integer> brandIds = MyUtils.getBrandList(url);
		int brandId = brandIds.get(new Random().nextInt(brandIds.size()));

		String reqJson0 = "{" + MyUtils.getRandomToken() + reqJsons.get(0) + "}";
		reqJson0 = MyUtils.replaceIdinBackCommand(reqJson0, brandId);
		String response = MyUtils.sendPost(url, reqJson0);
		logger.info(response);
	}

	//��ȡ������Ϣ
	public void dealsubscriptiongetUnRead() {
		List<String> reqJsons = (List<String>) reqData.get("dealsubscriptiongetUnRead");
		String token = MyUtils.getRandomToken();

		String reqJson0 = "{" + token + reqJsons.get(0) + "}";
		reqJson0 = MyUtils.replaceSince(reqJson0);
		String response = MyUtils.sendPost(url, reqJson0);
		logger.info(response);
	}

	//�ղ��б�
	public void dealdealgetFavDealList() {
		List<String> reqJsons = (List<String>) reqData.get("dealdealgetFavDealList");

		String reqJson0 = "{" + reqJsons.get(0) + "}";
		reqJson0 = MyUtils.replaceSince(reqJson0);
		String response = MyUtils.sendPost(url, reqJson0);
		logger.info(response);
	}

	//�û���Ϣ
	public void userinfo() {
		List<String> reqJsons = (List<String>) reqData.get("Userinfo");

		String reqJson0 = "{" + MyUtils.getRandomToken() + reqJsons.get(0) + "}";
		String response = MyUtils.sendPost(url, reqJson0);
		logger.info(response);
	}

	//�û���¼
	public void userlogin() {
		//�����ȡһ���û�����Ϣ
		Map<String, String> info = MyUtils.getRandomUserInfo();
		List<String> userlogin = (List<String>) reqData.get("userlogin");
		String reqJson1 = "{" + userlogin.get(0) + "}";

		JSONObject commandInfo = JSONObject.fromObject(reqJson1).getJSONObject("commandInfo");
		String oldemail = commandInfo.getString("email");
		String oldpassword = commandInfo.getString("password");
		String password = "";
		try {
			password = new String(Base64.encode(MyUtils.encrypt(oldpassword.getBytes())));
		} catch (Exception e) {
			e.printStackTrace();
		}
		reqJson1 = reqJson1.replace("\"email\":\"" + oldemail + "\",", "\"email\":\"" + info.get("email") + "\",")
				.replace("\"password\":\"" + oldpassword + "\"", "\"password\":\"" + password + "\"");
		String response = MyUtils.sendPost(url, reqJson1);
		logger.info(response);
	}

	//�û��ǳ�  Ϊ�˲�Ӱ�����  �ǳ���������¼
	public void userlogout() {
		List<String> reqJsons = (List<String>) reqData.get("userlogout");
		//�����ȡһ���û�����Ϣ
		Map<String, String> info = MyUtils.getRandomUserInfo();
		//�û��ǳ�
		String reqJson0 = "{" + info.get("token") + reqJsons.get(0) + "}";
		String response = MyUtils.sendPost(url, reqJson0);
		logger.info(response);

		//Ϊ�˲�Ӱ�����,�ǳ����¼
		List<String> userlogin = (List<String>) reqData.get("userlogin");
		String reqJson1 = "{" + userlogin.get(0) + "}";

		JSONObject commandInfo = JSONObject.fromObject(reqJson1).getJSONObject("commandInfo");
		String oldemail = commandInfo.getString("email");
		String oldpassword = commandInfo.getString("password");
		String password = "";
		try {
			password = new String(Base64.encode(MyUtils.encrypt(oldpassword.getBytes())));
		} catch (Exception e) {
			e.printStackTrace();
		}
		reqJson1 = reqJson1.replace("\"email\":\"" + oldemail + "\",", "\"email\":\"" + info.get("email") + "\",")
				.replace("\"password\":\"" + oldpassword + "\"", "\"password\":\"" + password + "\"");
		String response1 = MyUtils.sendPost(url, reqJson1);
	}

	//��ѯ�û���Ϣ
	public void userprofile() {
		List<String> reqJsons = (List<String>) reqData.get("userprofile");
		int userId = Integer.parseInt((String) MyUtils.getRandomIdToken().get("id"));

		String reqJson0 = "{" + reqJsons.get(0) + "}";
		reqJson0 = MyUtils.replaceIdinBackCommand(reqJson0, userId);
		String response = MyUtils.sendPost(url, reqJson0);
		logger.info(response);
	}

	//ȡ����ע�û�
	public void userunfollow() {
		List<String> reqJsons = (List<String>) reqData.get("userunfollow");
		//�õ�ִ�й�ע�������û�id
		Map id_token = MyUtils.getRandomIdToken();
		int id = Integer.parseInt((String) id_token.get("id"));
		String token = (String) id_token.get("token");
		//�õ�����ע�û���id
		int userId = MyUtils.getRandomUserId(id);

		// ��ȡ��ע�û�ָ��
		List<String> reqJson1 = (List<String>) reqData.get("userfollow");
		String addJson = "{" + token + reqJson1.get(0) + "}";
		addJson = MyUtils.replaceIdinBackCommand(addJson, userId);
		// ���͹�ע�û�������
		MyUtils.sendPost(url, addJson);
		//��ȡȡ����ע�û�ָ��
		String reqJson0 = "{" + token + reqJsons.get(0) + "}";
		reqJson0 = MyUtils.replaceIdinBackCommand(reqJson0, userId);
		String response = MyUtils.sendPost(url, reqJson0);
		logger.info(response);
	}

	//����ɹ��
	public void postcreate() {
		List<String> reqJsons = (List<String>) reqData.get("postcreate");
		String token = MyUtils.getRandomToken();
		String reqJson = "{" + token + reqJsons.get(4) + "}";

		// textMap���ڴ��ı�,fileMap���ڴ�ͼƬ
		Map<String, String> textMap = new HashMap<String, String>();
		Map<String, String> fileMap = new HashMap<String, String>();

		textMap.put("requestData", reqJson);
		String image_path1 = "image/1.jpg";
		String image_path2 = "image/2.jpg";
		String image_path3 = "image/3.jpg";
		String image_path4 = "image/4.jpg";
		fileMap.put("images", image_path1);
		fileMap.put("images", image_path2);
		fileMap.put("images", image_path3);
		fileMap.put("images", image_path4);

		String response = MyUtils.postUpload(url, textMap, fileMap, reqJson);
		logger.info(response);
	}

	// ���͵�deal�б�
	public void dealsubscriptionmyAlerts() {
		List<String> reqJsons = (List<String>) reqData.get("dealsubscriptionmyAlerts");

		String reqJson0 = "{" + reqJsons.get(0) + "}";
		String response = MyUtils.sendPost(url, reqJson0);
		logger.info(response);
	}
}
