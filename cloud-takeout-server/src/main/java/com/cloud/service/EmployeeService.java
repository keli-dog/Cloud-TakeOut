package com.cloud.service;

import com.cloud.dto.EmployeeDTO;
import com.cloud.dto.EmployeeLoginDTO;
import com.cloud.dto.EmployeePageQueryDTO;
import com.cloud.entity.Employee;
import com.cloud.result.PageResult;

public interface EmployeeService {
    Employee login(EmployeeLoginDTO employeeLoginDTO);
    void save(EmployeeDTO employeeDTO);
    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);
    void startOrstop(Integer status, Long id);
    Employee getById(long id);
    void update(EmployeeDTO employeeDTO);
}
