package com.cloud.controller.admin;

import com.cloud.constant.JwtClaimsConstant;
import com.cloud.dto.EmployeeDTO;
import com.cloud.dto.EmployeeLoginDTO;
import com.cloud.dto.EmployeePageQueryDTO;
import com.cloud.entity.Employee;
import com.cloud.properties.JwtProperties;
import com.cloud.result.PageResult;
import com.cloud.result.Result;
import com.cloud.service.EmployeeService;
import com.cloud.utils.JwtUtil;
import com.cloud.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags="员工相关接口")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation(value="员工登录")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation(value="员工退出登录")
    public Result<String> logout() {
        return Result.success();
    }

    @PostMapping
    @ApiOperation("新增员工")
    public Result save(@RequestBody EmployeeDTO employeeDTO){ //只有前端传的JSON格式的参数，采用加@RequestBody
        log.info("新增员工：{}",employeeDTO);
        System.out.println("当前线程id:"+Thread.currentThread().getId());
        employeeService.save(employeeDTO);
        return Result.success();
    }
    @GetMapping("/page")
    @ApiOperation("员工分页查询")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO){
        log.info("员工分页查询，参数+",employeePageQueryDTO);
        PageResult pageResult=employeeService.pageQuery(employeePageQueryDTO);
        return Result.success(pageResult);
    }
    //TODO 这里的路径有疑问，为什么不用update？这个id是怎么获取的？
    @PostMapping("/status/{status}")
    @ApiOperation("启用禁用员工账号")
    public Result startOrstop(@PathVariable Integer status,Long id){
        log.info("启用禁用员工账号:{},{}",status,id);
        employeeService.startOrstop(status,id);
        return Result.success();
    }
    @GetMapping("/{id}")
    @ApiOperation("根据员工ID查询员工信息")  //这个的作用就是在点击修改后，回显员工的信息
    public Result<Employee> getByID(@PathVariable long id){
        Employee employee=employeeService.getById(id);
        return Result.success(employee);
    }
    @PutMapping
    @ApiOperation("修改员工信息")
    public Result update(@RequestBody EmployeeDTO employeeDTO){
        employeeService.update(employeeDTO);
        return Result.success(employeeDTO);
    }

}
