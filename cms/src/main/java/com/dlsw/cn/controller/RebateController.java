package com.dlsw.cn.controller;

import com.dlsw.cn.po.Rebate;
import com.dlsw.cn.service.RebateService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/report")
public class RebateController extends BaseController {

    @Autowired
    RebateService rebateService;

    @ApiOperation(value = "获取返利列表")
    @RequestMapping(value = "/getRebateList", method = RequestMethod.POST)
    public ResponseEntity<List<Rebate>> getRebateList(){
        List<Rebate> rebateList = rebateService.getRebateList(null);
        return new ResponseEntity<>(rebateList,HttpStatus.OK);
    }

    @ApiOperation(value = "更新返利状态")
    @RequestMapping(value = "/updateRebate", method = RequestMethod.POST)
    public  ResponseEntity<Map<String,Long>> updateRebate(){
        rebateService.updateRebate(null);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
