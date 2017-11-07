package com.dlsw.cn.repositories;

import com.dlsw.cn.enumerate.RoleType;
import com.dlsw.cn.po.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long>, JpaSpecificationExecutor<User> {

    User findOne(Long id);

    User save(User user);

    @Query("select u from User u where u.phone = ?1 and u.disable=0")
    User findByPhone(String phone);

    @Query("select u from User u where u.phone = ?1 or u.appId =?1 and u.disable=0")
    User findByPhoneOrAppId(String phone);

    User getByAuthorizationCode(String authorizationCode);

    @Query("select u.roleType,count(u) from User u where u.orgPath like CONCAT(?1,'%')  group by u.roleType")
    List<Object[]> analysisMemberDistribution(String orgPath);

    @Query("select u from User u where u.orgPath like concat(?1,'%') and phone = ?2 group by u.roleType")
    User findOffspringCountByOrgPathAndPhone(String orgPath, String phone);

    @Query("select u.roleType,count(u) from User u where u.orgPath = ?1 group by u.roleType")
    List<Object[]> analysisImmediateMemberDistribution(String orgPath);

    @Query("select u.roleType,count(u) from User u where  u.orgPath = ?1 and u.roleType = ?2 and diffDate <= 30 group by u.roleType")
    List<Object[]> analysisNewSeniorImmediateMemberDistribution(String orgPath, RoleType roleType);

    @Query("select u.roleType,count(u) from User u where  u.orgPath like CONCAT(?1,'%') and diffDate <= 30 group by u.roleType")
    List<Object[]> analysisNewMemberDistribution(String orgPath);

    @Query("select u from User u where u.roleType < ?1 and u.disable=0")
    List<User> findByLessThanRoleType(RoleType roleType);

    @Query("select u from User u where u.roleType = ?1 and u.disable=0")
    List<User> findByEqualsRoleType(RoleType roleType);

    @Query("select u from User u where u.orgPath like CONCAT(?1,'%') and u.disable=0")
    List<User> findByLikeOrgPath(String orgPath);

    @Query("select u from User u where u.orgPath = ?1 and u.disable=0")
    List<User> findByOneLevelOrgPath(String orgPath);

    @Query("select u from User u where  u.orgPath = ?1 and u.roleType = ?2 and u.diffDate <= 30")
    List<User> findNewSeniorImmediateMemberList(String orgPath, RoleType roleType);

    @Query("select u from User u where  u.orgPath = ?1 and u not in (select o.user from Order o where o.diffDate <= 30)")
    List<User> findSleepMemberList(String orgPath);

    @Query("select u.roleType,count(u) from User u where  u.orgPath = ?1 and u.roleType = ?2 and u not in (select o.user from Order o where o.diffDate <= 30) group by u.roleType")
    List<Object[]> analysisSleepMemberDistribution(String orgPath, RoleType roleType);

    @Query(value = "select count(*) from t_user u where u.org_path like CONCAT(?1,'%') and u.role_type = ?2 and u.disable=0", nativeQuery = true)
    Long findSumByLikeOrgPath(String orgPath,RoleType roleType);

    @Query(value = "select count(*) from t_user u where u.org_path = ?1 and u.role_type = ?2 and u.disable=0", nativeQuery = true)
    Long findSumByOneLevelOrgPath(String orgPath,RoleType roleType);

    User findByAppId(String appId);
}
