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

	//分类列表接口
	public void categorylist() {
		List<String> reqJsons = (List<String>) reqData.get("categorylist");
		String reqJson0 = "{" + reqJsons.get(0) + "}";
		String response = MyUtils.sendPost(url, reqJson0);
		logger.info(response);
	}

	//晒单信息接口
	public void postinfo() {
		List<String> reqJsons = (List<String>) reqData.get("postinfo");
		// 随机获取一个最新的post
		List<Integer> postIds = MyUtils.getPostList(url, "new", 1, 20);
		int postId = postIds.get(new Random().nextInt(postIds.size()));

		String reqJson0 = "{" + reqJsons.get(0) + "}";
		reqJson0 = MyUtils.replaceIdinBackCommand(reqJson0, postId);
		String response = MyUtils.sendPost(url, reqJson0);
		logger.info(response);
	}

	//获取消息列表
	public void messagegetlist() {
		List<String> reqJsons = (List<String>) reqData.get("messagegetlist");

		String reqJson0 = "{" + MyUtils.getRandomToken() + reqJsons.get(new Random().nextInt(3)) + "}";
		String response = MyUtils.sendPost(url, reqJson0);
		logger.info(response);
	}

	//获取收藏的晒单的列表
	public void postgetfavoritelist() {
		List<String> reqJsons = (List<String>) reqData.get("postgetfavoritelist");

		String reqJson0 = "{" + MyUtils.getRandomToken() + reqJsons.get(0) + "}";
		String response = MyUtils.sendPost(url, reqJson0);
		logger.info(response);
	}

	//获取用户喜欢的晒单的列表
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

	// 喜欢的品牌的列表
	public void brandlikelist() {
		List<String> reqJsons = (List<String>) reqData.get("brandlikelist");
		Map<String, String> user_token = MyUtils.getRandomIdToken();
		int userId = Integer.parseInt((String) user_token.get("id"));

		String reqJson0 = "{" + reqJsons.get(0) + "}";
		reqJson0 = MyUtils.replaceUserid(reqJson0, userId);
		String response = MyUtils.sendPost(url, reqJson0);
		logger.info(response);
	}

	// 品牌列表
	public void brandlist() {
		List<String> reqJsons = (List<String>) reqData.get("brandlist");
		String reqJson0 = "{" + reqJsons.get(1) + "}";
		String response = MyUtils.sendPost(url, reqJson0);
		logger.info(response);
	}

	//获取晒单列表
	public void postgetlist() {
		List<String> reqJsons = (List<String>) reqData.get("postgetlist");
		String reqJson0 = "{" + reqJsons.get(0) + "}";
		String response = MyUtils.sendPost(url, reqJson0);
		logger.info(response);
	}

	//添加喜欢
	public void postaddlike() {
		List<String> reqJsons = (List<String>) reqData.get("postaddlike");
		//随机获取一个最新的post
		List<Integer> postIds = MyUtils.getPostList(url, "new", 1, 20);
		int postId = postIds.get(new Random().nextInt(postIds.size()));

		String reqJson0 = "{" + MyUtils.getRandomToken() + reqJsons.get(0) + "}";
		reqJson0 = MyUtils.replaceIdinBackCommand(reqJson0, postId);
		String response = MyUtils.sendPost(url, reqJson0);
		logger.info(response);
	}

	//删除晒单评论
	public void postdelcomment() {
		List<String> reqJsons = (List<String>) reqData.get("postdelcomment");
		String token = MyUtils.getRandomToken();

		//随机获取一个最新的post
		List<Integer> postIds = MyUtils.getPostList(url, "new", 1, 20);
		int postId = postIds.get(new Random().nextInt(postIds.size()));

		//先添加一条评论 在删除
		List<String> _reqJsons = (List<String>) reqData.get("postaddcomment");
		String addJson = "{" + token + _reqJsons.get(0) + "}";

		addJson = MyUtils.replaceIdinBackCommand(addJson, postId);
		String addResponse = MyUtils.sendPost(url, addJson);
		//获取新添加的评论的id
		int commentId = (int) JSONObject.fromObject(addResponse).getJSONObject("responseData").getJSONObject("comment")
				.get("id");
		String reqJson0 = "{" + token + reqJsons.get(0) + "}";
		reqJson0 = MyUtils.replaceIdinBackCommand(reqJson0, commentId);
		//发送删除平论的请求
		String response = MyUtils.sendPost(url, reqJson0);
		logger.info(response);
	}

	//删除晒单
	public void postdelete() {
		List<String> reqJsons = (List<String>) reqData.get("postdelete");
		String token = MyUtils.getRandomToken();

		// 获取create post的指令
		List<String> addJsons = (List<String>) reqData.get("postcreate");
		String addJson = "{" + token + addJsons.get(0) + "}";

		// textMap用于存文本,fileMap用于存图片
		Map<String, String> textMap = new HashMap<String, String>();
		Map<String, String> fileMap = new HashMap<String, String>();

		textMap.put("requestData", addJson);

		String image_path1 = "image/1.jpg";

		fileMap.put("images", image_path1);
		// 发送create post请求
		String response1 = MyUtils.postUpload(url + "/Post", textMap, fileMap, addJson);
		// 获取新创建的post的id
		int postId = JSONObject.fromObject(response1).getJSONObject("responseData").getJSONObject("post").getInt("id");
		// 构造删除该post的指令
		String delJson = "{" + token + reqJsons.get(0) + "}";
		delJson = MyUtils.replaceIdinBackCommand(delJson, postId);
		// 发送删除该post的请求
		String response = MyUtils.sendPost(url, delJson);
		logger.info(response);
	}

	//取消收藏晒单
	public void postdelfavorite() {
		List<String> reqJsons = (List<String>) reqData.get("postdelfavorite");
		String token = MyUtils.getRandomToken();
		//随机获取一个最新的post
		List<Integer> postIds = MyUtils.getPostList(url, "new", 1, 20);
		int postId = postIds.get(new Random().nextInt(postIds.size()));

		//随机取得一个post并添加收藏
		List<String> _reqJsons = (List<String>) reqData.get("postaddfavorite");
		String addJson = "{" + token + _reqJsons.get(0) + "}";
		addJson = MyUtils.replaceIdinBackCommand(addJson, postId);
		//发送收藏post的请求
		MyUtils.sendPost(url, addJson);
		//获取删除收藏的指令
		String delJson = "{" + token + reqJsons.get(0) + "}";
		delJson = MyUtils.replaceIdinBackCommand(delJson, postId);
		//发送取消收藏的指令
		String response = MyUtils.sendPost(url, delJson);
		logger.info(response);
	}

	//取消喜欢晒单
	public void postdellike() {
		List<String> reqJsons = (List<String>) reqData.get("postdellike");
		String token = MyUtils.getRandomToken();
		//随机获取一个最新的post
		List<Integer> postIds = MyUtils.getPostList(url, "new", 1, 20);
		int postId = postIds.get(new Random().nextInt(postIds.size()));

		//随机取得一个post并添加喜欢
		List<String> _reqJsons = (List<String>) reqData.get("postaddlike");
		String addJson = "{" + token + _reqJsons.get(0) + "}";
		addJson = MyUtils.replaceIdinBackCommand(addJson, postId);
		//发送喜欢post的请求
		MyUtils.sendPost(url, addJson);
		//获取删除喜欢的指令
		String delJson = "{" + token + reqJsons.get(0) + "}";
		delJson = MyUtils.replaceIdinBackCommand(delJson, postId);
		//发送取消喜欢的指令
		String response = MyUtils.sendPost(url, delJson);
		logger.info(response);
	}

	//举报晒单
	public void postreport() {
		List<String> reqJsons = (List<String>) reqData.get("postreport");
		//随机获取一个最新的post
		List<Integer> postIds = MyUtils.getPostList(url, "new", 1, 20);
		int postId = postIds.get(new Random().nextInt(postIds.size()));
		//查询post信息
		String _response = MyUtils.getPostInfo(url, postId);
		//获取post的author的Id
		int post_userId = JSONObject.fromObject(_response).getJSONObject("responseData").getJSONObject("post")
				.getJSONObject("author").getInt("id");

		String reqJson0 = "{" + MyUtils.getRandomToken(post_userId) + reqJsons.get(0) + "}";
		String response = MyUtils.sendPost(url, reqJson0);
		logger.info(response);
	}

	//更新晒单
	public void postupdate() {
		List<String> reqJsons = (List<String>) reqData.get("postupdate");
		String token = MyUtils.getRandomToken();

		// 获取create post的指令
		List<String> addReqJsons = (List<String>) reqData.get("postcreate");
		String addJson = "{" + token + addReqJsons.get(4) + "}";

		// textMap用于存文本,fileMap用于存图片
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
		// 发送create post请求
		String response1 = MyUtils.postUpload(url, textMap, fileMap, addJson);
		// 获取新创建的post的id
		int postId = JSONObject.fromObject(response1).getJSONObject("responseData").getJSONObject("post").getInt("id");
		JSONArray images = JSONObject.fromObject(response1).getJSONObject("responseData").getJSONObject("post")
				.getJSONArray("images");
		List<Integer> imagelist = new ArrayList<Integer>();
		for (Iterator iterator = images.iterator(); iterator.hasNext();) {
			JSONObject image = (JSONObject) iterator.next();
			imagelist.add(image.getInt("id"));
		}

		// textMap用于存文本,fileMap用于存图片
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
		// 返回修改的Post接口结构
		String response = MyUtils.postUpload(url, textMap1, fileMap1, reqJson);
		logger.info(response);
	}

	//用户粉丝列表
	public void userfanlist() {
		List<String> reqJsons = (List<String>) reqData.get("userfanlist");
		int userId = Integer.parseInt((String) MyUtils.getRandomIdToken().get("id"));

		String reqJson0 = "{" + reqJsons.get(0) + "}";
		reqJson0 = MyUtils.replaceIdinBackCommand(reqJson0, userId);
		String response = MyUtils.sendPost(url, reqJson0);
		logger.info(response);
	}

	//关注用户
	public void userfollow() {
		List<String> reqJsons = (List<String>) reqData.get("userfollow");
		//拿到执行关注操作的用户id
		Map id_token = MyUtils.getRandomIdToken();
		int id = Integer.parseInt((String) id_token.get("id"));
		String token = (String) id_token.get("token");
		//拿到被关注用户的id
		int userId = MyUtils.getRandomUserId(id);

		String reqJson0 = "{" + token + reqJsons.get(0) + "}";
		reqJson0 = MyUtils.replaceIdinBackCommand(reqJson0, userId);
		String response = MyUtils.sendPost(url, reqJson0);
		logger.info(response);
	}

	//关注的用户的列表
	public void userfollowlist() {
		List<String> reqJsons = (List<String>) reqData.get("userfollowlist");
		int userId = Integer.parseInt((String) MyUtils.getRandomIdToken().get("id"));

		String reqJson0 = "{" + reqJsons.get(0) + "}";
		reqJson0 = MyUtils.replaceIdinBackCommand(reqJson0, userId);
		String response = MyUtils.sendPost(url, reqJson0);
		logger.info(response);
	}

	//获取晒单评论
	public void postgetcomment() {
		List<String> reqJsons = (List<String>) reqData.get("postgetcomment");
		// 随机获取一个最新的post
		List<Integer> postIds = MyUtils.getPostList(url, "new", 1, 20);
		int postId = postIds.get(new Random().nextInt(postIds.size()));

		String reqJson0 = "{" + reqJsons.get(0) + "}";
		reqJson0 = MyUtils.replaceIdinBackCommand(reqJson0, postId);
		String response = MyUtils.sendPost(url, reqJson0);
		logger.info(response);
	}

	//获取推荐的tag
	public void hashtagrecommends() {
		List<String> reqJsons = (List<String>) reqData.get("hashtagrecommends");

		String reqJson0 = "{" + reqJsons.get(0) + "}";
		String response = MyUtils.sendPost(url, reqJson0);
		logger.info(response);
	}

	//搜索tag
	public void hashtagsearch() {
		List<String> reqJsons = (List<String>) reqData.get("hashtagsearch");

		String reqJson0 = "{" + reqJsons.get(1) + "}";
		String response = MyUtils.sendPost(url, reqJson0);
		logger.info(response);
	}

	//删除消息
	public void messagedelete() {
		List<String> reqJsons = (List<String>) reqData.get("messagedelete");
		String token = MyUtils.getRandomToken();
		//随机获取消息列表中的消息
		List<Integer> messageList = MyUtils.getMessageList(url, token);
		int size = messageList.size();
		int messageId = 0;
		if (size > 0) {
			messageId = messageList.get(new Random().nextInt(size));
		}
		//删除消息
		String reqJson0 = "{" + token + reqJsons.get(0) + "}";
		reqJson0 = MyUtils.replaceMessageid(reqJson0, messageId);
		String resposne = MyUtils.sendPost(url, reqJson0);
		logger.info(resposne);
	}

	//收藏晒单
	public void postaddfavorite() {
		List<String> reqJsons = (List<String>) reqData.get("postaddfavorite");
		String token = MyUtils.getRandomToken();
		//随机获取一个最新的post
		List<Integer> postIds = MyUtils.getPostList(url, "new", 1, 20);
		int postId = postIds.get(new Random().nextInt(postIds.size()));

		String reqJson0 = "{" + token + reqJsons.get(0) + "}";
		reqJson0 = MyUtils.replaceIdinBackCommand(reqJson0, postId);
		String response = MyUtils.sendPost(url, reqJson0);
		logger.info(response);
	}

	//添加评论
	public void postaddcomment() {
		List<String> reqJsons = (List<String>) reqData.get("postaddcomment");
		//随机获取一个最新的post
		List<Integer> postIds = MyUtils.getPostList(url, "new", 1, 20);
		int postId = postIds.get(new Random().nextInt(postIds.size()));

		String reqJson0 = "{" + MyUtils.getRandomToken() + reqJsons.get(new Random().nextInt(3)) + "}";
		reqJson0 = MyUtils.replaceIdinBackCommand(reqJson0, postId);
		String response = MyUtils.sendPost(url, reqJson0);
		logger.info(response);
	}

	//Tag详情
	public void taginfo() {
		List<String> reqJsons = (List<String>) reqData.get("taginfo");
		String reqJson0 = "{" + reqJsons.get(new Random().nextInt(6)) + "}";
		String response = MyUtils.sendPost(url, reqJson0);
		logger.info(response);
	}

	//关注品牌
	public void brandaddlike() {
		List<String> reqJsons = (List<String>) reqData.get("brandaddlike");
		// 随机获取一个品牌Id
		List<Integer> brandIds = MyUtils.getBrandList(url);
		int brandId = brandIds.get(new Random().nextInt(brandIds.size()));

		String reqJson0 = "{" + MyUtils.getRandomToken() + reqJsons.get(0) + "}";
		reqJson0 = MyUtils.replaceIdinBackCommand(reqJson0, brandId);
		String response = MyUtils.sendPost(url, reqJson0);
		logger.info(response);
	}

	//获取推送消息
	public void dealsubscriptiongetUnRead() {
		List<String> reqJsons = (List<String>) reqData.get("dealsubscriptiongetUnRead");
		String token = MyUtils.getRandomToken();

		String reqJson0 = "{" + token + reqJsons.get(0) + "}";
		reqJson0 = MyUtils.replaceSince(reqJson0);
		String response = MyUtils.sendPost(url, reqJson0);
		logger.info(response);
	}

	//收藏列表
	public void dealdealgetFavDealList() {
		List<String> reqJsons = (List<String>) reqData.get("dealdealgetFavDealList");

		String reqJson0 = "{" + reqJsons.get(0) + "}";
		reqJson0 = MyUtils.replaceSince(reqJson0);
		String response = MyUtils.sendPost(url, reqJson0);
		logger.info(response);
	}

	//用户信息
	public void userinfo() {
		List<String> reqJsons = (List<String>) reqData.get("Userinfo");

		String reqJson0 = "{" + MyUtils.getRandomToken() + reqJsons.get(0) + "}";
		String response = MyUtils.sendPost(url, reqJson0);
		logger.info(response);
	}

	//用户登录
	public void userlogin() {
		//随机获取一个用户的信息
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

	//用户登出  为了不影响测试  登出后立即登录
	public void userlogout() {
		List<String> reqJsons = (List<String>) reqData.get("userlogout");
		//随机获取一个用户的信息
		Map<String, String> info = MyUtils.getRandomUserInfo();
		//用户登出
		String reqJson0 = "{" + info.get("token") + reqJsons.get(0) + "}";
		String response = MyUtils.sendPost(url, reqJson0);
		logger.info(response);

		//为了不影响测试,登出后登录
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

	//查询用户信息
	public void userprofile() {
		List<String> reqJsons = (List<String>) reqData.get("userprofile");
		int userId = Integer.parseInt((String) MyUtils.getRandomIdToken().get("id"));

		String reqJson0 = "{" + reqJsons.get(0) + "}";
		reqJson0 = MyUtils.replaceIdinBackCommand(reqJson0, userId);
		String response = MyUtils.sendPost(url, reqJson0);
		logger.info(response);
	}

	//取消关注用户
	public void userunfollow() {
		List<String> reqJsons = (List<String>) reqData.get("userunfollow");
		//拿到执行关注操作的用户id
		Map id_token = MyUtils.getRandomIdToken();
		int id = Integer.parseInt((String) id_token.get("id"));
		String token = (String) id_token.get("token");
		//拿到被关注用户的id
		int userId = MyUtils.getRandomUserId(id);

		// 获取关注用户指令
		List<String> reqJson1 = (List<String>) reqData.get("userfollow");
		String addJson = "{" + token + reqJson1.get(0) + "}";
		addJson = MyUtils.replaceIdinBackCommand(addJson, userId);
		// 发送关注用户的请求
		MyUtils.sendPost(url, addJson);
		//获取取消关注用户指令
		String reqJson0 = "{" + token + reqJsons.get(0) + "}";
		reqJson0 = MyUtils.replaceIdinBackCommand(reqJson0, userId);
		String response = MyUtils.sendPost(url, reqJson0);
		logger.info(response);
	}

	//发布晒单
	public void postcreate() {
		List<String> reqJsons = (List<String>) reqData.get("postcreate");
		String token = MyUtils.getRandomToken();
		String reqJson = "{" + token + reqJsons.get(4) + "}";

		// textMap用于存文本,fileMap用于存图片
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

	// 推送的deal列表
	public void dealsubscriptionmyAlerts() {
		List<String> reqJsons = (List<String>) reqData.get("dealsubscriptionmyAlerts");

		String reqJson0 = "{" + reqJsons.get(0) + "}";
		String response = MyUtils.sendPost(url, reqJson0);
		logger.info(response);
	}
}
