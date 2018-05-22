package com.mmall.common;

/**
 * @program: mmall
 * @description:
 * @author: whua
 * @create: 2018-05-22 20:30
 **/
public class Const {

    public static final String CURRENT_USER = "currentUser";

    public static final String USERNAME = "username";
    public static final String EMAIL = "email";


    public interface Role {
        int ROLE_CUSTOMER = 0;//普通用户
        int ROLE_ADMIN = 1;//管理员
    }

}
