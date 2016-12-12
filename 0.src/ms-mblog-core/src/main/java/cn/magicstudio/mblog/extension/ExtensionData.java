package cn.magicstudio.mblog.extension;

import java.io.Serializable;
import org.apache.commons.lang.StringUtils;

public class ExtensionData implements Serializable, Comparable<ExtensionData> {
	private static final long serialVersionUID = 5697237591471377596L;
	private ExtensionDataType extensionDataType;
	private String clazzPath;
	private String sourceText;
	private Long timestamp;

	public ExtensionData() {
		this.timestamp = Long.valueOf(System.currentTimeMillis());
	}

	public ExtensionDataType getExtensionDataType() {
		return this.extensionDataType;
	}

	public void setExtensionDataType(ExtensionDataType extensionDataType) {
		this.extensionDataType = extensionDataType;
	}

	public String getClazzPath() {
		return this.clazzPath;
	}

	public void setClazzPath(String clazzPath) {
		this.clazzPath = clazzPath;
	}

	public String getSourceText() {
		return this.sourceText;
	}

	public void setSourceText(String sourceText) {
		this.sourceText = sourceText;
	}

	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public boolean isBlank() {
		return !isNotBlank();
	}

	public boolean isNotBlank() {
		return (StringUtils.isNotBlank(this.clazzPath))
				|| (StringUtils.isNotBlank(this.sourceText));
	}

	public int compareTo(ExtensionData o) {
		if (this.timestamp.longValue() < o.getTimestamp().longValue())
			return -1;
		if (this.timestamp.longValue() > o.getTimestamp().longValue()) {
			return 1;
		}
		return 0;
	}

	public int hashCode() {
		int prime = 31;
		int result = 1;
		result = 31 * result
				+ (this.clazzPath == null ? 0 : this.clazzPath.hashCode());
		result = 31
				* result
				+ (this.extensionDataType == null ? 0 : this.extensionDataType
						.hashCode());
		result = 31 * result
				+ (this.sourceText == null ? 0 : this.sourceText.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExtensionData other = (ExtensionData) obj;
		if (this.clazzPath == null) {
			if (other.clazzPath != null)
				return false;
		} else if (!this.clazzPath.equals(other.clazzPath))
			return false;
		if (this.extensionDataType != other.extensionDataType)
			return false;
		if (this.sourceText == null) {
			if (other.sourceText != null)
				return false;
		} else if (!this.sourceText.equals(other.sourceText))
			return false;
		return true;
	}
 }
