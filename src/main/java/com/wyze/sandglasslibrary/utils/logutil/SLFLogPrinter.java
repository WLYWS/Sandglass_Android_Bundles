package com.wyze.sandglasslibrary.utils.logutil;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * Created by yangjie
 * describe:用于输出格式化的log
 * time: 2022/12/10
 */
final class SLFLogPrinter {

	private static final int DEBUG = 3;
	private static final int ERROR = 6;
	private static final int INFO = 4;
	private static final int VERBOSE = 2;
	private static final int WARN = 5;

	// 制表格
	private static final char TOP_LEFT_CORNER = '╔';
	private static final char BOTTOM_LEFT_CORNER = '╚';
	private static final char MIDDLE_CORNER = '╟';
	private static final String HORIZONTAL_DOUBLE_LINE = "║ ";
	private static final String DOUBLE_DIVIDER = "═════════════════════════════════════════════════════════";
	private static final String SINGLE_DIVIDER = "─────────────────────────────────────────────────────────";
	private static final String TOP_BORDER = TOP_LEFT_CORNER + DOUBLE_DIVIDER;
	private static final String BOTTOM_BORDER = BOTTOM_LEFT_CORNER + DOUBLE_DIVIDER;
	private static final String MIDDLE_BORDER = MIDDLE_CORNER + SINGLE_DIVIDER;

	/**
	 * log最大一次输出4076 bytes,，则设置一次最多输出4000bytes
	 */
	private static final int CHUNK_SIZE = 4000;

	/**
	 * json输出格式tab数
	 */
	private static final int JSON_INDENT = 2;

	/**
	 * 最小堆栈帖数
	 */
	private static final int MIN_STACK_OFFSET = 3;

	/**
	 * Localize single mTag and method count for each thread
	 */
	private final ThreadLocal<Integer> localMethodCount = new ThreadLocal<>();

	/**
	 * log 输出配置对象
	 */
	private final SLFLogSettings mSettings = new SLFLogSettings();

	public SLFLogPrinter() {
		//
	}

	/**
	 * 初始化
	 */
	public SLFLogSettings getSet() {
		return mSettings;
	}

	public SLFLogPrinter t(int methodCount) {
		localMethodCount.set(methodCount);
		return this;
	}

	public void e(String tag, Throwable ex) {
		log(tag, ERROR, throw2String(ex));
	}

	public void e(String tag, String message, Object... args) {
		log(tag, ERROR, createMessage(message, args));
	}

	public void d(String tag, String message, Object... args) {
		log(tag, DEBUG, message, args);
	}

	public void w(String tag, String message, Object... args) {
		log(tag, WARN, message, args);
	}

	public void i(String tag, String message, Object... args) {
		log(tag, INFO, message, args);
	}

	public void v(String tag, String message, Object... args) {
		log(tag, VERBOSE, message, args);
	}

	public void json(String tag, String json) {
		if (!mSettings.isLogOpen()) {
			return;
		}

		if (TextUtils.isEmpty(json)) {
			i(tag, "Empty - json");
			return;
		}
		try {
			json = json.trim();
			if (json.startsWith("{")) {
				JSONObject jsonObject = new JSONObject(json);
				String message = jsonObject.toString(JSON_INDENT);
				i(tag, message);
				return;
			}
			if (json.startsWith("[")) {
				JSONArray jsonArray = new JSONArray(json);
				String message = jsonArray.toString(JSON_INDENT);
				i(tag, message);
				return;
			}
			i(tag, "Invalid Json");
		} catch (JSONException e) {
			e(tag, e);
		}
	}

	public void xml(String tag, String xml) {
		if (!mSettings.isLogOpen()) {
			return;
		}

		if (TextUtils.isEmpty(xml)) {
			w(tag, "Empty - xml");
			return;
		}
		try {
			Source xmlInput = new StreamSource(new StringReader(xml));
			StreamResult xmlOutput = new StreamResult(new StringWriter());
			@SuppressWarnings("java:S2755")
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			transformer.transform(xmlInput, xmlOutput);
			w(tag, xmlOutput.getWriter().toString().replaceFirst(">", ">\n"));
		} catch (TransformerException e) {
			e(tag, "Invalid xml");
		}
	}

	private synchronized void log(String tag, int priority, String message, Object... args) {
		if (!mSettings.isLogOpen()) {
			return;
		}

		int methodCount = getMethodCount();
		tag = logHeaderContent(priority, tag, methodCount);

		if (TextUtils.isEmpty(message)) {
			message = "Empty/NULL log message";
		}
		message = createMessage(message, args);
		byte[] bytes = message.getBytes();
		int length = bytes.length;
		if (length <= CHUNK_SIZE) {
			logContent(priority, tag, message);
			return;
		}

		for (int i = 0; i < length; i += CHUNK_SIZE) {
			int count = Math.min(length - i, CHUNK_SIZE);
			logContent(priority, tag, new String(bytes, i, count));
		}
	}

