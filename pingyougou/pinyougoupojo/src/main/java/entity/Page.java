package entity;

import java.io.Serializable;
import java.util.List;

public class Page<T> implements Serializable {
         private int totalRecord; /**
     * 记录列表
     */ private List<T> records; /**
     * 页码
     */ private int pageNo = 1; /**
     * 每页显示长度
     */ private int pageSize = 10; /**
     * 分页开始
     */ private String start;

        private String end;



    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getStart() {
        start = String.valueOf(((pageNo - 1) * pageSize));
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        end = String.valueOf((pageNo * pageSize));
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}
