package com.dealmoon.presstest.utils;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

public class PropertiesUtil extends Properties {
	private static final long serialVersionUID = 1L;

	private ListEnumerationAdapter<Object> keyList = new ListEnumerationAdapter<Object>();

	/**
	 * Ĭ�Ϲ��췽��
	 */
	public PropertiesUtil() {

	}

	/**
	 * ��ָ��·��������Ϣ��Properties
	 * 
	 * @param path
	 */
	public PropertiesUtil(String path) {
		try {
			InputStream is = new FileInputStream(path);
			this.load(is);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("ָ���ļ������ڣ�");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��дput����������property�Ĵ���˳�򱣴�key��keyList�������ظ��ĺ��߽�����ǰ�ߡ�
	 */
	public synchronized Object put(Object key, Object value) {
		if (keyList.contains(key)) {
			keyList.remove(key);
		}
		keyList.add(key);
		return super.put(key, value);
	}

	/**
	 * ��ȡProperties��key�����򼯺�
	 * 
	 * @return
	 */
	public List<Object> getKeyList() {
		return keyList;
	}

	/**
	 * ����Properties��ָ���ļ���Ĭ��ʹ��UTF-8����
	 * 
	 * @param path ָ���ļ�·��
	 */
	public void store(String path) {
		this.store(path, "UTF-8");
	}

	/**
	 * ����Properties��ָ���ļ�����ָ����Ӧ��ű���
	 * 
	 * @param path ָ��·��
	 * @param charset �ļ�����
	 */
	public void store(String path, String charset) {
		if (path != null && !"".equals(path)) {
			try {
				OutputStream os = new FileOutputStream(path);
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, charset));
				this.store(bw, null);
				bw.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			throw new RuntimeException("�洢·������Ϊ��!");
		}
	}

	/**
	 * ��дkeys����
	 */
	@Override
	public synchronized Enumeration<Object> keys() {
		keyList.reset();
		return keyList;
	}

	/**
	 * ArrayList��Enumeration��������
	 */
	private static class ListEnumerationAdapter<T> extends ArrayList<T> implements Enumeration<T> {
		private static final long serialVersionUID = 1L;
		private int index = 0;

		public boolean hasMoreElements() {
			return index < this.size();
		}

		public T nextElement() {
			if (this.hasMoreElements()) {
				return this.get(index++);
			}
			return null;
		}

		/**
		 * ����index��ֵΪ0��ʹ��Enumeration���Լ�����ͷ��ʼ����
		 */
		public void reset() {
			this.index = 0;
		}
	}
}
