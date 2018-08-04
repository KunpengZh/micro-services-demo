package com.ibm.cio.sets.procurement.controller;


import com.auth0.jwt.JWT;
import com.ibm.cio.sets.procurement.model.AuthenticatedUser;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/user")
public class UserController {
    private static final String ACCESS_TOKEN = "access-token";

    @RequestMapping(value = "/getUser", method = RequestMethod.GET)
    public String getUser(@RequestParam("userId") String userId) {

        System.out.println("this is running within the application");
        System.out.println(userId);
        return "Hello Controller";
    }

    @RequestMapping(value = "/getUserProfile", method = RequestMethod.GET)
    public AuthenticatedUser getUserProfile(HttpServletRequest servletRequest) {

        final String token = getToken(servletRequest);
        if (StringUtils.isNotBlank(token)) {
            return convertAccessTokenToAuthenticatedUser(token);
        } else {
            return new AuthenticatedUser();
        }

    }


    private AuthenticatedUser convertAccessTokenToAuthenticatedUser(String encodedData) {
        final JWT jwt = JWT.decode(encodedData);
        AuthenticatedUser user = new AuthenticatedUser();
        user.setIss(jwt.getClaim("iss").asString());
        user.setSub(jwt.getClaim("sub").asString());
        user.setName(jwt.getClaim("name").asString());
        user.setExp(jwt.getClaim("exp").asString());
        return user;
    }

    private String getToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (ACCESS_TOKEN.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return (String) request.getHeader(ACCESS_TOKEN);
    }
}