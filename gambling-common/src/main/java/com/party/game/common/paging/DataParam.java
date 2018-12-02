package com.party.game.common.paging;

/**
 * 数据控制参数
 *
 * @author yifeng
 * @date 2018/8/6 0006
 * @time 下午 17:28
 */
public class DataParam {
    // 关联字段
    private String joinColumn;

    // 所属者编号
    private String initiatorId;

    // 是否显示全部合作商
    private Boolean isShowAll;

    public String getJoinColumn() {
        return joinColumn;
    }

    public void setJoinColumn(String joinColumn) {
        this.joinColumn = joinColumn;
    }

    public String getInitiatorId() {
        return initiatorId;
    }

    public void setInitiatorId(String initiatorId) {
        this.initiatorId = initiatorId;
    }

    public Boolean getShowAll() {
        return isShowAll;
    }

    public void setShowAll(Boolean showAll) {
        isShowAll = showAll;
    }
}
