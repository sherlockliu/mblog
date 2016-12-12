package cn.magicstudio.mblog.data.trigger;

import java.util.List;

public abstract class RegexDbUpdateTrigger implements DbTrigger {
	private String id;
	private String type;
	private String express;
	private java.util.regex.Pattern regex;
	private List<String> exclude;

	public List<String> getExclude() {
		return this.exclude;
	}

	public void setExclude(List<String> exclude) {
		this.exclude = exclude;
	}

	public RegexDbUpdateTrigger(String express) {
		this.express = express;
		this.regex = java.util.regex.Pattern.compile(express, 2);
		this.exclude = new java.util.ArrayList();
	}

	public String getName() {
		return this.id;
	}

	public String getExpress() {
		return this.express;
	}

	public void setName(String val) {
		this.id = val;
	}

	public void setExpress(String val) {
		this.express = val;
	}

	public boolean match(Object... values) {
		if ((values == null) || (values.length == 0))
			return false;
		String type = values[0].toString();
		if (this.exclude.indexOf(type) == 0)
			return false;
		return this.regex.matcher(type).find();
	}
}