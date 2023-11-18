package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import com.sky.vo.UserLoginVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WeChatProperties weChatProperties;
    public static final String WX_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";

    @Override
    public UserLoginVO login(UserLoginDTO userLoginDTO) {
        //获取用户的openid
        String openid = getOpenid(userLoginDTO.getCode());
        //判断openid是否为空
        if (openid == null) {
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        //判断该用户是否在用户表User中
        User user = userMapper.getByOpenid(openid);
        if(user == null){
            //如果不存在，则转到注册逻辑
           user= User.builder()
                   .openid(openid)
                   .createTime(LocalDateTime.now())
                   .build();
            //保存该对象
            userMapper.insert(user);
        }
        UserLoginVO userLoginVO = new UserLoginVO();
        BeanUtils.copyProperties(user, userLoginVO);
        //返回该用户对象
        return userLoginVO;
    }
    public String getOpenid(String code){
        //构造请求参数
        Map<String, String> params = new HashMap<>();
        params.put("appid", weChatProperties.getAppid());
        params.put("secret", weChatProperties.getSecret());
        params.put("js_code", code);
        params.put("grant_type", "authorization_code");//固定写法
        //调用微信服务接口，获取用户的openid
        String json = HttpClientUtil.doGet(WX_LOGIN_URL, params);
        //包装成json数据
        JSONObject jsonObj = JSON.parseObject(json);
        return jsonObj.getString("openid");
    }
}
