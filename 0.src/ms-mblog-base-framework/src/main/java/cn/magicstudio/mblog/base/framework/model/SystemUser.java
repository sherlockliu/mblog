package cn.magicstudio.mblog.base.framework.model;

import java.io.Serializable;
import java.util.Date;


import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import cn.magicstudio.mblog.base.framework.enums.EnvironmentIdentifyEnum;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SystemUser implements Serializable, Cloneable {
	private static final long serialVersionUID = -3494497248942074486L;
	private Integer userid;
	private String username;
	private String sex;
	private String loginName;
	private String loginPassword;
	private String mobilePhone;
	private String telPhone;
	private String email;
	private String state;
	private String category;
	private String organizName;
	private String organizNo;
	private Date pwdUpdateTime;
	private Date gmtCreate;
	private Date gmtUpdate;
	private String storeType;
	private String areasystemid;
	private String parentsystemid;
	private String regionNo;
	private String organName;
	private String organNo;
	private int organLevel;
	private String organTypeNo;
	private String checkCost;
	private EnvironmentIdentifyEnum env;
	private String ip;

	public String getOrganName() {
		return this.organName;
	}

	public void setOrganName(String organName) {
		this.organName = organName;
	}

	public String getOrganNo() {
		return this.organNo;
	}

	public void setOrganNo(String organNo) {
		this.organNo = organNo;
	}

	public int getOrganLevel() {
		return this.organLevel;
	}

	public void setOrganLevel(int organLevel) {
		this.organLevel = organLevel;
	}

	public String getParentsystemid() {
		return this.parentsystemid;
	}

	public void setParentsystemid(String parentsystemid) {
		this.parentsystemid = parentsystemid;
	}

	public String getAreasystemid() {
		return this.areasystemid;
	}

	public void setAreasystemid(String areasystemid) {
		this.areasystemid = areasystemid;
	}

	public Integer getUserid() {
		return this.userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@JsonIgnore
	public String getSex() {
		return this.sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getLoginName() {
		return this.loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	@JsonIgnore
	public String getLoginPassword() {
		return this.loginPassword;
	}

	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}

	public String getMobilePhone() {
		return this.mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getTelPhone() {
		return this.telPhone;
	}

	public void setTelPhone(String telPhone) {
		this.telPhone = telPhone;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getOrganizName() {
		return this.organizName;
	}

	public void setOrganizName(String organizName) {
		this.organizName = organizName;
	}

	public String getOrganizNo() {
		return this.organizNo;
	}

	public void setOrganizNo(String organizNo) {
		this.organizNo = organizNo;
	}

	@JsonIgnore
	public Date getPwdUpdateTime() {
		return this.pwdUpdateTime;
	}

	public void setPwdUpdateTime(Date pwdUpdateTime) {
		this.pwdUpdateTime = pwdUpdateTime;
	}

	public Date getGmtCreate() {
		return this.gmtCreate;
	}

	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	@JsonIgnore
	public Date getGmtUpdate() {
		return this.gmtUpdate;
	}

	public void setGmtUpdate(Date gmtUpdate) {
		this.gmtUpdate = gmtUpdate;
	}

	public String getStoreType() {
		return this.storeType;
	}

	public void setStoreType(String storeType) {
		this.storeType = storeType;
	}

	public EnvironmentIdentifyEnum getEnv() {
		return this.env;
	}

	public void setEnv(EnvironmentIdentifyEnum env) {
		this.env = env;
	}

	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getRegionNo() {
		return this.regionNo;
	}

	public void setRegionNo(String regionNo) {
		this.regionNo = regionNo;
	}

	public String getOrganTypeNo() {
		return this.organTypeNo;
	}

	public void setOrganTypeNo(String organTypeNo) {
		this.organTypeNo = organTypeNo;
	}

	public String getCheckCost() {
		return this.checkCost;
	}

	public void setCheckCost(String checkCost) {
		this.checkCost = checkCost;
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
