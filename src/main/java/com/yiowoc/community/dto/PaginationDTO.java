package com.yiowoc.community.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class PaginationDTO {
    private List<QuestionDTO> questionDTOs;
    private boolean hasPreviousPage;
    private boolean hasFirstPage;
    private boolean hasNextPage;
    private boolean hasEndPage;
    private Integer curPage;
    private Integer totalPage;
    private List<Integer> shownPages = new ArrayList<>();

    public PaginationDTO(Integer totalPage, Integer curPage, Integer size) {
        this.curPage = curPage;
        this.totalPage = totalPage;

        shownPages.add(this.curPage);
        for(int i = 1; i <= 3; ++i) {
            if(this.curPage - i > 0) {
                shownPages.add(0, this.curPage - i);
            }
            if(this.curPage + i <= totalPage) {
                shownPages.add(this.curPage + i);
            }
        }
        if(this.curPage == 1) {
            hasPreviousPage = false;
        } else {
            hasPreviousPage = true;
        }
        if(this.curPage == this.totalPage) {
            hasNextPage = false;
        } else {
            hasNextPage = true;
        }
        if(shownPages.contains(1)) {
            hasFirstPage = false;
        } else {
            hasFirstPage = true;
        }
        if(shownPages.contains(this.totalPage)) {
            hasEndPage = false;
        } else {
            hasEndPage = true;
        }
    }
}
