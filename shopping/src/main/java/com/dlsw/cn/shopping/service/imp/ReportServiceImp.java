package com.dlsw.cn.shopping.service.imp;

import com.dlsw.cn.common.dto.EntryDTO;
import com.dlsw.cn.common.enumerate.OrderStatus;
import com.dlsw.cn.common.enumerate.RoleType;
import com.dlsw.cn.common.po.Order;
import com.dlsw.cn.common.po.User;
import com.dlsw.cn.common.repositories.OrderRepository;
import com.dlsw.cn.common.repositories.UserRepository;
import com.dlsw.cn.common.service.BaseService;
import com.dlsw.cn.common.util.DateUtil;
import com.dlsw.cn.shopping.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhanwang
 * @create 2017-08-19 1:18
 **/
@Service
public class ReportServiceImp extends BaseService implements ReportService {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    UserRepository userRepository;

    @Override
    public Map<String, Long> analysisMemberDistribution(String phone) {
        User user = userRepository.findByPhone(phone);
        List<Object[]> list = userRepository.analysisMemberDistribution(getOrgPath(user));
        return fillResult(list);
    }

    @Override
    public Map<String, Long> analysisImmediateMemberDistribution(String phone) {
        User user = userRepository.findByPhone(phone);
        List<Object[]> list = userRepository.analysisImmediateMemberDistribution(getOrgPath(user));
        return fillResult(list);
    }

    @Override
    public Map<String, Long> analysisNewMemberDistribution(String phone) {
        User user = userRepository.findByPhone(phone);
        List<Object[]> list = userRepository.analysisNewMemberDistribution(getOrgPath(user));
        return fillResult(list);
    }

    @Override
    public Map<String, Long> analysisNewSeniorImmediateMemberDistribution(String phone) {
        User user = userRepository.findByPhone(phone);
        List<Object[]> list = userRepository.analysisNewSeniorImmediateMemberDistribution(getOrgPath(user), RoleType.高级合伙人);
        return fillResult(list);
    }


    @Override
    public Map<String, Long> analysisSleepDistribution(String phone) {
        User user = userRepository.findByPhone(phone);
        List<Object[]> list = userRepository.analysisSleepMemberDistribution(getOrgPath(user), RoleType.高级合伙人);
        return fillResult(list);
    }

    @Override
    public Map<String, List<EntryDTO<String, Long>>> analysisImmediateTeamOrderSaleVolume(String phone) {
        User user = userRepository.findByPhone(phone);
        Map<String, Long> ordinaryOrderSaleVolume = fillVolumeResult(orderRepository.analysisImmediateTeamOrdinaryOrderSaleVolume(getOrgPath(user), OrderStatus.已支付, DateUtil.getYearBeginDate(), DateUtil.getCurrentDate()));
        List<User> userList = userRepository.findByOneLevelOrgPath(getOrgPath(user));
        Map<String, Long> serviceOrderSaleVolume = userList.stream().map(zxUser -> zxUser.getServiceOrderList()).flatMap(set -> set.stream())
                .filter(order -> order.getOrderStatus().getCode() >= OrderStatus.已支付.getCode())
                .collect(Collectors.groupingBy(order -> order.getMonth().length() == 1 ? "0" + order.getMonth() : order.getMonth(), Collectors.summingLong(Order::getProductNum)));
        List<String> monthList = DateUtil.getYTDMonth();
        Map<String, List<EntryDTO<String, Long>>> map = new HashMap<>();
        map.put("直属团队进货量", fillAnalysisOrderVolume(ordinaryOrderSaleVolume, monthList));
        map.put("直属团队销货量", fillAnalysisOrderVolume(serviceOrderSaleVolume, monthList));
        return map;
    }


    @Override
    public Map<String, List<EntryDTO<String, Double>>> analysisImmediateTeamOrderShare(String phone) {
        User user = userRepository.findByPhone(phone);
        Map<String, Long> ordinaryOrderSaleVolume = fillVolumeResult(orderRepository.analysisImmediateTeamOrdinaryOrderSaleVolume(getOrgPath(user), OrderStatus.已支付, DateUtil.getYearBeginDate(), DateUtil.getCurrentDate()));
        List<User> userList = userRepository.findByOneLevelOrgPath(getOrgPath(user));
        Map<String, Long> serviceOrderSaleVolume = userList.stream().map(zxUser -> zxUser.getServiceOrderList()).flatMap(set -> set.stream())
                .filter(order -> order.getOrderStatus().getCode() >= OrderStatus.已支付.getCode())
                .collect(Collectors.groupingBy(order -> order.getMonth().length() == 1 ? "0" + order.getMonth() : order.getMonth(), Collectors.summingLong(Order::getProductNum)));
        List<String> monthList = DateUtil.getYTDMonth();
        Map<String, List<EntryDTO<String, Double>>> map = new HashMap<>();
        map.put("直属团队进货量环比", fillAnalysisOrderShare(ordinaryOrderSaleVolume, monthList));
        map.put("直属团队销货量环比", fillAnalysisOrderShare(serviceOrderSaleVolume, monthList));
        return map;
    }


