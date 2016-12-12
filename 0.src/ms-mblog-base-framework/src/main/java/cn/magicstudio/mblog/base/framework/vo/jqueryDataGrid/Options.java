package cn.magicstudio.mblog.base.framework.vo.jqueryDataGrid;

public class Options {
	private boolean required;
	private String url;
	private String valueField;
	private String textField;
	private String missingMessage;
	private int precision;

	public int getPrecision() {
		return this.precision;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}

	public boolean isRequired() {
		return this.required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getValueField() {
		return this.valueField;
	}

	public void setValueField(String valueField) {
		this.valueField = valueField;
	}

	public String getTextField() {
		return this.textField;
	}

	public void setTextField(String textField) {
		this.textField = textField;
	}

	public String getMissingMessage() {
		return this.missingMessage;
	}

	public void setMissingMessage(String missingMessage) {
		this.missingMessage = missingMessage;
	}
}
