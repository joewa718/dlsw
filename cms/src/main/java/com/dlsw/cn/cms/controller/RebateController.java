package com.dlsw.cn.cms.controller;

import com.dlsw.cn.common.dto.OrderDTO;
import com.dlsw.cn.common.dto.PageDTO;
import com.dlsw.cn.common.dto.RebateDTO;
import com.dlsw.cn.cms.service.OrderService;
import com.dlsw.cn.cms.service.RebateService;
import com.dlsw.cn.cms.vo.RebateVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rebate")
public class RebateController extends BaseController {
    @Autowired
    OrderService orderService;

    @Autowired
    RebateService rebateService;

    @ApiOperation(value = "获取返利列表")
    @RequestMapping(value = "/fetchPage", method = RequestMethod.POST)
    public ResponseEntity<PageDTO<RebateDTO>> getRebateList(@ModelAttribute RebateVo rebateVo){
        PageDTO<RebateDTO> pageDTO = rebateService.fetchPage(rebateVo);
        return new ResponseEntity<>(pageDTO,HttpStatus.OK);
    }

    @ApiOperation(value = "更新返利状态")
    @RequestMapping(value = "/setStatus", method = RequestMethod.POST)
    public  ResponseEntity setStatus(@RequestParam("ids") String ids){
        rebateService.setStatus(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @ApiOperation(value = "获取返利明细")
    @RequestMapping(value = "/getRebate", method = RequestMethod.POST)
    public ResponseEntity<OrderDTO> getOrder(@RequestParam long orderId) {
        OrderDTO orderDTO = orderService.getOrder(orderId);
        return new ResponseEntity<>(orderDTO, HttpStatus.OK);
    }
}
