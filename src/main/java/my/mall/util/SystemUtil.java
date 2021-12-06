package my.mall.util;

import java.math.BigInteger;
import java.security.MessageDigest;

public class SystemUtil {

    private SystemUtil() {
    }


    /**
     * 登录或注册成功后,生成保持用户登录状态会话token值
     *
     * @param src:为用户最新一次登录时的now()+user.id+random(4)
     * @return
     */
    public static String genToken(String src,String str) {
        if (null == src || "".equals(src)) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(src.getBytes());
            String result = new BigInteger(1, md.digest()).toString(16);
            if (result.length() == 31) {
                result = result + "-";
            }
            StringBuilder sb = new StringBuilder(result);
            result = sb.replace(0,str.length(),str).toString();
            System.out.println("result:"+result);
            return result;
        } catch (Exception e) {
            return null;
        }
    }
}
