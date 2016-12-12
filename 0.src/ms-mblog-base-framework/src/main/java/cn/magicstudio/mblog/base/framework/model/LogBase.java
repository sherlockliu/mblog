/*     */package cn.magicstudio.mblog.base.framework.model;

/*     */

/*     */import java.io.IOException;
/*     */
import java.text.ParseException;
/*     */
import java.text.SimpleDateFormat;
/*     */
import java.util.Date;
/*     */
import javax.validation.constraints.NotNull;


import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import cn.magicstudio.mblog.base.framework.model.log.BusinessDataLog;
import cn.magicstudio.mblog.base.framework.vo.BaseDto;
import cn.magicstudio.mblog.base.frameworkn.enums.log.JsonType;
import cn.magicstudio.mblog.base.frameworkn.enums.log.LogType;
import cn.magicstudio.mblog.base.frameworkn.enums.log.OperType;
import cn.magicstudio.mblog.base.frameworkn.enums.log.SystemCode;

public abstract class LogBase extends BaseDto {
	private static final long serialVersionUID = -1921385400568858250L;
	private int id;
	@NotNull(message = "操作人不能为空!")
	private int operId;
	@NotNull(message = "操作人姓名不能为空!")
	private String operName;
	@NotNull(message = "操作人类型不能为空!")
	private OperType operType;
	@NotNull(message = "日志类型不能为空!")
	private LogType logType;
	private int targetId;
	private String targetCode;
	private int targetType;
	private String content;
	private String ip;
	private String remark;
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserialize.class)
	private Date createTime;
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserialize.class)
	private Date updateTime;
	@NotNull(message = "系统编码不能为空!")
	private SystemCode systemCode;
	private String tablesName;
	@NotNull(message = "Json内容类型不能为空!")
	private JsonType jsonType;

	public LogBase() {
		this.createTime = new Date();
		this.updateTime = this.createTime;
	}

	public static LogBase getLog(SystemCode systemCode, int targetType) {
		switch (systemCode) {
		case UC:
		case MDM:
		case GMS:
		case PMS:
		case FAS:
		case POS:
		case MPS:
		case OC:
			return new BusinessDataLog(systemCode, targetType);
		}

		return null;
	}

	public LogBase bulidOperInfo(int operId, String operName, String ip) {
		this.operId = operId;
		this.operName = operName;
		this.ip = ip;
		return this;
	}

	public LogBase bulidTargetObject(int targetId, String targetCode,
			String content) {
		this.targetId = targetId;
		this.targetCode = targetCode;
		this.content = content;
		return this;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getOperId() {
		return this.operId;
	}

	public void setOperId(int operId) {
		this.operId = operId;
	}

	public String getOperName() {
		return this.operName;
	}

	public void setOperName(String operName) {
		this.operName = operName;
	}

	public OperType getOperType() {
		return this.operType;
	}

	public void setOperType(OperType operType) {
		this.operType = operType;
	}

	public LogType getLogType() {
		return this.logType;
	}

	public void setLogType(LogType logType) {
		this.logType = logType;
	}

	public int getTargetId() {
		return this.targetId;
	}

	public void setTargetId(int targetId) {
		this.targetId = targetId;
	}

	public String getTargetCode() {
		return this.targetCode;
	}

	public void setTargetCode(String targetCode) {
		this.targetCode = targetCode;
	}

	public int getTargetType() {
		return this.targetType;
	}

	public void setTargetType(int targetType) {
		this.targetType = targetType;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public SystemCode getSystemCode() {
		return this.systemCode;
	}

	public void setSystemCode(SystemCode systemCode) {
		this.systemCode = systemCode;
	}

	public String getTablesName() {
		return this.tablesName;
	}

	public void setTablesName(String tablesName) {
		this.tablesName = tablesName;
	}

	public JsonType getJsonType() {
		return this.jsonType;
	}

	public void setJsonType(JsonType jsonType) {
		this.jsonType = jsonType;
	}

	private static class JsonDateSerializer extends JsonSerializer<Date> {
		public void serialize(Date value, JsonGenerator jgen,
				SerializerProvider provider) throws IOException,
				JsonProcessingException {
			String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.format(value);
			jgen.writeString(formattedDate);
		}
	}

	private static class JsonDateDeserialize extends JsonDeserializer<Date> {
		public Date deserialize(JsonParser jp, DeserializationContext ctxt)
				throws IOException, JsonProcessingException {
			Date date = null;
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				date = sdf.parse(jp.getText());
			} catch (ParseException localParseException) {
			}

			return date;
		}
	}
}
