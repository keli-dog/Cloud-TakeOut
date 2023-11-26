package com.cloud.controller.user;

import com.cloud.constant.JwtClaimsConstant;
import com.cloud.dto.UserLoginDTO;
import com.cloud.properties.JwtProperties;
import com.cloud.result.Result;
import com.cloud.service.UserService;
import com.cloud.utils.JwtUtil;
import com.cloud.vo.UserLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user/user")
@Api(tags = "用户相关接口")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtProperties jwtProperties;

    @PostMapping("/login")
    @ApiOperation(value = "微信登录", notes = "用户登录")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) {
        log.info("userLoginDTO={}", userLoginDTO);
        //调用service
        UserLoginVO userLoginVO = userService.login(userLoginDTO);
        //构建token的用户
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, userLoginVO.getId());
        //生成token
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);
        //设置token
        userLoginVO.setToken(token);
        //返回结果
        return Result.success(userLoginVO);
    }
}
