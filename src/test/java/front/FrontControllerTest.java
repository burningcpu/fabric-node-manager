package front;

import base.BaseControllerTest;
import com.alibaba.fastjson.JSON;
import com.webank.fabric.node.manager.common.pojo.front.ReqFrontVO;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class FrontControllerTest extends BaseControllerTest {

    @Test
    public void newFrontTest() throws Exception {
        ReqFrontVO param = ReqFrontVO.builder().frontIp("127.0.0.1").frontPort(6001).build();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post( "/front/new").
                content(JSON.toJSONString(param)).
                contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.
                andExpect(MockMvcResultMatchers.status().isOk()).
                andDo(MockMvcResultHandlers.print());
        MockHttpServletResponse response = resultActions.andReturn().getResponse();
        System.out.println("=====status:"+response.getStatus());
        System.out.println("=====response:"+response.getContentAsString());
    }

}
