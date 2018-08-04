package com.ibm.cio.sets.procurement.utils;

import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class ResourceTestUtil {

    protected MockHttpServletRequestBuilder get(String urlMapping) {
        return MockMvcRequestBuilders.get(urlMapping);
    }

}