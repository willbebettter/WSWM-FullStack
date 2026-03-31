package wswm.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wswm.bean.yhb;
import wswm.mapper.login;
@Slf4j
@Service
public class dljy {
    @Autowired
    public login login;
    public boolean denglu(String usr, String pwd) {
        try{
            Integer result = login.select(usr,pwd);
            if(result==1){
                log.info("登录成功");
                return true;
            }
            return false;
        }catch (Exception e){
            log.warn(e.getMessage());
            return false;
        }
    }

    public String getphoto(String usr) {
        try{
            return login.photo(usr);
        }catch (Exception e){
            return null;
        }
    }
}