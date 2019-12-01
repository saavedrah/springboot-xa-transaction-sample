package com.mono.transactionSample.model;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="multitenantcodectrl")
@IdClass(MultiTenantCodeCtrl.MultiTenantCodeCtrlPK.class)
public class MultiTenantCodeCtrl  implements Serializable {

    @Id
    @Column(name="userid")
    private String userId;
    @Id
    @Column(name="sys_tenantcode")
    private String tenantCode;
    @Column(name="username")
    private String userName;
    private String password;
    @Column(name="lang")
    private String langCd;
    private String timezone;
    @Column(name="imedaccessdenyflg")
    private int imedAccessDenyFlg;
    @Column(name="allowduedate")
    private String allowDueDate;
    @Column(name="adminflg")
    private int adminFlg;
    @Deprecated
    private String vendorcode;
    private String uuid;
    @Column(name="passwordexpirydate")
    private String passwordExpiryDate;


    public MultiTenantCodeCtrl() {
    }

    public MultiTenantCodeCtrl(String userId, String password, String tenantCode) {
        this.userId = userId;
        this.password = password;
        this.langCd = "JA";
        this.timezone = "Tokyo";
        this.imedAccessDenyFlg = 0;
        this.allowDueDate = "";
        this.adminFlg = 0;
        this.passwordExpiryDate = "";
        this.uuid = "";
        this.tenantCode = tenantCode;
    }

    public MultiTenantCodeCtrl(String tenantCode, String userId) {
        this.tenantCode = tenantCode;
        this.userId = userId;
        this.password = "password";
        this.langCd = "jp";
        this.timezone = "JST";
        this.imedAccessDenyFlg = 0;
        this.allowDueDate = "";
        this.adminFlg = 0;
        this.passwordExpiryDate = "";
        this.uuid = "";
    }

    /**
     * ユーザーIDを取得する
     * @return ユーザーID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * ユーザーIDを設定する
     * @param userId ユーザーID
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * ユーザー名を取得する
     * @return ユーザー名
     */
    public String getUserName() {
        return this.userName;
    }

    /**
     * ユーザー名を設定する
     * @param userName ユーザー名
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    public String getLangCd() {
        String languageCd = this.langCd;
        if (languageCd == null) {
            return "";
        }
        if (this.langCd.equalsIgnoreCase("JP")) {
            languageCd = "ja";
        }
        return languageCd.toLowerCase();
    }

    public void setLangCd(String langCd) {
        this.langCd = langCd;
    }

    public int getAdminFlg() {
        return this.adminFlg;
    }

    public void setAdminFlg(int adminFlg) {
        this.adminFlg = adminFlg;
    }

    public String getTimezone() {
        return this.timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTenantCode() {
        return this.tenantCode;
    }

    public int getImedAccessDenyFlg() {
        return this.imedAccessDenyFlg;
    }

    public void setImedAccessDenyFlg(int imedAccessDenyFlg) {
        this.imedAccessDenyFlg = imedAccessDenyFlg;
    }

    public String getAllowDate() {
        return this.allowDueDate;
    }

    public void setAllowDueDate(String allowDueDate) {
        this.allowDueDate = allowDueDate;
    }

    public String getVendorCode() {
        return this.vendorcode;
    }

    public void setVendorcode(String vendorcode) {
        this.vendorcode = vendorcode;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPasswordExpiryDate() {
        return passwordExpiryDate;
    }

    public void setPasswordExpiryDate(String passwordExpiryDate) {
        this.passwordExpiryDate = passwordExpiryDate;
    }

    public boolean isLocked() {
        return (this.imedAccessDenyFlg == 1 ? true : false);
    }

    public boolean getAdminFlgToBoolean() {
        return (this.adminFlg == 1 ? true : false);
    }

    @Embeddable
    static class MultiTenantCodeCtrlPK implements Serializable {
        @Column(name="userid")
        private String userId;
        @Column(name="sys_tenantcode")
        private String tenantCode;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof MultiTenantCodeCtrlPK)) {
            return false;
        }

        MultiTenantCodeCtrlPK c = (MultiTenantCodeCtrlPK) obj;

        return (c.tenantCode.equals(this.tenantCode)
                && c.userId.equals(this.userId));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + tenantCode.hashCode();
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
        return result;
    }
}
