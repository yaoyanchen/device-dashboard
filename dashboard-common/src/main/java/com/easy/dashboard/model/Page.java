package com.easy.dashboard.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Page {
    private Long total;
    private Integer pageIndex;
    private Integer pageNum;
    private List<?> list;

    public boolean isHaveNext() {
        return !(this.getTotal() > ((long) (this.getPageIndex() - 1) * this.getPageNum()));
    }

    public boolean isHaveBack() {
        return this.getPageIndex() != 1;
    }



    public Page(Integer pageIndex,Integer pageNum) {
        if (pageIndex == null) {
            this.setPageIndex(1);
        }
        if (pageNum == null) {
            this.setPageNum(10);
        }
        if (pageIndex != null && pageNum != null) {
            this.setPageIndex(pageIndex);
            this.setPageNum(pageNum);
        }
    }
}
