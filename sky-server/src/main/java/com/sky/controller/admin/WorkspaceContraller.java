package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/workspace")
@Api(tags = "工作台")
@Slf4j
public class WorkspaceContraller {
    @Autowired
    private WorkspaceService workspaceService;
    @GetMapping("/businessData")
    @ApiOperation("运营数据")
    public Result<BusinessDataVO> businessData() {
        log.info("运营数据");
        BusinessDataVO businessDataVO = workspaceService.businessData();
        return Result.success(businessDataVO);
    }
}
