package com.party.game.mobile.web.dto;

import com.party.game.common.paging.Page;

import java.io.Serializable;

/**
 * 异步请求实体
 * party
 * Created by wei.li
 * on 2016/8/26 0026.
 */
public class AjaxResult implements Serializable {

    private static final String SUCCESS_DESCRIPTION = "success";

    /**
     * ajax 交互结果
     */
    private boolean success;

    /**
     * 交互描述信息
     */
    private String description;

    /**
     * 上次请求url地址
     */
    private String lastRequestUrl;

    /**
     * 本次请求url地址
     */
    private String currentRequestUrl;

    /**
     * 下次跳转的url地址
     */
    private String nextRequestUrl;

    /**
     * 交互数据
     */
    private Object data;

    /**
     * 分页模型
     */
    private Page page;

    public AjaxResult() {
        this(false);
    }

    public AjaxResult(boolean success) {
        this(success, SUCCESS_DESCRIPTION);
    }

    public AjaxResult(boolean success, String description) {
        this(success, description, null, null);
    }

    public AjaxResult(boolean success, String description, Object data, Page page) {
        this.success = success;
        this.description = description;
        this.data = data;
        this.page = page;
    }

    public AjaxResult(boolean success, String description, Object data) {
        this.success = success;
        this.description = description;
        this.data = data;
    }

    public static AjaxResult error(String description) {
        return new AjaxResult(false, description);
    }

    public static AjaxResult error(String description, Object data) {
        return new AjaxResult(false, description, data);
    }

    public static AjaxResult success() {
        return new AjaxResult(true, SUCCESS_DESCRIPTION);
    }

    public static AjaxResult success(String description) {
        return new AjaxResult(true, description);
    }

    public static AjaxResult success(Object data) {
        return new AjaxResult(true, null, data, null);
    }

    public static AjaxResult success(Object data, Page page) {
        return new AjaxResult(true, null, data, page);
    }

    public static AjaxResult success(String description, Object data) {
        return new AjaxResult(true, description, data, null);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLastRequestUrl() {
        return lastRequestUrl;
    }

    public void setLastRequestUrl(String lastRequestUrl) {
        this.lastRequestUrl = lastRequestUrl;
    }

    public String getCurrentRequestUrl() {
        return currentRequestUrl;
    }

    public void setCurrentRequestUrl(String currentRequestUrl) {
        this.currentRequestUrl = currentRequestUrl;
    }

    public String getNextRequestUrl() {
        return nextRequestUrl;
    }

    public void setNextRequestUrl(String nextRequestUrl) {
        this.nextRequestUrl = nextRequestUrl;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}
