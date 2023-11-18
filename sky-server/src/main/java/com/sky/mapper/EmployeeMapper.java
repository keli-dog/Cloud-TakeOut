package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);
    /**
     * 插入员工数据
     * @param employee
     *
     */
    @Insert("INSERT INTO employee (name, username, password, phone, sex, id_number, create_time, update_time, create_user, update_user, status) " +
            "VALUES (#{name}, #{username}, #{password}, #{phone}, #{sex}, #{idNumber}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser}, #{status})")
    //这里的属性名和实体类中属性名不一致，但是在 application.yml中配置开启了驼峰命名法，就可以自动匹配上
    void insert(Employee employee);

    //这里就不用注解sql语句了,因为是动态sql，就需要用到映射文件，resource/mapper/EmployeeMapper.xml
    Page<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /*
    * 根据id动态修改属性*/
    void update(Employee employee);
    @Select("select * from employee where id=#{id}")
    Employee getById(long id);
}
