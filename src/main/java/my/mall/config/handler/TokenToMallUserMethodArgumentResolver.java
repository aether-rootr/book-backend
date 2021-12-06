package my.mall.config.handler;

import my.mall.common.Constants;
import my.mall.common.MallException;
import my.mall.common.ServiceResultEnum;
import my.mall.config.annotation.TokenToAdminUser;
import my.mall.config.annotation.TokenToMallUser;
import my.mall.dao.AdminUserMapper;
import my.mall.dao.AdminUserTokenMapper;
import my.mall.dao.MallUserMapper;
import my.mall.dao.MallUserTokenMapper;
import my.mall.entity.AdminUser;
import my.mall.entity.AdminUserToken;
import my.mall.entity.User;
import my.mall.entity.MallUserToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 登录时效
 */
@Component
public class TokenToMallUserMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private MallUserMapper mallUserMapper;
    @Autowired
    private MallUserTokenMapper mallUserTokenMapper;


    public TokenToMallUserMethodArgumentResolver() {
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        if (parameter.hasParameterAnnotation(TokenToMallUser.class)) {
            return true;
        }
        return false;
    }

    /**
     * 获取拦截器中获取的当前用户信息
     * @param parameter
     * @param mavContainer
     * @param webRequest
     * @param binderFactory
     * @return
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        if (parameter.getParameterAnnotation(TokenToMallUser.class) instanceof TokenToMallUser) {
            User user = null;
            String token = webRequest.getHeader("token");
            if (null != token && !"".equals(token) && token.length() == Constants.TOKEN_LENGTH) {
                MallUserToken mallUserToken = mallUserTokenMapper.selectByToken(token);
                if (mallUserToken == null || mallUserToken.getExpireTime().getTime() <= System.currentTimeMillis()) {
                    MallException.fail(ServiceResultEnum.TOKEN_EXPIRE_ERROR.getResult());
                }
                user = mallUserMapper.selectByPrimaryKey(mallUserToken.getUserId());
                if (user == null) {
                    MallException.fail(ServiceResultEnum.USER_NULL_ERROR.getResult());
                }
                if (user.getLockedFlag().intValue() == 1) {
                    MallException.fail(ServiceResultEnum.LOGIN_USER_LOCKED_ERROR.getResult());
                }
                return user;
            } else {
                MallException.fail(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
            }
        }
        return null;
    }

    public static byte[] getRequestPostBytes(HttpServletRequest request)
            throws IOException {
        int contentLength = request.getContentLength();
        if (contentLength < 0) {
            return null;
        }
        byte buffer[] = new byte[contentLength];
        for (int i = 0; i < contentLength; ) {
            int readlen = request.getInputStream().read(buffer, i,
                    contentLength - i);
            if (readlen == -1) {
                break;
            }
            i += readlen;
        }
        return buffer;
    }

}
