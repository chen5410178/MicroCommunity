package com.java110.utils.util;

import com.java110.utils.exception.FilterException;

import javax.servlet.http.HttpServletRequest;

public class TokenUtil {

    /**
     * 获取TOKEN
     *
     * @param request
     * @return
     */
    public static String getToken(HttpServletRequest request) {
        String token = "";
        String authorization = request.getHeader("Authorization");

        if( StringUtil.isEmpty(authorization)){
            return token;
        }
        if(authorization.length()> 7) {
            token = authorization.substring("Bearer ".length());
        }

        return token;
    }
}
