package com.ibm.cio.sets.procurement.identify;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class IdentityFilter implements Filter {
    private String internalSecret = "YourveryveryverySecretInternalKey";



    private Jackson2ObjectMapperFactoryBean factoryBean;

    @Autowired
    public void setFactoryBean(Jackson2ObjectMapperFactoryBean factoryBean) {
        this.factoryBean = factoryBean;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (!(request instanceof HttpServletRequest)) {
            chain.doFilter(request, response);
            return;
        }

        final HttpServletRequest httpRequest = (HttpServletRequest) request;

        final String requestSecret = (String) httpRequest.getHeader("app-shared-secret");

        if (internalSecret.equals(requestSecret)) {
            chain.doFilter(request, response);
        } else {
            System.out.println("You do not have the correct secret, will return");
            responseUnauthorizedError((HttpServletResponse) response);
        }
    }



    private void responseUnauthorizedError(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.api+json");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        PrintWriter writer = response.getWriter();
        HashMap<String, Object> respMap = new HashMap<String, Object>();
        HashMap<String, Object> dataMap = new HashMap<String, Object>();
        List<HashMap<String, Object>> errList = new ArrayList<>();
        dataMap.put(ResponseJsonNode.STATUS.getNodeName(), "Unauthorized");
        dataMap.put(ResponseJsonNode.CODE.getNodeName(), "401");
        dataMap.put(ResponseJsonNode.DETAIL.getNodeName(), "The request does not contain a correct secret.");
        ((List<HashMap<String, Object>>) errList).add(dataMap);
        respMap.put(ResponseJsonNode.ERRORS.getNodeName(), errList);
        writer.print(factoryBean.getObject().writeValueAsString(respMap));
        writer.flush();
        writer.close();
    }

    @Override
    public void destroy() {
        System.out.println("Identity filter has been destroyed");
    }
}

