package com.dealmoon.presstest.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.MimetypesFileTypeMap;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.pkcs.RSAPrivateKeyStructure;
import org.bouncycastle.util.encoders.Base64;

/**
 * @author: WY
 * @data:2015��4��3�� ����11:11:55
 * @description:������
 */
public class MyUtils {
	private final static String url = "http://api2.test.dealmoon.net";

	/**
	 * �滻�ַ����еĿհ��ַ�,���绻��/�ո�/�س���
	 * 
	 * @param strSource ���������ַ���
	 * @return ��������ַ���
	 */
	public static String removeBlank(String strSource) {
		return (strSource.replaceAll("\\s*", ""));
	}

	/**
	 * ���������ʽƥ���ַ���,ѡ��ĳ����
	 * 
	 * @param strSource Ҫƥ����ַ���
	 * @param pattern �������ʽ
	 * @param groupId ����ƥ�䣬ѡȡ�ĸ���
	 * @return ��Ӧ��Ľ���ַ���
	 */
	public static String matchString(String strSource, String pattern, int groupId) {
		Pattern _pattern = Pattern.compile(pattern);
		Matcher matcher = _pattern.matcher(strSource);
		String strResult = null;
		if (matcher.find()) {
			strResult = matcher.group(groupId);
		}
		return strResult;
	}

