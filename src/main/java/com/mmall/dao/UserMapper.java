package com.mmall.dao;

import com.mmall.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    //返回数据库中用户名为username的数量
    int checkUsername(String username);

    //返回数据库中邮箱为email的数量
    int checkEmail(String email);

    //返回用户登录之后的信息
    //注意：Mybatis在传递多个参数的时候，要用到@Param注解，同时在XML文件中写SQL语句的时候，变量对应的为@Param("XXX")中的XXX
    User selectLogin(@Param("username") String username, @Param("password") String password);

    //返回用户username的密码问题
    String selectQuestionByUsername(String username);
}