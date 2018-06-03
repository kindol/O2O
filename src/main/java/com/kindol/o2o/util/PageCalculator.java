package com.kindol.o2o.util;

public class PageCalculator {

    /**
     * 将前端进来的pageIndex转换为后台的rowIndex，rowIndex从0开始计数
     * @param pageIndex
     * @return
     */
    public static int calculateRowIndex(int pageIndex, int pageSize){
            return (pageIndex > 0) ? (pageIndex-1)*pageSize : 0;
    }

}