	/**
	 * MD5�����ַ������γ�32λ����
	 * 
	 * @param plainText �����ܵ��ַ���
	 * @return ���ܺ���ַ���
	 */
	public static String MD5(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(input.getBytes());
			BigInteger number = new BigInteger(1, messageDigest);
			String hashtext = number.toString(16);
			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext;
			}
			return hashtext;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * ���ַ���д���ļ���
	 * 
	 * @param fileName �ļ���
	 * @param strContent ��Ҫд�������
	 */
	public static void writeFile(String fileName, String strContent) {
		FileWriter fw = null;
		File file = new File(fileName);
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			fw = new FileWriter(file);
			BufferedWriter out = new BufferedWriter(fw);
			out.write(strContent, 0, strContent.length() - 1);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ���û���id��tokenд��pro/token.properties��
	 * 
	 * @param token token�����û�id,���ý�ȡ�ַ�������ʽ�õ�id
	 */
	public static void writeUserIdToken(String token) {
		String filename = "pro/token.properties";
		HashMap<String, String> map = new HashMap<String, String>();
		String[] id_token = token.split("\\|");
		map.put(id_token[0], token);
		PropertiesReader.proWriter(filename, map);
	}

	/**
	 * �����ӿ���Ӧʱ��ͳ���ļ�
	 * 
	 * @param fileName
	 */
	public static void createExcel(String fileName) {
		// ��һ��������һ��webbook����Ӧһ��Excel�ļ�
		HSSFWorkbook wb = new HSSFWorkbook();
		// �ڶ�������webbook������һ��sheet,��ӦExcel�ļ��е�sheet
		HSSFSheet sheet = wb.createSheet("�ӿ���Ӧʱ��");
		// ����������sheet�����ӱ�ͷ��0��,ע���ϰ汾poi��Excel����������������short
		HSSFRow row = sheet.createRow((int) 0);
		// ���Ĳ���������Ԫ�񣬲�����ֵ��ͷ ���ñ�ͷ����
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // ����һ�����и�ʽ

		HSSFCell cell = row.createCell(0);
		cell.setCellValue("�ӿ���");
		cell.setCellStyle(style);
		cell = row.createCell(1);
		cell.setCellValue("��Ӧʱ��");
		cell.setCellStyle(style);
		cell = row.createCell(2);
		// �����������ļ��浽ָ��λ��
		try {
			FileOutputStream fout = new FileOutputStream(fileName, true);
			wb.write(fout);
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��¼�ӿ���Ӧʱ�� ����,�������������߳�ͬʱ�õ��ļ�������������쳣
	 * 
	 * @param content
	 */
	public static synchronized void writeExcel(String[] content) {

		String fileName = "logs/statistics.xls";
		File file = new File(fileName);
		if (!file.exists()) {
			//����ļ��������򴴽��ļ�
			createExcel(fileName);
		}
		try {
			FileInputStream fs = new FileInputStream(file); //��ȡlogs/statistics.xls  
			POIFSFileSystem ps = new POIFSFileSystem(fs); //ʹ��POI�ṩ�ķ����õ�excel����Ϣ  
			HSSFWorkbook wb = new HSSFWorkbook(ps);
			HSSFSheet sheet = wb.getSheetAt(0); //��ȡ������������Ϊһ��excel�����ж��������  

			FileOutputStream out = new FileOutputStream(fileName); //��logs/statistics.xls��д����  
			HSSFRow row = sheet.createRow((short) (sheet.getLastRowNum() + 1)); //�������кź�׷������  
			row.createCell(0).setCellValue((String) content[0]); //���õ�һ������0��ʼ����Ԫ�������  
			row.createCell(1).setCellValue(Integer.parseInt(content[1])); //���õڶ�������0��ʼ����Ԫ�������  

			out.flush();
			wb.write(out);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * ��post�������ı����ϴ�ͼƬ��������
	 * 
	 * @param urlStr url��ַ
	 * @param textMap �ı�Map
	 * @param fileMap �ļ�Map
	 * @param requestData requestData
	 * @return responseData
	 */
	public static String postUpload(String urlStr, Map<String, String> textMap, Map<String, String> fileMap,
			String requestData) {
		long startTime = System.currentTimeMillis(); // ��ȡ��ʼʱ��
		String res = null;
		HttpURLConnection conn = null;
		// boundary����requestͷ���ϴ��ļ����ݵķָ���
		String BOUNDARY = "---------------------------123821742118716";
		try {
			urlStr = MyUtils.url + "/Post";
			URL url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setUseCaches(false);
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");

			// ����DMAuthorization��֤��Ϣ
			String DMAuthorization = MyUtils.MD5("deal" + requestData + "moon");
			conn.setRequestProperty("DMAuthorization", DMAuthorization);

			conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

			OutputStream out = new DataOutputStream(conn.getOutputStream());
			// �����ı�
			if (textMap != null) {
				StringBuffer strBuf = new StringBuffer();
				Iterator<?> iter = textMap.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					String inputName = (String) entry.getKey();
					String inputValue = (String) entry.getValue();
					if (inputValue == null) {
						continue;
					}
					strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"\r\n\r\n");
					strBuf.append(inputValue);
				}
				out.write(strBuf.toString().getBytes());
			}

			// �ϴ��ļ�
			if (fileMap != null) {
				Iterator iter = fileMap.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					String inputName = (String) entry.getKey();
					String inputValue = (String) entry.getValue();
					if (inputValue == null) {
						continue;
					}
					File file = new File(inputValue);
					String filename = file.getName();
					String contentType = new MimetypesFileTypeMap().getContentType(file);
					if (filename.endsWith(".png")) {
						contentType = "image/png";
					}
					if (contentType == null || contentType.equals("")) {
						contentType = "application/octet-stream";
					}

					StringBuffer strBuf = new StringBuffer();
					strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"; filename=\"" + filename
							+ "\"\r\n");
					strBuf.append("Content-Type:" + contentType + "\r\n\r\n");

					out.write(strBuf.toString().getBytes());

					DataInputStream in = new DataInputStream(new FileInputStream(file));
					int bytes = 0;
					byte[] bufferOut = new byte[1024];
					while ((bytes = in.read(bufferOut)) != -1) {
						out.write(bufferOut, 0, bytes);
					}
					// ����ʱ���,ʹ ����ͼƬ���ظ�
					out.write(String.valueOf(System.currentTimeMillis() + Math.random()).getBytes());
					in.close();
				}
			}

			byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
			out.write(endData);
			out.flush();
			out.close();

			if (conn.getResponseCode() == 200) {
				// ��ȡ��������
				StringBuffer strBuf = new StringBuffer();
				BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String line = null;
				while ((line = reader.readLine()) != null) {
					strBuf.append(line).append("\n");
				}
				res = strBuf.toString();
				reader.close();
				reader = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
		long endTime = System.currentTimeMillis(); // ��ȡ����ʱ��
		String[] content = { JSONObject.fromObject(requestData).getString("command"),
				String.valueOf(endTime - startTime) };
		//��¼�������󵽽��������ʱ��
		MyUtils.writeExcel(content);
		return res;
	}

	/**
	 * ��ָ�� URL ����POST����
	 * 
	 * @param url url ����� URL
	 * @param headers ͷ��Ϣ,��ֵ��,���Ϊ�ն�ȡĬ�ϵ�
	 * @param jsonData requestData
	 * @return responseData
	 */
	public static String sendPost(String url, String jsonData) {
		long startTime = System.currentTimeMillis(); // ��ȡ��ʼʱ��
		HttpURLConnection conn = null;
		StringBuffer sbf = new StringBuffer();
		try {
			url = MyUtils.url;
			URL realUrl = new URL(url);
			// ����http����
			conn = (HttpURLConnection) realUrl.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setUseCaches(false);

			// ����DMAuthorization��֤��Ϣ
			String DMAuthorization = MyUtils.MD5("deal" + jsonData + "moon");
			conn.setRequestProperty("DMAuthorization", DMAuthorization);
			conn.setRequestProperty("Accept-Charset", "UTF-8");
			conn.connect();

			// ��jsonData ���͵�������
			PrintWriter out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), "utf-8"));
			out.println(jsonData);
			out.flush();
			out.close();

			if (conn.getResponseCode() == 200) {
				// ��ȡ�ظ���Ϣ
				BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String lines;
				while ((lines = reader.readLine()) != null) {
					lines = new String(lines.getBytes(), "utf-8");
					sbf.append(lines);
				}
				reader.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
		long endTime = System.currentTimeMillis(); // ��ȡ����ʱ��
		String[] content = { JSONObject.fromObject(jsonData).getString("command"), String.valueOf(endTime - startTime) };
		//��¼�������󵽽ӿڷ������ݵ�ʱ��
		MyUtils.writeExcel(content);
		return sbf.toString();
	}

	/**
	 * �ж�id�Ƿ���һ��ĳ�ṹ��������
	 * 
	 * @param response ���󷵻�ֵ
	 * @param arrayName �ṹ������
	 * @param id ��Ҫ���id
	 * @return true ���� false ������
	 */
	public static boolean idInArray(String response, String arrayName, int id) {
		JSONArray array = JSONObject.fromObject(response).getJSONObject("responseData").getJSONArray(arrayName);
		int _id = -1;
		for (Iterator iterator = array.iterator(); iterator.hasNext();) {
			JSONObject obj = (JSONObject) iterator.next();
			if (obj.getInt("id") == id) {
				_id = id;
				break;
			}
		}
		return id == _id;
	}

	/**
	 * ��ȡ��Ӧ���͵�post�б�
	 * 
	 * @param url ����url
	 * @param type post����
	 * @param pageNum ҳ��
	 * @param pageSize ҳ���С
	 * @return
	 */
	public static List<Integer> getPostList(String url, String type, int pageNum, int pageSize) {
		Map<String, List<String>> reqData = PropertiesReader.getAppRequestData();
		List<String> reqJsons = (List<String>) reqData.get("postgetlist");
		List<Integer> ids = new ArrayList<Integer>();

		String token = MyUtils.getRandomToken();
		String reqJson;
		if (type.equals("follow")) {
			reqJson = "{" + token + reqJsons.get(0) + "}";
		} else {
			reqJson = "{" + reqJsons.get(0) + "}";
		}

		String commandInfo = JSONObject.fromObject(reqJson).getJSONObject("commandInfo").toString();
		reqJson = reqJson.replace(commandInfo, "{" + "\"type\":\"" + type + "\",\"pageNum\":" + pageNum
				+ ",\"pageSize\":" + pageSize + "}");
		String response = MyUtils.sendPost(url, reqJson);
		JSONArray array = JSONObject.fromObject(response).getJSONObject("responseData").getJSONArray("posts");
		for (Iterator iterator = array.iterator(); iterator.hasNext();) {
			JSONObject obj = (JSONObject) iterator.next();
			ids.add(obj.getInt("id"));
		}
		return ids;
	}

	/**
	 * ���ݴ����postId��ѯpost��Ϣ
	 * 
	 * @param url
	 * @param poatId
	 */
	public static String getPostInfo(String url, int postId) {
		Map<String, List<String>> reqData = PropertiesReader.getAppRequestData();
		List<String> reqJsons = (List<String>) reqData.get("postinfo");

		String reqJson0 = "{" + reqJsons.get(0) + "}";
		JSONObject obj = JSONObject.fromObject(reqJson0).getJSONObject("commandInfo");
		reqJson0 = reqJson0.replace(obj.toString(), "{\"id\":" + postId + "}");

		return MyUtils.sendPost(url, reqJson0);
	}

	/**
	 * ��ȡһ�������token
	 * 
	 * @return
	 */
	public static String getRandomToken() {
		Map<String, String> user_token = PropertiesReader.getTokens();
		String[] tokens = user_token.values().toArray(new String[0]);
		Random random = new Random();
		return "\"token\":" + "\"" + tokens[random.nextInt(tokens.length - 1)] + "\",";
	}

	/**
	 * ��ȡһ�������token,��ȥָ��user��Ӧ��token
	 * 
	 * @param userId
	 * @return
	 */
	public static String getRandomToken(int userId) {
		Map<String, String> user_token = PropertiesReader.getTokens();
		user_token.remove(userId);
		String[] tokens = user_token.values().toArray(new String[0]);

		Random random = new Random();
		return "\"token\":" + "\"" + tokens[random.nextInt(tokens.length - 1)] + "\",";
	}

	/**
	 * ��ȡһ�������id,��ȥָ��user��Ӧ��id
	 * 
	 * @param userId
	 * @return
	 */
	public static int getRandomUserId(int userId) {
		Map<String, String> user_token = PropertiesReader.getTokens();
		user_token.remove(userId);
		String[] ids = user_token.keySet().toArray(new String[0]);

		Random random = new Random();
		return Integer.parseInt(ids[random.nextInt(ids.length - 1)]);

	}

	/**
	 * ����õ�һ���û���id��token
	 * 
	 * @return
	 */
	public static Map<String, String> getRandomIdToken() {
		Map<String, String> user_token = PropertiesReader.getTokens();
		String[] userIds = user_token.keySet().toArray(new String[0]);
		String userId = userIds[new Random().nextInt(userIds.length - 1)];
		String userToken = user_token.get(userId);
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", userId);
		map.put("token", "\"token\":" + "\"" + userToken + "\",");

		return map;
	}

	/**
	 * ��ȡһ������û�����Ϣ,����������
	 * 
	 * @return
	 */
	public static Map<String, String> getRandomUserInfo() {
		Map<String, String> info = new HashMap<String, String>();

		Map<String, List<String>> reqData = PropertiesReader.getAppRequestData();
		List<String> reqJsons = (List<String>) reqData.get("Userinfo");
		String token = getRandomToken();
		String reqJson0 = "{" + token + reqJsons.get(0) + "}";
		String response = MyUtils.sendPost(url, reqJson0);
		JSONObject userInfo = JSONObject.fromObject(response).getJSONObject("responseData").getJSONObject("userInfo");
		String id = userInfo.getString("id");
		String name = userInfo.getString("name");
		String email = userInfo.getString("email");

		info.put("id", id);
		info.put("token", token);
		info.put("name", name);
		info.put("email", email);
		return info;
	}

	/**
	 * app�滻commandInfo�е�id
	 * 
	 * @param reqJson
	 * @param newId
	 * @return
	 */
	public static String replaceIdinAppCommand(String reqString, int newId) {
		int oldId = JSONObject.fromObject("{" + reqString + "}").getJSONObject("commandInfo").getInt("id");
		return reqString.replace("\"id\":" + oldId + ",", "\"id\":" + newId + ",");
	}

	/**
	 * ��̨�滻commandInfo�е�id
	 * 
	 * @param reqJson
	 * @param newId
	 * @return
	 */
	public static String replaceIdinBackCommand(String reqJson, int newId) {
		int oldId = JSONObject.fromObject(reqJson).getJSONObject("commandInfo").getInt("id");
		return reqJson.replace("\"id\":" + oldId, "\"id\":" + newId);
	}

	/**
	 * �滻getpostlikelist��userId
	 * 
	 * @param reqJson
	 * @param userId
	 * @return
	 */
	public static String replaceUserid(String reqJson, int userId) {
		int oldId = JSONObject.fromObject(reqJson).getJSONObject("commandInfo").getInt("userId");
		return reqJson.replace("\"userId\":" + oldId, "\"userId\":" + userId);
	}

	/**
	 * �滻deal/subscription/getUnRead��since
	 * 
	 * @param reqJson
	 * @return
	 */
	public static String replaceSince(String reqJson) {
		String commandInfo = JSONObject.fromObject(reqJson).getJSONObject("commandInfo").toString();
		reqJson = reqJson.replace(commandInfo, "{\"since\":\"" + Math.round(System.currentTimeMillis() / 1000) + "\"}");
		return reqJson;
	}

	/**
	 * �滻getmessagelist��messageId
	 * 
	 * @param reqJson
	 * @param userId
	 * @return
	 */
	public static String replaceMessageid(String reqJson, int messageId) {
		String commandInfo = JSONObject.fromObject(reqJson).getJSONObject("commandInfo").toString();
		return reqJson.replace(commandInfo, "{\"id\":[" + messageId + "]}");
	}

	/**
	 * ��ȡƷ���б�
	 * 
	 * @param url
	 * @return
	 */
	public static List<Integer> getBrandList(String url) {
		Map<String, List<String>> reqData = PropertiesReader.getAppRequestData();
		List<String> reqJsons = (List<String>) reqData.get("brandlist");
		List<Integer> ids = new ArrayList<Integer>();

		String reqJson = "{" + reqJsons.get(1) + "}";
		String response = MyUtils.sendPost(url, reqJson);
		JSONArray array = JSONObject.fromObject(response).getJSONObject("responseData").getJSONArray("brands");

		for (Iterator iterator = array.iterator(); iterator.hasNext();) {
			JSONObject obj = (JSONObject) iterator.next();
			ids.add(obj.getInt("id"));
		}
		return ids;
	}

	/**
	 * ��ȡ�û���Ϣ�б�
	 * 
	 * @param url
	 * @return
	 */
	public static List<Integer> getMessageList(String url, String token) {
		Map<String, List<String>> reqData = PropertiesReader.getAppRequestData();
		List<String> reqJsons = (List<String>) reqData.get("messagegetlist");
		List<Integer> ids = new ArrayList<Integer>();
		String reqJson = "{" + token + reqJsons.get(2) + "}";
		String response = MyUtils.sendPost(url, reqJson);
		JSONArray array = JSONObject.fromObject(response).getJSONObject("responseData").getJSONArray("messages");

		for (Iterator iterator = array.iterator(); iterator.hasNext();) {
			JSONObject obj = (JSONObject) iterator.next();
			ids.add(obj.getInt("id"));
		}
		return ids;
	}

	/**
	 * ���ַ����м��ع�Կ
	 * 
	 * @param publicKeyStr ��Կ�����ַ���
	 * @throws Exception ���ع�Կʱ�������쳣
	 */
	public static RSAPublicKey loadPublicKey(String publicKeyStr) throws Exception {
		try {
			byte[] buffer = Base64.decode(publicKeyStr);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
			return (RSAPublicKey) keyFactory.generatePublic(keySpec);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("�޴��㷨");
		} catch (InvalidKeySpecException e) {
			throw new Exception("��Կ�Ƿ�");
		} catch (NullPointerException e) {
			throw new Exception("��Կ����Ϊ��");
		}
	}

	/**
	 * ���ļ��м���˽Կ
	 * 
	 * @param keyFileName ˽Կ�ļ���
	 * @return �Ƿ�ɹ�
	 * @throws Exception
	 */
	public static RSAPrivateKey loadPrivateKey(InputStream in) throws Exception {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String readLine = null;
			StringBuilder sb = new StringBuilder();
			while ((readLine = br.readLine()) != null) {
				if (readLine.charAt(0) == '-') {
					continue;
				} else {
					sb.append(readLine);
					sb.append('\r');
				}
			}
			return loadPrivateKey(sb.toString());
		} catch (IOException e) {
			throw new Exception("˽Կ���ݶ�ȡ����");
		} catch (NullPointerException e) {
			throw new Exception("˽Կ������Ϊ��");
		}
	}

	public static RSAPrivateKey loadPrivateKey(String privateKeyStr) throws Exception {

		try {
			byte[] buffer = Base64.decode(privateKeyStr);
			RSAPrivateKeyStructure asn1PrivKey = new RSAPrivateKeyStructure(
					(ASN1Sequence) ASN1Sequence.fromByteArray(buffer));
			RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(asn1PrivKey.getModulus(),
					asn1PrivKey.getPrivateExponent());
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("�޴��㷨");
		} catch (InvalidKeySpecException e) {
			throw new Exception("˽Կ�Ƿ�");
		} catch (NullPointerException e) {
			throw new Exception("˽Կ����Ϊ��");
		}
	}

	/**
	 * ���ܹ���
	 * 
	 * @param plainTextData ��������
	 * @return
	 * @throws Exception ���ܹ����е��쳣��Ϣ
	 */
	public static byte[] encrypt(byte[] plainTextData) throws Exception {
		String _publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC/ucgTqTXbHwGQHl5k+jImN48L" + "\r"
				+ "q/meEm9GIZ/BRWeE7OgavWTVVc/EAe4bJ8DGHdSeIazG8LmsUohuedHx6Q2eYdwa" + "\r"
				+ "MEQWS/G0GKL5U+85GMv4QHK20e6bDy+BdqmNGNNBQUNBnFTca/GqOYMPEhKGuRdj" + "\r"
				+ "YK18MuZzDsk1XU9PhQIDAQAB" + "\r";
		RSAPublicKey publicKey = loadPublicKey(_publicKey);
		if (publicKey == null) {
			throw new Exception("���ܹ�ԿΪ��, ������");
		}
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			byte[] output = cipher.doFinal(plainTextData);
			return output;
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("�޴˼����㷨");
		} catch (NoSuchPaddingException e) {
			return null;
		} catch (InvalidKeyException e) {
			throw new Exception("���ܹ�Կ�Ƿ�,����");
		} catch (IllegalBlockSizeException e) {
			throw new Exception("���ĳ��ȷǷ�");
		} catch (BadPaddingException e) {
			throw new Exception("������������");
		}
	}

	/**
	 * ���ܹ���
	 * 
	 * @param privateKey ˽Կ
	 * @param cipherData ��������
	 * @return ����
	 * @throws Exception ���ܹ����е��쳣��Ϣ
	 */
	public static byte[] decrypt(byte[] cipherData) throws Exception {
		InputStream inputStream = new FileInputStream(new File("pro/rsa_private_key.pem"));
		RSAPrivateKey privateKey = loadPrivateKey(inputStream);
		if (privateKey == null) {
			throw new Exception("����˽ԿΪ��, ������");
		}
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("RSA");

			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] output = cipher.doFinal(cipherData);
			return output;
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("�޴˽����㷨");
		} catch (NoSuchPaddingException e) {
			return null;
		} catch (InvalidKeyException e) {
			throw new Exception("����˽Կ�Ƿ�,����");
		} catch (IllegalBlockSizeException e) {
			throw new Exception("���ĳ��ȷǷ�");
		} catch (BadPaddingException e) {
			throw new Exception("������������");
		}
	}
}