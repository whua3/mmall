package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @program: mmall
 * @description:
 * @author: whua
 * @create: 2018-05-22 18:50
 **/
@Controller
@RequestMapping("/usr/")
public class UserController {

    @Autowired
    private IUserService iUserService;

    /**
     * @Description: 用户登录
     * @Param: [username, password, session]
     * @return: java.lang.Object
     */
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    //@ResponseBody:将controller的方法返回的对象通过适当的转换器转换为指定的格式之后，写入到response对象的body区，通常用来返回JSON数据或者是XML
    public ServerResponse<User> login(String username, String password, HttpSession session) {
        //service-->mybatis-->dao
        ServerResponse<User> response = iUserService.login(username, password);
        if (response.isSuccess()) {
            //session里写入key-value对，key=常量“currentUser”,value=使用username和password返回的用户信息
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }

    /**
     * @Description: 用户登出，删除session中的当前用户
     * @Param: [session]
     * @return: com.mmall.common.ServerResponse<java.lang.String>
     */
    @RequestMapping(value = "logout.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }

    /**
     * @Description: 用户注册
     * @Param: [user]
     * @return: com.mmall.common.ServerResponse<java.lang.String>
     */
    @RequestMapping(value = "register.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user) {
        return iUserService.register(user);
    }

    /**
     * @Description: 验证用户名和email，防止恶意用户通过接口调用用户注册
     * @Param: [str, type] ，用type来判断str是用户名还是email
     * @return: com.mmall.common.ServerResponse<java.lang.String>
     */
    @RequestMapping(value = "check_valid.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkValid(String str, String type) {
        return iUserService.checkValid(str, type);
    }

    /**
     * @Description: 从session中获取用户信息
     * @Param: [session]
     * @return: com.mmall.common.ServerResponse<com.mmall.pojo.User>
     */
    @RequestMapping(value = "get_user_info.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user != null) {
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createBySuccessMessage("用户未登录，无法获取当前用户信息");
    }

    /** 
    * @Description: 根据用户名获取密码问题 
    * @Param: [username] 
    * @return: com.mmall.common.ServerResponse<java.lang.String> 
    */ 
    @RequestMapping(value = "forget_get_question.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String username){
        return iUserService.selectQuestion(username);
    }


}
