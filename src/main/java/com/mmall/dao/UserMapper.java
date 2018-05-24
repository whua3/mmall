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

    //返回用户名为username，密码问题为question，问题答案为answer的记录数，有记录数就表示查到了answer
    int checkAnswer(@Param("username") String username, @Param("question") String question, @Param("answer") String answer);

    //根据用户名和新密码来更新密码，返回生效的行数
    int updatePasswordByUsername(@Param("username") String username, @Param("passwordNew") String passwordNew);

    //根据userId和password来查找数据库中是否有记录
    int checkPassword(@Param("password") String password, @Param("userId") Integer userId);

    //查找数据库中是否已经有了这个email
    int checkEmailByUserId(@Param("email") String email, @Param("userId") Integer userId);
}