package com.dlsw.cn.common.repositories;
import com.dlsw.cn.common.po.DeliveryAddress;
import com.dlsw.cn.common.po.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DeliveryAddressRepository extends CrudRepository<DeliveryAddress, Long>, JpaSpecificationExecutor<DeliveryAddress> {

    DeliveryAddress findOneByIdAndUser(long id, User user);

    DeliveryAddress findOneByIsDefaultAndUser(Boolean isDefault, User user);

}
