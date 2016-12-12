package cn.magicstudio.mblog.base.framework.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;

public class CustomDateEditorBase extends CustomDateEditor {
	private DateFormat dateFormat;
	private boolean allowEmpty;
	private int exactDateLength;

	public CustomDateEditorBase(DateFormat dateFormat, boolean allowEmpty) {
		super(dateFormat, allowEmpty);
		this.dateFormat = dateFormat;
		this.allowEmpty = allowEmpty;
		this.exactDateLength = -1;
	}

	public void setAsText(String text) throws IllegalArgumentException {
		if ((this.allowEmpty) && (StringUtils.isEmpty(StringUtils.trim(text)))) {
			setValue(null);
		} else {
			if ((text != null) && (this.exactDateLength >= 0)
					&& (text.length() != this.exactDateLength)) {
				throw new IllegalArgumentException(
						"Could not parse date: it is not exactly"
								+ this.exactDateLength + "characters long");
			}

			try {
				if (StringUtils.isEmpty(StringUtils.trim(text))) {
					setValue(null);
				} else {
					if (text.length() > 10) {
						this.dateFormat = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");
					} else {
						this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					}
					setValue(this.dateFormat.parse(text));
				}
			} catch (ParseException ex) {
				throw new IllegalArgumentException("Could not parse date: "
						+ ex.getMessage(), ex);
			}
		}
	}

	public String getAsText() {
		Date value = (Date) getValue();
		return value != null ? this.dateFormat.format(value) : "";
	}
}