    @Override
    public Map<String, List<EntryDTO<String, Long>>> analysisOrderVolume(String phone) {
        User user = userRepository.findByPhone(phone);
        Map<String, Long> ordinaryOrderSaleVolume = fillVolumeResult(orderRepository.analysisOrdinaryOrderSaleVolume(user, OrderStatus.已支付, DateUtil.getYearBeginDate(), DateUtil.getCurrentDate()));
        Set<Order> orderList = user.getServiceOrderList();
        List<Long> orderIds = new ArrayList<>();
        if (orderList.size() > 0) {
            orderList.forEach(order -> orderIds.add(order.getId()));
        }
        List<Object[]> result;
        if (orderIds.size() > 0) {
            result = orderRepository.analysisServiceOrderSaleVolume(orderIds, OrderStatus.已支付, DateUtil.getYearBeginDate(), DateUtil.getCurrentDate());
        } else {
            result = new ArrayList<>();
        }
        Map<String, Long> serviceOrderSaleVolume = fillVolumeResult(result);
        List<String> monthList = DateUtil.getYTDMonth();
        Map<String, List<EntryDTO<String, Long>>> map = new HashMap<>();
        map.put("个人进货量", fillAnalysisOrderVolume(ordinaryOrderSaleVolume, monthList));
        map.put("个人销货量", fillAnalysisOrderVolume(serviceOrderSaleVolume, monthList));
        return map;
    }

    @Override
    public Map<String, List<EntryDTO<String, Double>>> analysisOrderShare(String phone) {
        User user = userRepository.findByPhone(phone);
        Map<String, Long> ordinaryOrderSaleVolume = fillVolumeResult(orderRepository.analysisOrdinaryOrderSaleVolume(user, OrderStatus.已支付, DateUtil.getYearBeginDate(), DateUtil.getCurrentDate()));
        Set<Order> orderList = user.getServiceOrderList();
        List<Long> orderIds = new ArrayList<>();
        if (orderList.size() > 0) {
            orderList.forEach(order -> orderIds.add(order.getId()));
        }
        List<Object[]> result;
        if (orderIds.size() > 0) {
            result = orderRepository.analysisServiceOrderSaleVolume(orderIds, OrderStatus.已支付, DateUtil.getYearBeginDate(), DateUtil.getCurrentDate());
        } else {
            result = new ArrayList<>();
        }
        Map<String, Long> serviceOrderSaleVolume = fillVolumeResult(result);
        List<String> monthList = DateUtil.getYTDMonth();
        Map<String, List<EntryDTO<String, Double>>> map = new HashMap<>();
        map.put("个人进货量环比", fillAnalysisOrderShare(ordinaryOrderSaleVolume, monthList));
        map.put("个人销货量环比", fillAnalysisOrderShare(serviceOrderSaleVolume, monthList));
        return map;
    }

    private List<EntryDTO<String, Double>> fillAnalysisOrderShare(Map<String, Long> serviceOrderSaleVolume, List<String> monthList) {
        List<EntryDTO<String, Double>> orderSaleShareList = new ArrayList<>();
        List<EntryDTO<String, Long>> entryDTOList = fillAnalysisOrderVolume(serviceOrderSaleVolume, monthList);
        for (int i = 0; i < entryDTOList.size(); i++) {
            if (i == 0) {
                orderSaleShareList.add(new EntryDTO<>(entryDTOList.get(i).getKey(), BigDecimal.valueOf(0).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()));
            } else {
                EntryDTO<String, Long> cur = entryDTOList.get(i);
                EntryDTO<String, Long> pre = entryDTOList.get(i - 1);
                BigDecimal cur_volume = new BigDecimal(cur.getValue());
                BigDecimal pre_volume = new BigDecimal(pre.getValue());
                Double share = cur_volume.longValue() == 0 ?
                        BigDecimal.valueOf(cur_volume.longValue()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() :
                        cur_volume.subtract(pre_volume).divide(cur_volume).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                orderSaleShareList.add(new EntryDTO<>(cur.getKey(), share));
            }
        }
        return orderSaleShareList;
    }

    private List<EntryDTO<String, Long>> fillAnalysisOrderVolume(Map<String, Long> serviceOrderSaleVolume, List<String> monthList) {
        List<EntryDTO<String, Long>> orderSaleVolumeList = new ArrayList<>();
        monthList.forEach(month -> {
            EntryDTO entryDTO;
            if (serviceOrderSaleVolume.containsKey(month)) {
                entryDTO = new EntryDTO(month, serviceOrderSaleVolume.get(month));
            } else {
                entryDTO = new EntryDTO(month, Long.valueOf(0));
            }
            orderSaleVolumeList.add(entryDTO);
        });
        return orderSaleVolumeList;
    }

    private Map<String, Long> fillVolumeResult(List<Object[]> list) {
        Map<String, Long> result = new HashMap<>();
        list.forEach(objects -> {
            String month;
            if (Integer.parseInt((String) objects[0]) < 10) {
                month = "0" + objects[0];
            } else {
                month = (String) objects[0];
            }
            result.put(month, (Long) objects[1]);
        });
        return result;
    }

    private Map<String, Long> fillResult(List<Object[]> list) {
        Map<String, Long> result = new LinkedHashMap<>();
        result.put(RoleType.高级合伙人.getName(), Long.valueOf(0));
        result.put(RoleType.合伙人.getName(), Long.valueOf(0));
        result.put(RoleType.VIP.getName(), Long.valueOf(0));
        list.forEach(objects -> {
            RoleType roleType = (RoleType) objects[0];
            if (result.containsKey(roleType.getName())) {
                result.put(roleType.getName(), (Long) objects[1]);
            }
        });
        return result;
    }
}
