package com.kindol.o2o.service;

public interface CacheService {

    /**
     * 依据key前缀删除撇配模式下的所有key-value，如传入shopcategory，则shopcategory_allfirstlevel等的
     * 以shopcategory打头的key-value都会被清空
     */
    void removeFromCache(String keyPrefix);

}
