package com.dlsw.cn.web.controller;

import com.dlsw.cn.web.dto.OrderDTO;
import com.dlsw.cn.web.dto.PageDTO;
import com.dlsw.cn.web.dto.RebateDTO;
import com.dlsw.cn.web.service.OrderService;
import com.dlsw.cn.web.vo.OrderVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
public class OrderController extends BaseController {
    @Autowired
    OrderService orderService;

    @ApiOperation(value = "获取订单列表")
    @RequestMapping(value = "/fetchPage", method = RequestMethod.POST)
    public ResponseEntity<PageDTO<RebateDTO>> getRebateList(@ModelAttribute OrderVo orderVo) {
        PageDTO<RebateDTO> pageDTO = orderService.fetchPage(orderVo);
        return new ResponseEntity<>(pageDTO, HttpStatus.OK);
    }

    @ApiOperation(value = "确认订单")
    @RequestMapping(value = "/sureOrder", method = RequestMethod.POST)
    public ResponseEntity sureOrder(@RequestParam("orderId") Long orderId) {
        OrderDTO orderDTO = orderService.sureOrder(orderId);
        return new ResponseEntity<>(orderDTO, HttpStatus.OK);
    }


    @ApiOperation(value = "获取订单明细")
    @RequestMapping(value = "/getOrder", method = RequestMethod.POST)
    public ResponseEntity<OrderDTO> getOrder(@RequestParam long orderId) {
        OrderDTO orderDTO = orderService.getOrder(orderId);
        return new ResponseEntity<>(orderDTO, HttpStatus.OK);
    }
}