	private String createMessage(String message, Object... args) {
		return args == null || args.length == 0 ? message : String.format(message, args);
	}

	private String logHeaderContent(int logType, String tag, int methodCount) {

		StackTraceElement[] trace = Thread.currentThread().getStackTrace();
		int stackOffset = getStackOffset(trace);

		if (methodCount + stackOffset > trace.length) {
			methodCount = trace.length - stackOffset - 1;
		}

		if (TextUtils.isEmpty(tag)) {
			tag = getSimpleClassName(trace[stackOffset + 1].getClassName());
		}
		if (mSettings.isShowTable()) {
			logChunk(logType, tag, TOP_BORDER);
		}

		if (mSettings.isShowThreadInfo()) {
			if (mSettings.isShowTable()) {
				logChunk(logType, tag, HORIZONTAL_DOUBLE_LINE + " Thread: " + Thread.currentThread().getName());
				logChunk(logType, tag, MIDDLE_BORDER);
			} else {
				logChunk(logType, tag, "Thread: " + Thread.currentThread().getName());
			}
		}

		boolean isShowMethod = false;
		for (int i = methodCount; i > 0; i--) {
			int stackIndex = i + stackOffset;
			if (stackIndex >= trace.length) {
				continue;
			}
			isShowMethod = true;
			StringBuilder builder = new StringBuilder();
			builder.append(getSimpleClassName(trace[stackIndex].getClassName())).append(".")
					.append(trace[stackIndex].getMethodName()).append(" ").append(" (")
					.append(trace[stackIndex].getFileName()).append(":").append(trace[stackIndex].getLineNumber())
					.append(")");
			if (mSettings.isShowTable()) {
				builder.insert(0, HORIZONTAL_DOUBLE_LINE);
			}
			logChunk(logType, tag, builder.toString());
		}
		if (mSettings.isShowTable() && isShowMethod) {
			logChunk(logType, tag, MIDDLE_BORDER);
		}
		return tag;
	}

	/**
	 * 过滤LogUtil、LogPrinter堆栈帧
	 *
	 * @param trace
	 *            堆栈
	 * @return 堆栈帧数
	 */
	private int getStackOffset(StackTraceElement[] trace) {
		for (int i = MIN_STACK_OFFSET; i < trace.length; i++) {
			StackTraceElement e = trace[i];
			String name = e.getClassName();
			if (!name.equals(SLFLogPrinter.class.getName()) && !name.equals(SLFLogUtil.class.getName())) {
				return i-1;
			}
		}
		return -1;
	}

	private void logContent(int logType, String tag, String chunk) {
		String lineStr = System.getProperty("line.separator");
		if(lineStr != null){
			String[] lines = chunk.split(lineStr);// 换行符
			for (String line : lines) {
				if (mSettings.isShowTable()) {
					line = HORIZONTAL_DOUBLE_LINE + line;
				}
				logChunk(logType, tag, line);
			}
		}

		if (mSettings.isShowTable()) {
			logChunk(logType, tag, BOTTOM_BORDER);

		}
	}

	private void logChunk(int logType, String tag, String chunk) {
		if(!TextUtils.isEmpty(chunk)&&chunk.contains("http")){
			chunk = chunk.replace("\\","");
		}
		switch (logType) {
			case ERROR:
				mSettings.getLogAdapter().e(tag, chunk);
				break;
			case INFO:
				mSettings.getLogAdapter().i(tag, chunk);
				break;
			case VERBOSE:
				mSettings.getLogAdapter().v(tag, chunk);
				break;
			case WARN:
				mSettings.getLogAdapter().w(tag, chunk);
				break;
			case DEBUG:
			default:
				mSettings.getLogAdapter().d(tag, chunk);
				break;
		}
	}

	private String getSimpleClassName(String name) {
		int lastIndex = name.lastIndexOf('.');
		return name.substring(lastIndex + 1).split("\\$")[0];
	}

	private String throw2String(Throwable ex) {
		if (null == ex) {
			return "";
		}
		Writer info = new StringWriter();
		PrintWriter printWriter = new PrintWriter(info);
		ex.printStackTrace(printWriter);

		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.flush();
		return info.toString();
	}

	private int getMethodCount() {
		Integer count = localMethodCount.get();
		int result = mSettings.getMethodCount();
		if (count != null) {
			localMethodCount.remove();
			result = count;
		}
		if (result < 0) {
			throw new IllegalStateException("methodCount cannot be negative");
		}
		return result;
	}

}
