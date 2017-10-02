package com.dlsw.cn.service;

import com.dlsw.cn.vo.PageVo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public abstract class BaseService {

    public static final String SORT_PREFIX = "+";

    private PageRequest buildPageRequest(int pageNumber, int pageSize, Sort sort) {
        return new PageRequest(pageNumber - 1, pageSize == 0 ? 10 : pageSize, sort);
    }

    protected PageRequest getPageRequest(PageVo request) {
        PageRequest pageRequest;
        if (request.getSort() != null) {
            if (request.getSort().startsWith(SORT_PREFIX)) {
                Sort sort = buildSort(request);
                pageRequest = this.buildPageRequest(request.getPage(), request.getSize(), sort);
            } else {
                Sort sort = buildSort(request);
                pageRequest = this.buildPageRequest(request.getPage(), request.getSize(), sort);
            }
        } else {
            Sort sort = buildSort(request);
            pageRequest = this.buildPageRequest(request.getPage(), request.getSize(), sort);
        }
        return pageRequest;
    }

    protected Sort buildSort(PageVo request) {
        Sort sort = null;
        if (request.getSort() != null) {
            if (request.getSort().startsWith(SORT_PREFIX)) {
                sort = new Sort(Sort.Direction.ASC, request.getSort().substring(1, request.getSort().length()));
            } else {
                sort = new Sort(Sort.Direction.DESC, request.getSort().substring(1, request.getSort().length()));
            }
        } else {
            sort = new Sort(Sort.Direction.ASC, "id");
        }
        return sort;
    }


}
