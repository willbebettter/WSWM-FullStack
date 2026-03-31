package wswm.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wswm.mapper.appyh;
import wswm.bean.yh;

import java.util.List;

@Slf4j
@Service
public class pl {
    @Autowired
    private appyh app;
    public List<yh> allappyh() {
        return app.allappyh();
    }
}
