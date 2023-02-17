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

	public SLFLogSettings() {
	}

	public SLFLogSettings showThreadInfo() {
		this.showThreadInfo = true;
		return this;
	}

	public SLFLogSettings showTable() {
		this.showTable = true;
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
		return this.methodCount;
	}

	public boolean isShowThreadInfo() {
		return this.showThreadInfo;
	}

	public boolean isShowTable() {
		return this.showTable;
	}

	public boolean isLogOpen() {
		return this.isLogOpen;
	}

	public SLFLogAdapter getLogAdapter() {
		if (this.logAdapter == null) {
			this.logAdapter = new SLFLogAdapter();
		}

		return this.logAdapter;
	}
}
