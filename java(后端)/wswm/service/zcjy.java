package wswm.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wswm.bean.yhb;
import wswm.mapper.register;

import java.time.LocalDateTime;

@Slf4j
@Service
public class zcjy {
    @Autowired
    public register regis;

    public boolean zhuce(yhb yh) {
        try {
            String num=yh.getPhonenumber();
            Integer x = regis.select(num);
            if (x != 0) {
                log.warn("用户名已存在");
                return false;
            } else {
                LocalDateTime time = LocalDateTime.now();
                String usr=yh.getUsr();
                String pwd=yh.getPwd();
                String phonenumber=yh.getPhonenumber();
                String checkcode=yh.getCheckcode();
                regis.insert(usr,pwd, phonenumber, checkcode, time);
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public boolean upphoto(String wzfilename, String usr) {
        try {
            regis.updatephoto(wzfilename, usr);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean repeatnofind(String usr) {
        try {
            Integer x = regis.selectUsr(usr);
            if (x == 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public boolean changeusr(String usr, String oldusr) {
        try {
            regis.changeusr(usr, oldusr);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getphoto(String usr) {
        try {
            return regis.getphoto(usr);
        } catch (Exception e) {
            log.warn("获取原图片失败");
            return null;
        }
    }
}
