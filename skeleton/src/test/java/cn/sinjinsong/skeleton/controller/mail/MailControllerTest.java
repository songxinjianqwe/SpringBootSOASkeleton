package cn.sinjinsong.skeleton.controller.mail;

import cn.sinjinsong.skeleton.BaseSpringTest;
import cn.sinjinsong.skeleton.domain.entity.mail.MailDO;
import cn.sinjinsong.skeleton.enumeration.mail.MailStatus;
import cn.sinjinsong.skeleton.service.mail.MailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Created by SinjinSong on 2017/7/13.
 */
@Slf4j
public class MailControllerTest extends BaseSpringTest {
    MockMvc mvc;
    @Autowired
    WebApplicationContext webApplicationConnect;
    
    @Autowired
    private MailService mailService;
    @Autowired
    private ObjectMapper mapper;
    
    @Before
    public void setUp() throws JsonProcessingException {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationConnect).build();
    }

    @Test
    public void findMails() throws Exception {
        String url = "/mails/2";
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.put("target", Arrays.asList("receiver"));
        params.put("mail_status",Arrays.asList("all"));
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(url).params(params).accept(MediaType.APPLICATION_JSON)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(status,200);
        PageInfo<MailDO> mails = mailService.findByReceiver(2L, 1, 5, MailStatus.ALL);
        PageInfo<MailDO> result = mapper.readValue(content, new TypeReference<PageInfo<MailDO>>() {
        });
        log.info("result:{}",result.getList());
        assertEquals(result.getList(),mails.getList());
    }

    
    @Test
    public void sendMails() throws Exception {
    }

    @Test
    public void deleteMail() throws Exception {
    }

}