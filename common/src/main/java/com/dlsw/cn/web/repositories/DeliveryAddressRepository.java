package com.dlsw.cn.web.repositories;
import com.dlsw.cn.web.po.DeliveryAddress;
import com.dlsw.cn.web.po.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DeliveryAddressRepository extends CrudRepository<DeliveryAddress, Long>, JpaSpecificationExecutor<DeliveryAddress> {

    DeliveryAddress findOneByIdAndUser(long id, User user);

    DeliveryAddress findOneByIsDefaultAndUser(Boolean isDefault, User user);

}
