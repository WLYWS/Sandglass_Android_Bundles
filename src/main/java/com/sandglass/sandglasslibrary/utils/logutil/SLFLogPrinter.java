//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.sandglass.sandglasslibrary.utils.logutil;

import android.text.TextUtils;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

final class SLFLogPrinter {
	private static final int DEBUG = 3;
	private static final int ERROR = 6;
	private static final int INFO = 4;
	private static final int VERBOSE = 2;
	private static final int WARN = 5;
	private static final char TOP_LEFT_CORNER = '╔';
	private static final char BOTTOM_LEFT_CORNER = '╚';
	private static final char MIDDLE_CORNER = '╟';
	private static final String HORIZONTAL_DOUBLE_LINE = "║ ";
	private static final String DOUBLE_DIVIDER = "═════════════════════════════════════════════════════════";
	private static final String SINGLE_DIVIDER = "─────────────────────────────────────────────────────────";
	private static final String TOP_BORDER = "╔═════════════════════════════════════════════════════════";
	private static final String BOTTOM_BORDER = "╚═════════════════════════════════════════════════════════";
	private static final String MIDDLE_BORDER = "╟─────────────────────────────────────────────────────────";
	private static final int CHUNK_SIZE = 4000;
	private static final int JSON_INDENT = 2;
	private static final int MIN_STACK_OFFSET = 3;
	private final ThreadLocal<Integer> localMethodCount = new ThreadLocal();
	private final SLFLogSettings mSettings = new SLFLogSettings();

	public SLFLogPrinter() {
	}

	public SLFLogSettings getSet() {
		return this.mSettings;
	}

	public SLFLogPrinter t(int methodCount) {
		this.localMethodCount.set(methodCount);
		return this;
	}

	public void e(String tag, Throwable ex) {
		this.log(tag, 6, this.throw2String(ex));
	}

	public void e(String tag, String message, Object... args) {
		this.log(tag, 6, this.createMessage(message, args));
	}

	public void d(String tag, String message, Object... args) {
		this.log(tag, 3, message, args);
	}

	public void w(String tag, String message, Object... args) {
		this.log(tag, 5, message, args);
	}

	public void i(String tag, String message, Object... args) {
		this.log(tag, 4, message, args);
	}

	public void v(String tag, String message, Object... args) {
		this.log(tag, 2, message, args);
	}

	public void json(String tag, String json) {
		if (this.mSettings.isLogOpen()) {
			if (TextUtils.isEmpty(json)) {
				this.i(tag, "Empty - json");
			} else {
				try {
					json = json.trim();
					String message;
					if (json.startsWith("{")) {
						JSONObject jsonObject = new JSONObject(json);
						message = jsonObject.toString(2);
						this.i(tag, message);
						return;
					}

					if (json.startsWith("[")) {
						JSONArray jsonArray = new JSONArray(json);
						message = jsonArray.toString(2);
						this.i(tag, message);
						return;
					}

					this.i(tag, "Invalid Json");
				} catch (JSONException var5) {
					this.e(tag, var5);
				}

			}
		}
	}

	public void xml(String tag, String xml) {
		if (this.mSettings.isLogOpen()) {
			if (TextUtils.isEmpty(xml)) {
				this.w(tag, "Empty - xml");
			} else {
				try {
					Source xmlInput = new StreamSource(new StringReader(xml));
					StreamResult xmlOutput = new StreamResult(new StringWriter());
					Transformer transformer = TransformerFactory.newInstance().newTransformer();
					transformer.setOutputProperty("indent", "yes");
					transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
					transformer.transform(xmlInput, xmlOutput);
					this.w(tag, xmlOutput.getWriter().toString().replaceFirst(">", ">\n"));
				} catch (TransformerException var6) {
					this.e(tag, "Invalid xml");
				}

			}
		}
	}

	private synchronized void log(String tag, int priority, String message, Object... args) {
		if (this.mSettings.isLogOpen()) {
			int methodCount = this.getMethodCount();
			tag = this.logHeaderContent(priority, tag, methodCount);
			if (TextUtils.isEmpty(message)) {
				message = "Empty/NULL log message";
			}

			message = this.createMessage(message, args);
			byte[] bytes = message.getBytes();
			int length = bytes.length;
			if (length <= 4000) {
				this.logContent(priority, tag, message);
			} else {
				for(int i = 0; i < length; i += 4000) {
					int count = Math.min(length - i, 4000);
					this.logContent(priority, tag, new String(bytes, i, count));
				}

			}
		}
	}

