package my.mall.api.mall;

import io.swagger.annotations.*;
import my.mall.api.mall.param.MallUserLoginParam;
import my.mall.api.mall.param.MallUserRegisterParam;
import my.mall.api.mall.param.MallUserUpdateParam;
import my.mall.common.Constants;
import my.mall.common.ServiceResultEnum;
import my.mall.config.annotation.TokenToMallUser;
import my.mall.api.mall.vo.MallUserVO;
import my.mall.entity.User;
import my.mall.service.MallUserService;
import my.mall.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@Api(value = "mallUser", tags = "图书商城用户操作相关接口")
@RequestMapping("/api/mallUser")
public class MallPersonalAPI {

    @Resource
    private MallUserService mallUserService;

    private static final Logger logger = LoggerFactory.getLogger(MallPersonalAPI.class);

    @PostMapping("/user/login")
    @ApiOperation(value = "登录接口", notes = "返回token")
    public Result<String> login(@RequestBody @Valid MallUserLoginParam mallUserLoginParam) {
        if (!NumberUtil.isPhone(mallUserLoginParam.getLoginName())){
            return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_NAME_IS_NOT_PHONE.getResult());
        }
        String loginResult = mallUserService.login(mallUserLoginParam.getLoginName(), mallUserLoginParam.getPasswordMd5());

        logger.info("login api,loginName={},loginResult={}", mallUserLoginParam.getLoginName(), loginResult);

        //登录成功
        if (!StringUtils.isEmpty(loginResult) && loginResult.length() == Constants.TOKEN_LENGTH) {
            Result result = ResultGenerator.genSuccessResult();
            result.setData(loginResult);
            return result;
        }
        //登录失败
        return ResultGenerator.genFailResult(loginResult);
    }


    @PostMapping("/user/logout")
    @ApiOperation(value = "登出接口", notes = "清除token")
    public Result<String> logout(@TokenToMallUser User loginUser) {
        Boolean logoutResult = mallUserService.logout(loginUser.getUserId());

        logger.info("logout api,loginUser={}", loginUser.getUserId());


        //登出成功
        if (logoutResult) {
            return ResultGenerator.genSuccessResult();
        }
        //登出失败
        return ResultGenerator.genFailResult("logout error");
    }


    @PostMapping("/user/register")
    @ApiOperation(value = "用户注册", notes = "")
    public Result register(@RequestBody @Valid MallUserRegisterParam mallUserRegisterParam) {
        System.out.println("pass");
        if (!NumberUtil.isPhone(mallUserRegisterParam.getLoginName())){
            return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_NAME_IS_NOT_PHONE.getResult());
        }
        String registerResult = mallUserService.register(mallUserRegisterParam.getLoginName(), mallUserRegisterParam.getPassword(),mallUserRegisterParam.getNickName());

        logger.info("register api,loginName={},loginResult={}", mallUserRegisterParam.getLoginName(), registerResult);

        //注册成功
        if (ServiceResultEnum.SUCCESS.getResult().equals(registerResult)) {
            return ResultGenerator.genSuccessResult();
        }
        //注册失败
        return ResultGenerator.genFailResult(registerResult);
    }

    @PutMapping("/user/info")
    @ApiOperation(value = "修改用户信息", notes = "")
    public Result updateInfo(@RequestBody @ApiParam("用户信息") MallUserUpdateParam mallUserUpdateParam, @TokenToMallUser User loginUser) {
        Boolean flag = mallUserService.updateUserInfo(mallUserUpdateParam, loginUser.getUserId());
        if (flag) {
            //返回成功
            Result result = ResultGenerator.genSuccessResult();
            return result;
        } else {
            //返回失败
            Result result = ResultGenerator.genFailResult("修改失败");
            return result;
        }
    }

    @GetMapping("/user/info")
    @ApiOperation(value = "获取用户信息", notes = "")
    public Result<MallUserVO> getUserDetail(@TokenToMallUser User loginUser) {
        //已登录则直接返回
        MallUserVO mallUserVO = new MallUserVO();
        BeanUtil.copyProperties(loginUser, mallUserVO);
        return ResultGenerator.genSuccessResult(mallUserVO);
    }
}
