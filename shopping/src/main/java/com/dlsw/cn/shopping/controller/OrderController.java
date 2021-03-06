package com.dlsw.cn.shopping.controller;

import com.dlsw.cn.common.dto.OrderDTO;
import com.dlsw.cn.shopping.service.OrderService;
import com.dlsw.cn.shopping.service.ProductService;
import com.dlsw.cn.shopping.service.UserService;
import com.dlsw.cn.shopping.vo.OrderVo;
import com.dlsw.cn.shopping.vo.PayCertificateVo;
import com.dlsw.cn.common.enumerate.OrderStatus;
import com.dlsw.cn.common.enumerate.OrderType;
import com.dlsw.cn.common.po.Product;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.naming.OperationNotSupportedException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/order")
public class OrderController extends BaseController {
    @Autowired
    OrderService orderService;
    @Autowired
    UserService userService;
    @Autowired
    ProductService productService;
    @ApiOperation(value = "下订单")
    @RequestMapping(value = "/applyOrder", method = RequestMethod.POST)
    public ResponseEntity<Object> applyOrder(@ModelAttribute OrderVo orderVo) {
        String phone = super.getCurrentUser().getUsername();
        try {
            Product product = productService.getProductById(orderVo.getProductId());
            if (product == null) {
                throw new OperationNotSupportedException("无法找到对应的商品");
            }
            Object order = orderService.applyOrder(phone, orderVo);
            return new ResponseEntity<>(order,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @ApiOperation(value = "线下订单提交给推荐人")
    @RequestMapping(value = "/savePayCert", method = RequestMethod.POST)
    public ResponseEntity<Object> submitOrder(@ModelAttribute PayCertificateVo payCertificateVo) {
        String phone = super.getCurrentUser().getUsername();
        try {
            OrderDTO orderDTO = orderService.savePayCert(phone,payCertificateVo);
            return new ResponseEntity<>(orderDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "推荐人确认订单")
    @RequestMapping(value = "/sureOrder", method = RequestMethod.POST)
    public ResponseEntity<Object> sureOrder(@RequestParam long orderId) {
        String phone = super.getCurrentUser().getUsername();
        try {
            OrderDTO orderDTO = orderService.sureOrder(phone,orderId);
            return new ResponseEntity<>(orderDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "获取订单明细")
    @RequestMapping(value = "/getOrder", method = RequestMethod.POST)
    public ResponseEntity<OrderDTO> getOrder(@RequestParam long orderId) {
        String phone = super.getCurrentUser().getUsername();
        OrderDTO orderDTO = orderService.getOrder(phone, orderId);
        return new ResponseEntity<>(orderDTO, HttpStatus.OK);
    }

    @ApiOperation(value = "根据订单的类型汇总订单各种状态的量")
    @RequestMapping(value = "/groupByOrderTypeAndOrderStatusAndUser", method = RequestMethod.POST)
    public ResponseEntity<Map<String,Long>> groupByOrderTypeAndOrderStatusAndUser(@RequestParam OrderType orderType) {
        String phone = super.getCurrentUser().getUsername();
        Map<String,Long> map =  orderService.summaryOrderCount(phone,orderType);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @ApiOperation(value = "根据订单状态查询订单")
    @RequestMapping(value = "/findByOrderTypeAndOrderStatusAndUser", method = RequestMethod.POST)
    public ResponseEntity<List<OrderDTO>> findByOrderTypeAndOrderStatusAndUser(@RequestParam OrderType orderType, @RequestParam OrderStatus orderStatus) {
        String phone = super.getCurrentUser().getUsername();
        List<OrderDTO> orderDTOList = orderService.getOrderList(orderType, orderStatus, phone);
        return new ResponseEntity<>(orderDTOList, HttpStatus.OK);
    }

    @ApiOperation(value = "根据订单类型获取订单总量")
    @RequestMapping(value = "/countByOrderTypeAndUser", method = RequestMethod.POST)
    public ResponseEntity<Integer> countByOrderTypeAndUser(@RequestParam OrderType orderType) {
        String phone = super.getCurrentUser().getUsername();
        Integer count = orderService.getOrderCount(phone, orderType);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
}
