package com.dlsw.cn.common.repositories;

import com.dlsw.cn.common.enumerate.OrderStatus;
import com.dlsw.cn.common.po.Order;
import com.dlsw.cn.common.po.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    @Modifying
    @Query("update Order o set o.orderStatus = ?1 where o.id = ?2 and o.user =?3")
    void updateOrderStatusByIdAndUser(OrderStatus status, long id, User user);

    Order findOneByOrderCode(String orderCode);

    @Query("select t.month,sum (t.productNum) from Order t where t.user =?1 and t.orderStatus >= ?2 and t.orderTime >= ?3 and t.orderTime <=?4 group by t.month")
    List<Object[]> analysisOrdinaryOrderSaleVolume(User user, OrderStatus orderStatus, Date begin, Date end);

    @Query("select t.month,sum (t.productNum) from Order t where t.id in ?1 and t.orderStatus >= ?2 and t.orderTime >= ?3 and t.orderTime <=?4  group by t.month")
    List<Object[]> analysisServiceOrderSaleVolume(List<Long> ids, OrderStatus orderStatus, Date begin, Date end);

    @Query("select t.month,sum (t.productNum) from Order t where t.user.orgPath =?1 and t.orderStatus >= ?2 and t.orderTime >= ?3 and t.orderTime <=?4 group by t.month")
    List<Object[]> analysisImmediateTeamOrdinaryOrderSaleVolume(String orgPath, OrderStatus orderStatus, Date begin, Date end);

    @Query("select t.user,sum (t.productNum) from Order t where t.user.orgPath like CONCAT(?1,'%') and t.orderStatus >= ?2 and t.orderTime like CONCAT('%',?3,'%') group by t.user")
    List<Object[]> findOrderListByOrderTime(String orgPath, OrderStatus orderStatus,String month);
}
