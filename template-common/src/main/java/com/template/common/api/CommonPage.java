package com.template.common.api;

import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author qiuqfang
 * @date 2021/6/13
 */
public class CommonPage<T> {
    /**
     * 第几页
     */
    private Integer pageNum;
    /**
     * 每页共多少条数据
     */
    private Integer pageSize;
    /**
     * 共几页
     */
    private Integer totalPage;
    /**
     * 共多少条数据
     */
    private Long total;
    /**
     * 每页具体数据
     */
    private List<T> list;

    /**
     * 将PageHelper分页后的list转为分页信息
     *
     * @param list 分页数据
     * @param <T>  泛型
     * @return 分页信息
     */
    public static <T> CommonPage<T> restPage(List<T> list) {
        CommonPage<T> result = new CommonPage<T>();
        PageInfo<T> pageInfo = new PageInfo<T>(list);
        result.setTotalPage(pageInfo.getPages());
        result.setPageNum(pageInfo.getPageNum());
        result.setPageSize(pageInfo.getPageSize());
        result.setTotal(pageInfo.getTotal());
        result.setList(pageInfo.getList());
        return result;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
