package wswm.service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;
import wswm.bean.Webean;
import org.springframework.web.client.RestTemplate;
import wswm.wexin.WexinConfig;

@Slf4j
@Service
public class wxjh {
    @Autowired
    private  WexinConfig wei;
    private String code;
    private static final RestTemplate restTemplate = new RestTemplate();
    public Webean getwxyh(String code) {
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid={appid}&secret={appSecret}&js_code={code}&grant_type=authorization_code";
        String  response= restTemplate.getForObject(url, String.class, wei.getAppid(),wei.getSecret() , code);
        ObjectMapper map=new ObjectMapper();
        Webean webean = map.readValue(response, Webean.class);
        if (webean != null && (webean.getErrcode() == null || webean.getErrcode() == 0)) {
            return webean;
        }
        log.info(String.valueOf(webean));
        throw new RuntimeException("获取微信用户信息失败");
    }
}
