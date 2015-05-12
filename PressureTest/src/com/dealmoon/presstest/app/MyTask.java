package com.dealmoon.presstest.app;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.dealmoon.presstest.utils.Task;

public class MyTask extends Task {

	private Class claz;
	private String methodName;

	public MyTask() {

	}

	public MyTask(Class claz,String methodName) {
		this.claz = claz;
		this.methodName = methodName;
	}

	public void run() {
		try {
			Pressure pressure = (Pressure) claz.newInstance();
			Method m = claz.getDeclaredMethod(methodName, null);
			m.invoke(pressure);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
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
