package com.dlsw.cn.po;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * @author zhanwang
 * @create 2017-10-02 10:44
 **/
@MappedSuperclass
@DynamicUpdate
@DynamicInsert
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "entityCache")
public class BasePo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
