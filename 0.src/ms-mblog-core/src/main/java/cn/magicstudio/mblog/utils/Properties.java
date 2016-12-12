package cn.magicstudio.mblog.utils;

import java.io.UnsupportedEncodingException;

public class Properties extends java.util.Properties {
	private static final long serialVersionUID = -2357393781665671357L;
	private static final String PROPERTIES_DEFAULT_ENCODING = "ISO-8859-1";
	private String defaultEncoding;

	public Properties() {
	}

	public Properties(String defaultEncoding) {
		this();
		this.defaultEncoding = defaultEncoding;
	}

	public Properties(Properties defaultProperties) {
		super(defaultProperties);
	}

	public Properties(Properties defaultProperties, String defaultEncoding) {
		this(defaultProperties);
		this.defaultEncoding = defaultEncoding;
	}

	public synchronized Object get(Object key) {
		Object ovalue = super.get(key);
		if ((ovalue != null) && (this.defaultEncoding != null)
				&& ((ovalue instanceof String))) {
			try {
				return new String(
						String.valueOf(ovalue).getBytes("ISO-8859-1"),
						this.defaultEncoding);
			} catch (UnsupportedEncodingException e) {
				return ovalue;
			}
		}

		return ovalue;
	}

	public synchronized Object getWithEncoding(String key, String encoding)
			throws UnsupportedEncodingException {
		Object ovalue = get(key);
		if ((ovalue != null) && (encoding != null)
				&& ((ovalue instanceof String))) {
			if (this.defaultEncoding == null) {
				return new String(
						String.valueOf(ovalue).getBytes("ISO-8859-1"), encoding);
			}
			return new String(String.valueOf(ovalue).getBytes(
					this.defaultEncoding), encoding);
		}

		return ovalue;
	}

	public String getProperty(String key) {
		String value = super.getProperty(key);
		if ((value != null) && (this.defaultEncoding != null)) {
			try {
				return new String(String.valueOf(value).getBytes("ISO-8859-1"),
						this.defaultEncoding);
			} catch (UnsupportedEncodingException e) {
				return value;
			}
		}
		return value;
	}

	public String getProperty(String key, String defaultValue) {
		String value = getProperty(key);
		return value == null ? defaultValue : value;
	}

	public String getPropertyWithEncoding(String key, String encoding)
			throws UnsupportedEncodingException {
		String value = getProperty(key);
		if ((value != null) && (encoding != null)) {
			if (this.defaultEncoding == null) {
				return new String(value.getBytes("ISO-8859-1"), encoding);
			}
			return new String(value.getBytes(this.defaultEncoding), encoding);
		}

		return value;
	}

	public String getPropertyWithEncoding(String key, String defaultValue,
			String encoding) throws UnsupportedEncodingException {
		String value = getPropertyWithEncoding(key, encoding);
		return value == null ? defaultValue : value;
	}
}
