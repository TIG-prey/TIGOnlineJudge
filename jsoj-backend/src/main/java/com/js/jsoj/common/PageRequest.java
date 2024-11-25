package com.js.jsoj.common;

import com.js.jsoj.constant.CommonConstant;
import lombok.Data;

/**
 * @author JianShang
 * @version 1.0.0
 * @description 分页请求
 * @date 2024-10-17 10:30:24
 */
@Data
public class PageRequest {

    /**
     * 当前页号
     */
    private int current = 1;

    /**
     * 页面大小
     */
    private int pageSize = 10;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序顺序（默认升序）
     */
    private String sortOrder = CommonConstant.SORT_ORDER_ASC;
}
