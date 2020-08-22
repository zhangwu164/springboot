package com.youedata.util.es;
import java.util.List;
import java.util.Map;

public class EsDataPage {
    private Integer currentPage;

    private Integer pageSize;

    private Integer totalHits;

    private List<Map<String,Object>> sourceList;

    public EsDataPage(){
    }

    //有参构造
    public EsDataPage(Integer currentPage,Integer pageSize,Integer totalHits,List<Map<String,Object>> sourceList){
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalHits = totalHits;
        this.sourceList = sourceList;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(Integer totalHits) {
        this.totalHits = totalHits;
    }

    public List<Map<String, Object>> getSourceList() {
        return sourceList;
    }

    public void setSourceList(List<Map<String, Object>> sourceList) {
        this.sourceList = sourceList;
    }
}
