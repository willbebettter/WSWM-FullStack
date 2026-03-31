package wswm.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wswm.bean.ordwzfh;
import wswm.bean.tj1;
import wswm.service.appusr;
import wswm.utils.Fsqd;
import wswm.utils.WebSocketServer;
import wswm.utils.jwt;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
public class order {
    @Autowired
    private appusr appusr;

    @PostMapping("/order/salestatus")
    public Fsqd<Integer> salestatus(@RequestParam("status") Integer status) {
        try {
            if (status == 0) {
                WebSocketServer.message("0");
                return new Fsqd<>(200, "成功暂停营业", null);
            } else if (status == 1) {
                WebSocketServer.message("1");
                return new Fsqd<>(200, "成功开启营业", null);
            } else {
                return new Fsqd<>(400, "参数错误", null);
            }
        } catch (Exception e) {
            log.warn("未与小程序建立连接");
            return new Fsqd<>(200, "未与小程序建立连接", null);
        }
    }

    @GetMapping("/order/allorders")
    public Fsqd<List<ordwzfh>> allorders() {
        try {
            List<ordwzfh> list = appusr.allorders();
            return new Fsqd<>(200, "成功", list);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/order/out")
    public Fsqd<String> out(@RequestParam("uuid") String uuid) {
        try {
            appusr.status1(uuid);
            WebSocketServer.message("商家已出餐:" + uuid);
            return new Fsqd<>(200, "成功出餐", null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/order/cancel")
    public Fsqd<String> cancel(@RequestParam("uuid") String uuid) {
        try {
            appusr.status5(uuid);
            WebSocketServer.message("商家取消了改订单:" + uuid);
            return new Fsqd<>(200, "成功取消订单", null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/order/yye")
    public Fsqd<tj1> yye() {
        try {
            tj1 x = appusr.tj();
            return new Fsqd<>(200, "成功", x);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/order/ysale")
    public Fsqd<List<Integer>> ysale() {
        try {
            List<Integer> list = appusr.todayuuids();
            return new Fsqd<>(200, "成功获取月营业额", list);
        } catch (Exception e) {
            return new Fsqd<>(400, "参数错误", null);
        }
    }

    @GetMapping("/order/ysale2")
    public Fsqd<List<Integer>> ysale2() {
        try {
            List<Integer> list = appusr.todayuuids2();
            return new Fsqd<>(200, "成功获取月销量", list);
        } catch (Exception e) {
            return new Fsqd<>(400, "参数错误", null);
        }
    }

    @PostMapping("/app/fs")
    public Fsqd<String> fs(@RequestParam("token") String token, @RequestParam("msg") String msg) {
        try {
            Map<String, Object> dx = jwt.getyh(token);
            String name = (String) dx.get("name");
            WebSocketServer.message3("用户" + name + "给您发送消息了: " + msg);
            return new Fsqd<>(200, "成功发送", null);
        } catch (Exception e) {
            return new Fsqd<>(400, "jwt错误", null);
        }
    }
}
