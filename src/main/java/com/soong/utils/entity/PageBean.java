package com.soong.utils.entity;

import java.util.List;
import java.util.Map;

/**
 * 分页工具类，分页对象 PageBean 包括：
 * 1.数据表总记录数：totalCount，通过查询数据库得到，要考虑是否带查询条件。
 * 2.每一页显示的数据集合：beanList，通过查询数据库得到，要考虑是否带查询条件。
 * 3.当前页序号：currentPage，通过请求参数的形式传给 Servlet，如果没有传入该参数，应给其指定默认值，一般为 1。
 * 4.每一页显示的记录数：pageSize，通过请求参数的形式传给 Servlet，如果没有传入该参数，应给其指定默认值。
 * 5.总的页数：totalPage，其中 totalPage 只提供 getter() 方法，不能提供 setter() 方法，因为 totalPage 是
 *   由 totalCount 与 pageSize 计算得到的。
 * 6.查询条件：conditions，由用户的请求参数封装而成。在生成 PageBean 对象的时候设置好，返回给页面。
 */
public class PageBean<T> {
    /**
     * 需要查询的数据表的总记录数
     */
    private int totalCount;
    /**
     * 每一页显示的数据集合
     */
    private List<T> beanList;
    /**
     * 当前页面序号
     */
    private int currentPage;
    /**
     * 每一页显示的记录数
     */
    private int pageSize;

    /**
     * 查询条件
     */
    private Map<String, String> conditions;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    /**
     * 总的页数，通过计算得到，所以不能设置 setter() 方法，也不用设置成员变量
     * totalPage = totalCount % rows == 0 ? totalCount / rows : totalCount / rows + 1
     */
    public int getTotalPage() {
        return totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1;
    }

    public List<T> getBeanList() {
        return beanList;
    }

    public void setBeanList(List<T> beanList) {
        this.beanList = beanList;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Map<String, String> getConditions() {
        return conditions;
    }

    public void setConditions(Map<String, String> conditions) {
        this.conditions = conditions;
    }
}
