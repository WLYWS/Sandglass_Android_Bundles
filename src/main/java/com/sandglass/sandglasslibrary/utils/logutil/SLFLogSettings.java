package com.sandglass.sandglasslibrary.utils.logutil;
/**
 * Created by yangjie
 * describe:log输出格式设置
 * time: 2022/12/10
 */
public final class SLFLogSettings {

	private int methodCount = 0;
	private boolean showThreadInfo = false;
	private boolean showTable = false;
	private SLFLogAdapter logAdapter;
	private boolean isLogOpen = true;

	public SLFLogSettings showThreadInfo() {
		showThreadInfo = true;
		return this;
	}

	public SLFLogSettings showTable() {
		showTable = true;
		return this;
	}

	public SLFLogSettings setIsLogOpen(boolean isLogOpen) {
		this.isLogOpen = isLogOpen;
		return this;
	}

	public SLFLogSettings methodCount(int methodCount) {
		if (methodCount < 0) {
			methodCount = 0;
		}
		this.methodCount = methodCount;
		return this;
	}

	public SLFLogSettings logAdapter(SLFLogAdapter logAdapter) {
		this.logAdapter = logAdapter;
		return this;
	}

	public int getMethodCount() {
		return methodCount;
	}

	public boolean isShowThreadInfo() {
		return showThreadInfo;
	}

	public boolean isShowTable() {
		return showTable;
	}
	public boolean isLogOpen() {
		return isLogOpen;
	}

	public SLFLogAdapter getLogAdapter() {
		if (logAdapter == null) {
			logAdapter = new SLFLogAdapter();
		}
		return logAdapter;
	}
}
