package com.dlsw.cn.controller;

import com.dlsw.cn.dto.PageDTO;
import com.dlsw.cn.dto.RebateDTO;
import com.dlsw.cn.service.RebateService;
import com.dlsw.cn.vo.RebateVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rebate")
public class RebateController extends BaseController {

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

}
