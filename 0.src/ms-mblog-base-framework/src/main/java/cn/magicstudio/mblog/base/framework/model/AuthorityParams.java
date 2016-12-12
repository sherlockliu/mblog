package cn.magicstudio.mblog.base.framework.model;

import java.util.List;
import java.util.Map;

public class AuthorityParams {
	private String userId;
	private boolean verification;
	private String brandNoVerify;
	private String systemNoVerify;
	private String areaSystemNoVerify;
	private String[] brandNoList;
	private List<String> hasBrandNoList;
	private String areaVerify;
	private String[] areaList;
	private List<String> hasAreaList;
	private String cityCenterVerify;
	private String[] cityCenterList;
	private List<String> hasCityCenterList;
	private String storeVerify;
	private String[] storeList;
	private List<String> hasStoreList;
	private String shopVerify;
	private String[] shopList;
	private List<String> hasShopList;
	private Map<String, List<String>> queryParams;

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getBrandNoVerify() {
		return this.brandNoVerify;
	}

	public void setBrandNoVerify(String brandNoVerify) {
		this.brandNoVerify = brandNoVerify;
	}

	public String getAreaVerify() {
		return this.areaVerify;
	}

	public void setAreaVerify(String areaVerify) {
		this.areaVerify = areaVerify;
	}

	public String getCityCenterVerify() {
		return this.cityCenterVerify;
	}

	public void setCityCenterVerify(String cityCenterVerify) {
		this.cityCenterVerify = cityCenterVerify;
	}

	public String getStoreVerify() {
		return this.storeVerify;
	}

	public void setStoreVerify(String storeVerify) {
		this.storeVerify = storeVerify;
	}

	public String getShopVerify() {
		return this.shopVerify;
	}

	public void setShopVerify(String shopVerify) {
		this.shopVerify = shopVerify;
	}

	public String[] getBrandNoList() {
		return this.brandNoList;
	}

	public void setBrandNoList(String[] brandNoList) {
		this.brandNoList = brandNoList;
	}

	public List<String> getHasBrandNoList() {
		return this.hasBrandNoList;
	}

	public void setHasBrandNoList(List<String> hasBrandNoList) {
		this.hasBrandNoList = hasBrandNoList;
	}

	public String[] getAreaList() {
		return this.areaList;
	}

	public void setAreaList(String[] areaList) {
		this.areaList = areaList;
	}

	public String[] getCityCenterList() {
		return this.cityCenterList;
	}

	public void setCityCenterList(String[] cityCenterList) {
		this.cityCenterList = cityCenterList;
	}

	public List<String> getHasAreaList() {
		return this.hasAreaList;
	}

	public void setHasAreaList(List<String> hasAreaList) {
		this.hasAreaList = hasAreaList;
	}

	public List<String> getHasCityCenterList() {
		return this.hasCityCenterList;
	}

	public void setHasCityCenterList(List<String> hasCityCenterList) {
		this.hasCityCenterList = hasCityCenterList;
	}

	public String[] getStoreList() {
		return this.storeList;
	}

	public void setStoreList(String[] storeList) {
		this.storeList = storeList;
	}

	public List<String> getHasStoreList() {
		return this.hasStoreList;
	}

	public void setHasStoreList(List<String> hasStoreList) {
		this.hasStoreList = hasStoreList;
	}

	public String[] getShopList() {
		return this.shopList;
	}

	public void setShopList(String[] shopList) {
		this.shopList = shopList;
	}

	public List<String> getHasShopList() {
		return this.hasShopList;
	}

	public void setHasShopList(List<String> hasShopList) {
		this.hasShopList = hasShopList;
	}

	public boolean isVerification() {
		return this.verification;
	}

	public void setVerification(boolean verification) {
		this.verification = verification;
	}

	public Map<String, List<String>> getQueryParams() {
		return this.queryParams;
	}

	public void setQueryParams(Map<String, List<String>> queryParams) {
		this.queryParams = queryParams;
	}

	public String getSystemNoVerify() {
		return this.systemNoVerify;
	}

	public void setSystemNoVerify(String systemNoVerify) {
		this.systemNoVerify = systemNoVerify;
	}

	public String getAreaSystemNoVerify() {
		return this.areaSystemNoVerify;
	}

	public void setAreaSystemNoVerify(String areaSystemNoVerify) {
		this.areaSystemNoVerify = areaSystemNoVerify;
	}
}
