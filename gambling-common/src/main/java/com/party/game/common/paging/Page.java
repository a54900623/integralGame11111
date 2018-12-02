/*
 * Copyright 2014 Qunar.com All right reserved. This software is the
 * confidential and proprietary information of Qunar.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Qunar.com.
 */
package com.party.game.common.paging;

import org.apache.ibatis.session.RowBounds;

import java.util.ArrayList;


/** 分页信息
 * @author wei.li
 * @version 2016/8/1
 */
public class Page extends RowBounds {

    public static final int DEFAULT_PAGE_NO = 1;
    public static final int DEFAULT_PAGE_SIZE = 5;
    private int limit;
    private int page;
    private int totalCount;

    public Page() {
        this.page = DEFAULT_PAGE_NO;
        this.limit = DEFAULT_PAGE_SIZE;
    }

    public Page(int page, int limit) {
        this.limit = limit;
        this.page = page;
    }

    public int getPage() {
        return this.page;
    }

    public int getLimit() {
        return this.limit;
    }

    public int getTotalCount() {
        return this.totalCount;
    }

    public boolean isFirstPage() {
        return this.page <= 1;
    }

    public boolean isLastPage() {
        return this.page >= this.getTotalPages();
    }

    public int getPrePage() {
        return this.isHasPrePage()?this.page - 1:this.page;
    }

    public int getNextPage() {
        return this.isHasNextPage()?this.page + 1:this.page;
    }

    public boolean isDisabledPage(int page) {
        return page < 1 || page > this.getTotalPages() || page == this.page;
    }

    public boolean isHasPrePage() {
        return this.page - 1 >= 1;
    }

    public boolean isHasNextPage() {
        return this.page + 1 <= this.getTotalPages();
    }

    public int getStartRow() {
        return this.getLimit() > 0 && this.totalCount > 0?(this.page > 0?(this.page - 1) * this.getLimit() + 1:0):0;
    }

    public int getEndRow() {
        return this.page > 0?Math.min(this.limit * this.page, this.getTotalCount()):0;
    }

    public int getOffset() {
        return this.page > 0?(this.page - 1) * this.getLimit():0;
    }

    public int getTotalPages() {
        if(this.totalCount <= 0) {
            return 0;
        } else if(this.limit <= 0) {
            return 0;
        } else {
            int count = this.totalCount / this.limit;
            if(this.totalCount % this.limit > 0) {
                ++count;
            }

            return count;
        }
    }

    protected int computePageNo() {
        return computePageNumber(page, this.limit, this.totalCount);
    }

    public Integer[] getSlider() {
        return this.slider(7);
    }

    public Integer[] slider(int slidersCount) {
        return generateLinkPageNumbers(this.getPage(), this.getTotalPages(), slidersCount);
    }

    private static int computeLastPageNumber(int totalItems, int pageSize) {
        if(pageSize <= 0) {
            return 1;
        } else {
            int result = totalItems % pageSize == 0?totalItems / pageSize:totalItems / pageSize + 1;
            if(result <= 1) {
                result = 1;
            }

            return result;
        }
    }

    private static int computePageNumber(int page, int pageSize, int totalItems) {
        return page <= 1?1:(2147483647 != page && page <= computeLastPageNumber(totalItems, pageSize)?page:computeLastPageNumber(totalItems, pageSize));
    }

    private static Integer[] generateLinkPageNumbers(int currentPageNumber, int lastPageNumber, int count) {
        int avg = count / 2;
        int startPageNumber = currentPageNumber - avg;
        if(startPageNumber <= 0) {
            startPageNumber = 1;
        }

        int endPageNumber = startPageNumber + count - 1;
        if(endPageNumber > lastPageNumber) {
            endPageNumber = lastPageNumber;
        }

        if(endPageNumber - startPageNumber < count) {
            startPageNumber = endPageNumber - count;
            if(startPageNumber <= 0) {
                startPageNumber = 1;
            }
        }

        ArrayList result = new ArrayList();

        for(int i = startPageNumber; i <= endPageNumber; ++i) {
            result.add(new Integer(i));
        }

        return (Integer[])result.toArray(new Integer[result.size()]);
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Paginator");
        sb.append("{page=").append(this.page);
        sb.append(", limit=").append(this.limit);
        sb.append(", totalCount=").append(this.totalCount);
        sb.append('}');
        return sb.toString();
    }

}