	private String createMessage(String message, Object... args) {
		return args != null && args.length != 0 ? String.format(message, args) : message;
	}

	private String logHeaderContent(int logType, String tag, int methodCount) {
		StackTraceElement[] trace = Thread.currentThread().getStackTrace();
		int stackOffset = this.getStackOffset(trace);
		if (methodCount + stackOffset > trace.length) {
			methodCount = trace.length - stackOffset - 1;
		}

		if (TextUtils.isEmpty(tag)) {
			tag = this.getSimpleClassName(trace[stackOffset + 1].getClassName());
		}

		if (this.mSettings.isShowTable()) {
			this.logChunk(logType, tag, "╔═════════════════════════════════════════════════════════");
		}

		if (this.mSettings.isShowThreadInfo()) {
			if (this.mSettings.isShowTable()) {
				this.logChunk(logType, tag, "║  Thread: " + Thread.currentThread().getName());
				this.logChunk(logType, tag, "╟─────────────────────────────────────────────────────────");
			} else {
				this.logChunk(logType, tag, "Thread: " + Thread.currentThread().getName());
			}
		}

		boolean isShowMethod = false;

		for(int i = methodCount; i > 0; --i) {
			int stackIndex = i + stackOffset;
			if (stackIndex < trace.length) {
				isShowMethod = true;
				StringBuilder builder = new StringBuilder();
				builder.append(this.getSimpleClassName(trace[stackIndex].getClassName())).append(".").append(trace[stackIndex].getMethodName()).append(" ").append(" (").append(trace[stackIndex].getFileName()).append(":").append(trace[stackIndex].getLineNumber()).append(")");
				if (this.mSettings.isShowTable()) {
					builder.insert(0, "║ ");
				}

				this.logChunk(logType, tag, builder.toString());
			}
		}

		if (this.mSettings.isShowTable() && isShowMethod) {
			this.logChunk(logType, tag, "╟─────────────────────────────────────────────────────────");
		}

		return tag;
	}

	private int getStackOffset(StackTraceElement[] trace) {
		for(int i = 3; i < trace.length; ++i) {
			StackTraceElement e = trace[i];
			String name = e.getClassName();
			if (!name.equals(SLFLogPrinter.class.getName()) && !name.equals(SLFLogUtil.class.getName())) {
				return i - 1;
			}
		}

		return -1;
	}

	private void logContent(int logType, String tag, String chunk) {
		String lineStr = System.getProperty("line.separator");
		if (lineStr != null) {
			String[] lines = chunk.split(lineStr);
			String[] var6 = lines;
			int var7 = lines.length;

			for(int var8 = 0; var8 < var7; ++var8) {
				String line = var6[var8];
				if (this.mSettings.isShowTable()) {
					line = "║ " + line;
				}

				this.logChunk(logType, tag, line);
			}
		}

		if (this.mSettings.isShowTable()) {
			this.logChunk(logType, tag, "╚═════════════════════════════════════════════════════════");
		}

	}

	private void logChunk(int logType, String tag, String chunk) {
		if (!TextUtils.isEmpty(chunk) && chunk.contains("http")) {
			chunk = chunk.replace("\\", "");
		}

		switch(logType) {
			case 2:
				this.mSettings.getLogAdapter().v(tag, chunk);
				break;
			case 3:
			default:
				this.mSettings.getLogAdapter().d(tag, chunk);
				break;
			case 4:
				this.mSettings.getLogAdapter().i(tag, chunk);
				break;
			case 5:
				this.mSettings.getLogAdapter().w(tag, chunk);
				break;
			case 6:
				this.mSettings.getLogAdapter().e(tag, chunk);
		}

	}

	private String getSimpleClassName(String name) {
		int lastIndex = name.lastIndexOf(46);
		return name.substring(lastIndex + 1).split("\\$")[0];
	}

	private String throw2String(Throwable ex) {
		if (null == ex) {
			return "";
		} else {
			Writer info = new StringWriter();
			PrintWriter printWriter = new PrintWriter(info);
			ex.printStackTrace(printWriter);

			for(Throwable cause = ex.getCause(); cause != null; cause = cause.getCause()) {
				cause.printStackTrace(printWriter);
			}

			printWriter.flush();
			return info.toString();
		}
	}

	private int getMethodCount() {
		Integer count = (Integer)this.localMethodCount.get();
		int result = this.mSettings.getMethodCount();
		if (count != null) {
			this.localMethodCount.remove();
			result = count;
		}

		if (result < 0) {
			throw new IllegalStateException("methodCount cannot be negative");
		} else {
			return result;
		}
	}
}
