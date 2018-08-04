package com.ibm.cio.sets.procurement.controller;

import com.ibm.cio.sets.procurement.utils.ResourceTestUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value=UserController.class)
public class UserControllerTest extends ResourceTestUtil {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void getUserTest() throws Exception {
        MvcResult result = mockMvc.perform(get("/user/getUser?userId=kpzhang@cn.ibm.com")).andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        System.out.println(response);
        assertTrue(response.contains("Hello Controller"));
    }

}