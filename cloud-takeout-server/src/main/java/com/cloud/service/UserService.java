package com.cloud.service;

import com.cloud.dto.UserLoginDTO;
import com.cloud.vo.UserLoginVO;

public interface UserService {
    UserLoginVO login(UserLoginDTO userLoginDTO);
}
