package cn.magicstudio.mblog.base.framework.vo.jqueryDataGrid;

import org.codehaus.jackson.annotate.JsonAutoDetect;

import com.yougou.logistics.base.common.vo.jqueryDataGrid.Editor;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE)
public class JqueryDataGrid {
	private String field;
	private String title;
	private int width;
	private Editor editor;
	private int rowspan;
	private int colspan;
	private String align;
	private boolean readOnly;

	public boolean isReadOnly() {
		return this.readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public String getField() {
		return this.field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getWidth() {
		return this.width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public Editor getEditor() {
		return this.editor;
	}

	public void setEditor(Editor editor) {
		this.editor = editor;
	}

	public int getRowspan() {
		return this.rowspan;
	}

	public void setRowspan(int rowspan) {
		this.rowspan = rowspan;
	}

	public int getColspan() {
		return this.colspan;
	}

	public void setColspan(int colspan) {
		this.colspan = colspan;
	}

	public String getAlign() {
		return this.align;
	}

	public void setAlign(String align) {
		this.align = align;
	}
}
