package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
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
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private IUserService iUserService;

    /**
     * @Description: 用户登录, 同时将用户信息写入session
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
        return ServerResponse.createBySuccessMessage("用户注销成功");
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
        return ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户信息");
    }

    /**
     * @Description: 根据用户名获取密码问题
     * @Param: [username]
     * @return: com.mmall.common.ServerResponse<java.lang.String>
     */
    @RequestMapping(value = "forget_get_question.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String username) {
        return iUserService.selectQuestion(username);
    }

    /**
     * @Description: 根据用户名、密码问题、提供的答案去数据库查找密码答案是否正确
     * @Param: [username, question, answer]
     * @return: 答案正确则返回用户的Token（重置密码时需要Token），错误则返回问题答案错误
     */
    @RequestMapping(value = "forget_check_answer.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String username, String question, String answer) {
        return iUserService.checkAnswer(username, question, answer);
    }

    /**
     * @Description: 根据用户名，用户新密码，用户的token去重置用户的密码（此时用户处于未登录状态）
     * @Param: [username, passwordNew, forgetToken]
     * @return: com.mmall.common.ServerResponse<java.lang.String>
     */
    @RequestMapping(value = "forget_reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken) {
        return iUserService.forgetResetPassword(username, passwordNew, forgetToken);
    }

    /**
     * @Description: 在登录状态下，根据旧密码和新密码来重置用户密码
     * @Param: [session, passwordOld, passwordNew]
     * @return: com.mmall.common.ServerResponse<java.lang.String>
     */
    @RequestMapping(value = "reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPassword(HttpSession session, String passwordOld, String passwordNew) {
        //从session中获取当前用户的信息
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        return iUserService.resetPassword(passwordOld, passwordNew, user);
    }

    /**
     * @Description: 在登录状态下，更新用户的个人信息，若成功，并更新到session中
     * @Param: [session, user]
     * @return: com.mmall.common.ServerResponse<com.mmall.pojo.User>
     */
    @RequestMapping(value = "update_information.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> update_information(HttpSession session, User user) {
        //从session中获取当前用户的信息
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        user.setId(currentUser.getId());//传进来的user是没有id的，要去session中获取
        user.setUsername(currentUser.getUsername());
        ServerResponse<User> response = iUserService.updateInformation(user);
        if (response.isSuccess()) {
            //更新用户信息成功，将更新的信息更新到session中
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }

    /**
     * @Description: 用户在登录状态下，通过session中的用户id返回用户的信息（密码除外）
     * @Param: [session]
     * @return: com.mmall.common.ServerResponse<com.mmall.pojo.User>
     */
    @RequestMapping(value = "get_information.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> get_information(HttpSession session) {
        User currenyUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currenyUser == null) {
            //用户未登录，需要将status=10，告诉前端需要强制登录
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录，需要强制登录status=10");
        }
        return iUserService.getInformation(currenyUser.getId());
    }
}
