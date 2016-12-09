package cn.magicstudio.mblog.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 请写出类的用途 
 * @author user
 * @date  2016-12-09 10:45:03
 * @version 1.0.0
 * @copyright (C) 2013 WonHigh Information Technology Co.,Ltd 
 * All Rights Reserved. 
 * 
 * The software for the WonHigh technology development, without the 
 * company's written consent, and any other individuals and 
 * organizations shall not be used, Copying, Modify or distribute 
 * the software.
 * 
 */
public class Managers implements Serializable {
    /**
     * 
     */
    private Long id;

    /**
     * 
     */
    private Long shopId;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

    /**
     * 密码加盐
     */
    private String passwordSalt;

    /**
     * 创建日期
     */
    private Date createDate;

    /**
     * 
     */
    private String remark;

    /**
     * 真实名称
     */
    private String realName;

    /**
     * 
     * {@linkplain #id}
     *
     * @return the value of managers.id
     */
    public Long getId() {
        return id;
    }

    /**
     * 
     * {@linkplain #id}
     * @param id the value for managers.id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 
     * {@linkplain #shopId}
     *
     * @return the value of managers.shop_id
     */
    public Long getShopId() {
        return shopId;
    }

    /**
     * 
     * {@linkplain #shopId}
     * @param shopId the value for managers.shop_id
     */
    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    /**
     * 
     * {@linkplain #roleId}
     *
     * @return the value of managers.role_id
     */
    public Long getRoleId() {
        return roleId;
    }

    /**
     * 
     * {@linkplain #roleId}
     * @param roleId the value for managers.role_id
     */
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    /**
     * 
     * {@linkplain #userName}
     *
     * @return the value of managers.user_name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 
     * {@linkplain #userName}
     * @param userName the value for managers.user_name
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 
     * {@linkplain #password}
     *
     * @return the value of managers.password
     */
    public String getPassword() {
        return password;
    }

    /**
     * 
     * {@linkplain #password}
     * @param password the value for managers.password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 
     * {@linkplain #passwordSalt}
     *
     * @return the value of managers.password_salt
     */
    public String getPasswordSalt() {
        return passwordSalt;
    }

    /**
     * 
     * {@linkplain #passwordSalt}
     * @param passwordSalt the value for managers.password_salt
     */
    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    /**
     * 
     * {@linkplain #createDate}
     *
     * @return the value of managers.create_date
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * 
     * {@linkplain #createDate}
     * @param createDate the value for managers.create_date
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * 
     * {@linkplain #remark}
     *
     * @return the value of managers.remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 
     * {@linkplain #remark}
     * @param remark the value for managers.remark
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 
     * {@linkplain #realName}
     *
     * @return the value of managers.real_name
     */
    public String getRealName() {
        return realName;
    }

    /**
     * 
     * {@linkplain #realName}
     * @param realName the value for managers.real_name
     */
    public void setRealName(String realName) {
        this.realName = realName;
    }
}