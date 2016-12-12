package cn.magicstudio.mblog.utils.dynamic.compile;

import cn.wonhigh.retail.backend.utils.RegexUtils;

public class JavaSource {
	private String packageName;
	private String className;
	private String source;

	public JavaSource(String sourceString) {
		String className = RegexUtils.findFirst(sourceString,
				"public class (?s).*?{").split("extends")[0]
				.split("implements")[0].replaceAll("public class ", "")
				.replace("{", "").trim();
		String packageName = RegexUtils
				.findFirst(sourceString, "package (?s).*?;")
				.replaceAll("package ", "").replaceAll(";", "").trim();
		this.packageName = packageName;
		this.className = className;
		this.source = sourceString;
	}

	public JavaSource(String packageName, String className, String source) {
		this.packageName = packageName;
		this.className = className;
		this.source = source;
	}

	public String getPackageName() {
		return this.packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getClassName() {
		return this.className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String toString() {
		return this.packageName + "." + this.className;
	}
}
