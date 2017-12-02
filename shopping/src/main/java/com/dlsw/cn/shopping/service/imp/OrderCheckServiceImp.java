package com.dlsw.cn.shopping.service.imp;

import com.dlsw.cn.common.enumerate.DirectorLevel;
import com.dlsw.cn.common.enumerate.OrderStatus;
import com.dlsw.cn.common.enumerate.PayType;
import com.dlsw.cn.common.enumerate.RoleType;
import com.dlsw.cn.common.po.DeliveryAddress;
import com.dlsw.cn.common.po.Order;
import com.dlsw.cn.common.po.Product;
import com.dlsw.cn.common.po.User;
import com.dlsw.cn.common.repositories.UserRepository;
import com.dlsw.cn.common.service.BaseService;
import com.dlsw.cn.shopping.service.OrderCheckService;
import com.dlsw.cn.shopping.vo.OrderVo;
import com.dlsw.cn.shopping.vo.PayCertificateVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderCheckServiceImp extends BaseService implements OrderCheckService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public void applyOrder(User user, Product product, User recommend_man, DeliveryAddress deliveryAddress, OrderVo orderVo) {
        if (user.getAppId() != null && !user.isVerificationPhone()) {
            throw new RuntimeException("您是微信用户还未验证过手机，请先设置手机");
        }
        if (user.getLevel() == 0) {
            throw new RuntimeException("亲爱的董事长，您不能购买产品");
        }
        if (product == null) {
            throw new RuntimeException("无法找到对应的商品");
        }
        if (deliveryAddress == null) {
            throw new RuntimeException("无法找到对应的收货地址");
        }
        if (orderVo.getPayType() == PayType.线下转账 && StringUtils.isBlank(orderVo.getRecommendPhone())) {
            throw new RuntimeException("线下订单推荐人不能为空");
        }
        if (orderVo.getRecommendPhone().equals(user.getPhone())) {
            throw new RuntimeException("推荐人不能是本人");
        }
        if (userRepository.findOffspringCountByOrgPathAndPhone(getOrgPath(user), orderVo.getRecommendPhone()) != null) {
            throw new RuntimeException("推荐人不能是自己的下属");
        }
        if (recommend_man == null) {
            throw new RuntimeException("推荐人没有找到");
        }
        if (recommend_man.getRoleType() == RoleType.普通) {
            throw new RuntimeException("该推荐人不是VIP、合伙人、高级合伙人");
        }
        if (!gtLevelSelf(recommend_man, user)) {
            User higherUser = recommend_man.getHigher();
            while (!gtLevelSelf(higherUser, user)) {
                higherUser = higherUser.getHigher();
            }
            throw new RuntimeException("订单必须提交给级别大于自己的上级，推荐您提交给" + higherUser.getPhone() + "(" + higherUser.getNickname() + ")");
        }
        if (!recommend_man.isVerificationPhone()) {
            throw new RuntimeException("你的推荐人还未验证过手机，无法填写");
        }
    }

    /**
     * 验证上级和自己的级别大小
     *
     * @param recommend
     * @param orderUser
     */
    private boolean gtLevelSelf(User recommend, User orderUser) {
        if(recommend.getLevel() == 0){
            return true;
        }
        if (orderUser.getRoleType().getCode() < recommend.getRoleType().getCode()) {
            return true;
        }
        if (recommend.getRoleType().getCode() == orderUser.getRoleType().getCode() && recommend.getRoleType() == RoleType.高级合伙人) {
            if (DirectorLevel.getDirectorLevel(orderUser).getCode() < DirectorLevel.getDirectorLevel(recommend).getCode()) {
                return true;
            }
        }
        return false;

    }

    @Override
    public void savePayCert(Order order, PayCertificateVo payCertificateVo) {
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        if (order.getOrderStatus() != OrderStatus.待支付 && order.getOrderStatus() != OrderStatus.待确认) {
            throw new RuntimeException("订单状态必须是待支付");
        }
        if (order.getPayWay() != PayType.线下转账) {
            throw new RuntimeException("上传凭证，必须是线下订单类型");
        }
        if (StringUtils.isBlank(order.getRecommendPhone())) {
            throw new RuntimeException("线下订单推荐人不能为空");
        }
        User recommend_man = userRepository.findByPhone(order.getRecommendPhone());
        if (recommend_man == null) {
            throw new RuntimeException("推荐人没有找到");
        }
        if (payCertificateVo.getPayCertPhoto() == null || payCertificateVo.getPayCertPhoto().length == 0) {
            throw new RuntimeException("凭证照片不能为空");
        }
        if (StringUtils.isBlank(payCertificateVo.getPayCertInfo())) {
            throw new RuntimeException("凭证信息不能为空");
        }
    }

    @Override
    public void sureOrder(Order order, User user, User orderUser) {
        //线下转账，由属确认支付
        if (StringUtils.isBlank(order.getRecommendPhone()) || !user.getPhone().equals(order.getRecommendPhone())) {
            throw new RuntimeException("对不起，线下转账为成功，需要推荐人确认支付");
        }
        if (order.getPayWay() != PayType.线下转账) {
            throw new RuntimeException("确认订单，必须是线下转账类型");
        }
        if (order.getOrderStatus() != OrderStatus.待确认) {
            throw new RuntimeException("订单状态必须是待确认");
        }
    }
}
