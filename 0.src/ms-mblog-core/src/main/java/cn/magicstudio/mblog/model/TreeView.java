package cn.magicstudio.mblog.model;

import java.util.HashMap;
import java.util.Map;

public class TreeView {
	
	private String id;
	private String text;
	private int checked;
	private String parentId;
	private Map<String, Object> attributes = new HashMap();

	private String state;

	private int level;

	private String path;

	private Integer hasChildren;

	public Integer getHasChildren() {
		return this.hasChildren;
	}

	public void setHasChildren(Integer hasChildren) {
		this.hasChildren = hasChildren;
	}

	public String getText() {
		return this.text;
	}

	public int getLevel() {
		return this.level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getChecked() {
		return this.checked;
	}

	public void setChecked(int checked) {
		this.checked = checked;
	}

	public Map<String, Object> getAttributes() {
		return this.attributes;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return this.parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}
}
