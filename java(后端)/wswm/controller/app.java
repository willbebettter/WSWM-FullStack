package wswm.controller;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import wswm.bean.*;
import wswm.service.appusr;
import wswm.service.spxr;
import wswm.service.tcxr;
import wswm.service.wxjh;
import wswm.utils.Fsqd;
import wswm.utils.WebSocketServer;
import wswm.utils.jwt;

import java.io.File;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RestController
public class app {
    @Autowired
    private wxjh wxjh;
    @Autowired
    private appusr appusr;
    @Autowired
    private spxr spxr;
    @Autowired
    private tcxr tcxr;

    @RequestMapping("/app")
    public Fsqd<String> app() {
        return new Fsqd<>(200, "app", "成功接收到请求");
    }

    @PostMapping("/app/dl")
    public Fsqd<String> dl(String name) {
        return new Fsqd<>(200, "app", "成功接收到请求,用户名是:" + name);
    }

    @GetMapping("/app/tokenjy")
    public Fsqd<String> tokenjy(@RequestParam("token") String token) {
        if (jwt.jyapptoken(token)) {
            return new Fsqd<>(200, "token验证成功", null);
        } else {
            return new Fsqd<>(400, "token验证失败,毛都没有", null);
        }
    }

    @PostMapping("/app/givetoken")
    public Fsqd<String> givecode(@RequestParam("code") String code) {
        try {
            Webean yhxx = wxjh.getwxyh(code);
            if (appusr.findyh(yhxx.getOpenid())) {
                UUID uuid = UUID.randomUUID();
                appusr.addyh(yhxx.getOpenid(), uuid.toString());
                String photo = appusr.getphoto(yhxx.getOpenid());
                String token = jwt.giveappjwt(uuid.toString(), photo, 14400);
                return new Fsqd<>(200, "用户注册成功,token已返回", token);
            } else {
                String photo = appusr.getphoto(yhxx.getOpenid());
                String token = jwt.giveappjwt(appusr.getname(yhxx.getOpenid()), photo, 14400);
                return new Fsqd<>(201, "用户登陆成功,token已返回", token);
            }
        } catch (Exception e) {
            return new Fsqd<>(400, "处理数据异常", null);
        }
    }

    @GetMapping("/app/getgoods")
    public Fsqd<List<goodsbean>> getgoods() {
        try {
            List<goodsbean> list1 = spxr.allgoods();
            return new Fsqd<>(200, "请求成功,返回菜品列表", list1);
        } catch (Exception e) {
            throw new RuntimeException("处理数据异常");
        }
    }

    @GetMapping("/app/getmix")
    public Fsqd<List<mixgoodsbean>> getmix() {
        try {
            List<mixgoodsbean> list1 = tcxr.allgoods();
            return new Fsqd<>(200, "请求成功,返回套餐列表", list1);
        } catch (Exception e) {
            throw new RuntimeException("处理数据异常");
        }
    }

    @GetMapping("/app/getyh")
    public Fsqd<List<appyh>> getyh(@RequestParam("token") String token) {
        if (jwt.pipeijwt(token)) {
            Map<String, Object> map = jwt.getyh(token);
            String name = (String) map.get("name");
            String photo = (String) map.get("photo");
            List<appyh> list1 = new ArrayList<>();
            list1.add(new appyh(name, photo));
            return new Fsqd<>(200, "请求成功,返回用户名", list1);
        } else {
            return new Fsqd<>(400, "token验证失败,毛都没有", null);
        }
    }

    @GetMapping("/app/pl")
    public Fsqd<List<apppl>> pl() {
        try {
            List<apppl> list1 = appusr.allpl();
            return new Fsqd<>(200, "请求成功,返回评论相关数据", list1);
        } catch (Exception e) {
            return new Fsqd<>(400, "处理评论数据异常", null);
        }
    }

    @PostMapping("/app/fbpl")
    public Fsqd<String> fbpl(@RequestParam("token") String token, @RequestParam("content") String content) {
        try {
            Map<String, Object> x = jwt.getyh(token);
            String name = (String) x.get("name");
            String photo = appusr.getphotobyname(name);
            LocalDateTime time = LocalDateTime.now();
            appusr.addpl(name, photo, content, time);
            return new Fsqd<>(200, "评论发布成功", null);
        } catch (Exception e) {
            return new Fsqd<>(400, "token验证失败,毛都没有", null);
        }
    }

    @PostMapping("/app/updatename")
    public Fsqd<String> updatename(@RequestParam("token") String token, @RequestParam("name") String name) {
        try {
            Map<String, Object> yh = jwt.getyh(token);
            String name1 = (String) yh.get("name");
            String photo = appusr.getphotobyname(name1);
            String token1 = jwt.giveappjwt(name, photo, 14400);
            if (name1.equals(name)) {
                return new Fsqd<>(400, "用户名不能和原用户名相同", null);
            } else {
                Integer num = appusr.updatename(name1, name);
                if (num == 0) {
                    appusr.updateplname(name1, name);
                    return new Fsqd<>(200, "用户名修改成功", token1);
                } else if (num == 1) {
                    return new Fsqd<>(400, "用户名已存在,请重新输入", null);
                } else {
                    throw new RuntimeException("用户名修改失败");
                }
            }
        } catch (Exception e) {
            return new Fsqd<>(400, "token验证失败,毛都没有", null);
        }
    }

    @PostMapping("/app/updatephoto")
    public Fsqd<String> updatephoto(@RequestParam("token") String token, @RequestParam("file") MultipartFile file) {
        try {
            Map<String, Object> yh = jwt.getyh(token);
            String name1 = (String) yh.get("name");
            if (file.isEmpty()) {
                return new Fsqd<>(400, "文件为空", null);
            } else {
                String defaultfileName = "http://localhost:8080/yonghu.png";
                String fileName = file.getOriginalFilename();
                UUID jmname = UUID.randomUUID();
                String ze = "\\.[^.]+$";
                String ze2 = "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}\\.[^/]+";
                String xtfile1 = "E:\\编程学习文件夹\\wswm\\src\\main\\resources\\static\\";
                String xtfile2 = "E:\\编程学习文件夹\\wswm\\target\\classes\\static\\";
                String baseurl = "http://localhost:8080/";
                String ytp = appusr.getphotobyname(name1);
                if (ytp.equals(defaultfileName)) {
                    Pattern p1 = Pattern.compile(ze);
                    if (fileName != null) {
                        Matcher m1 = p1.matcher(fileName);
                        if (m1.find()) {
                            String wzurl = baseurl + jmname + m1.group();
                            String file1 = xtfile1 + jmname + m1.group();
                            String file2 = xtfile2 + jmname + m1.group();
                            File f1 = new File(file1);
                            File f2 = new File(file2);
                            try {
                                java.io.ByteArrayOutputStream byteArrayOutputStream = new java.io.ByteArrayOutputStream();
                                java.io.InputStream inputStream = file.getInputStream();
                                byte[] buffer = new byte[1024];
                                int len;
                                while ((len = inputStream.read(buffer)) > 0) {
                                    byteArrayOutputStream.write(buffer, 0, len);
                                }
                                byteArrayOutputStream.flush();
                                byte[] fileBytes = byteArrayOutputStream.toByteArray();
                                // 关闭输入流
                                inputStream.close();
                                byteArrayOutputStream.close();
                                // 保存到开发目录
                                java.io.FileOutputStream devOutputStream = new java.io.FileOutputStream(f1);
                                devOutputStream.write(fileBytes);
                                devOutputStream.flush();
                                devOutputStream.close();
                                log.info("文件保存到开发目录: {}", f1.getAbsolutePath());
                                // 保存到运行目录
                                java.io.FileOutputStream runtimeOutputStream = new java.io.FileOutputStream(f2);
                                runtimeOutputStream.write(fileBytes);
                                runtimeOutputStream.flush();
                                runtimeOutputStream.close();
                                log.info("文件保存到运行目录: {}", f2.getAbsolutePath());
                                if (appusr.updatephoto(name1, wzurl)) {
                                    String token1 = jwt.giveappjwt(name1, wzurl, 14400);
                                    appusr.updateplphoto(name1, wzurl);
                                    return new Fsqd<>(200, "用户头像修改成功", token1);
                                } else {
                                    throw new RuntimeException("用户头像修改失败");
                                }
                            } catch (Exception e) {
                                throw new RuntimeException("文件保存失败");
                            }
                        }
                    } else {
                        throw new RuntimeException("文件名为空");
                    }
                } else {
                    Pattern p1 = Pattern.compile(ze);
                    Pattern p2 = Pattern.compile(ze2);
                    if (fileName != null) {
                        Matcher m1 = p1.matcher(fileName);
                        Matcher m2 = p2.matcher(ytp);
                        if (m1.find() && m2.find()) {
                            String wzurl = baseurl + jmname + m1.group();
                            String file1 = xtfile1 + jmname + m1.group();
                            String file2 = xtfile2 + jmname + m1.group();
                            File f1 = new File(file1);
                            File f2 = new File(file2);
                            String file3 = xtfile1 + m2.group();
                            String file4 = xtfile2 + m2.group();
                            File f3 = new File(file3);
                            File f4 = new File(file4);
                            if (f3.delete() && f4.delete()) {
                                try {
                                    java.io.ByteArrayOutputStream byteArrayOutputStream = new java.io.ByteArrayOutputStream();
                                    java.io.InputStream inputStream = file.getInputStream();
                                    byte[] buffer = new byte[1024];
                                    int len;
                                    while ((len = inputStream.read(buffer)) > 0) {
                                        byteArrayOutputStream.write(buffer, 0, len);
                                    }
                                    byteArrayOutputStream.flush();
                                    byte[] fileBytes = byteArrayOutputStream.toByteArray();
                                    // 关闭输入流
                                    inputStream.close();
                                    byteArrayOutputStream.close();
                                    // 保存到开发目录
                                    java.io.FileOutputStream devOutputStream = new java.io.FileOutputStream(f1);
                                    devOutputStream.write(fileBytes);
                                    devOutputStream.flush();
                                    devOutputStream.close();
                                    log.info("文件保存到开发目录: {}", f1.getAbsolutePath());
                                    // 保存到运行目录
                                    java.io.FileOutputStream runtimeOutputStream = new java.io.FileOutputStream(f2);
                                    runtimeOutputStream.write(fileBytes);
                                    runtimeOutputStream.flush();
                                    runtimeOutputStream.close();
                                    log.info("文件保存到运行目录: {}", f2.getAbsolutePath());
                                    if (appusr.updatephoto(name1, wzurl)) {
                                        String token1 = jwt.giveappjwt(name1, wzurl, 14400);
                                        appusr.updateplphoto(name1, wzurl);
                                        return new Fsqd<>(200, "用户头像修改成功", token1);
                                    } else {
                                        throw new RuntimeException("用户头像修改失败");
                                    }
                                } catch (Exception e) {
                                    throw new RuntimeException("文件保存失败");
                                }
                            } else {
                                if (appusr.updatephoto(name1, wzurl)) {
                                    String token1 = jwt.giveappjwt(name1, wzurl, 14400);
                                    appusr.updateplphoto(name1, wzurl);
                                    return new Fsqd<>(200, "用户头像修改成功,但未能找到并删除原图片", token1);
                                } else {
                                    throw new RuntimeException("用户头像修改失败");
                                }
                            }
                        }
                    } else {
                        throw new RuntimeException("文件名为空");
                    }
                }
            }
        } catch (Exception e) {
            return new Fsqd<>(400, "token验证失败,毛都没有", null);
        }
        return new Fsqd<>(400, "修改失败", null);
    }

    @PostMapping("/app/zd")
    public Fsqd<String> zd(@RequestBody dto1 dto) {
        try {
            UUID uuid = UUID.randomUUID();
            Map<String, Object> tok = jwt.getyh(dto.getToken());
            String name1 = tok.get("name").toString();
            String openid = appusr.findopenidbyname(name1);
            String bh = uuid.toString();
            for (gwlist li : dto.getList()) {
                Integer foodid = li.getId();
                Integer count = li.getCount();
                LocalDateTime time = LocalDateTime.now();
                if (foodid < 100000) {
                    appusr.addcount(foodid, count);
                } else {
                    appusr.addmixcount(foodid, count);
                }
                appusr.addorder(openid, foodid, count, dto.getSum(), dto.getMsg(), dto.getAddress(), bh, time);
            }
            Map<String, String> map = new HashMap<>();
            map.put("newdd", bh);
            String json = JSON.toJSONString(map);
            WebSocketServer.message2(json);
            return new Fsqd<>(200, "成功,给你订单编号", bh);
        } catch (Exception e) {
            log.warn(e.toString());
            return new Fsqd<>(400, "错误", null);
        }
    }

    @GetMapping("/app/allorders")
    public Fsqd<List<wzfh>> allorders(@RequestParam("token") String token) {
        try {
            String name1 = jwt.getyh(token).get("name").toString();
            String openid = appusr.getopenid(name1);
            List<wzfh> list = appusr.findord(openid);
            return new Fsqd<>(200, "成功", list);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/app/giveorder")
    public Fsqd<String> giveorder(@RequestParam("status") Integer status, @RequestParam("uuid") String uuid) {
        try {
            appusr.giveorder(status, uuid);
            return new Fsqd<>(200, "成功", null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/app/tk")
    public Fsqd<String> tk(@RequestParam("uuid") String uuid) {
        try {
            appusr.tk(uuid);
            Map<String, String> map = new HashMap<>();
            map.put("tk", uuid);
            String json = JSON.toJSONString(map);
            WebSocketServer.message2(json);
            return new Fsqd<>(200, "成功", null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/app/pj")
    public Fsqd<String> pj(@RequestParam("uuid") String uuid, @RequestParam("pj") String pj) {
        try {
            appusr.pj(uuid, pj);
            Map<String, String> map = new HashMap<>();
            map.put("pj", pj);
            String json = JSON.toJSONString(map);
            WebSocketServer.message2(json);
            return new Fsqd<>(200, "成功收到评价", null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/app/cd")
    public Fsqd<String> cd(@RequestParam("uuid") String uuid, @RequestParam("token") String token) {
        try {
            Map<String, String> map = new HashMap<>();
            map.put("cd", uuid);
            String json = JSON.toJSONString(map);
            WebSocketServer.message2(json);
            return new Fsqd<>(200, "成功向商家催单", null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/app/web")
    public Fsqd<String> web() {
        try {
            WebSocketServer.message2("web");
            return new Fsqd<>(200, "成功", null);
        } catch (Exception e) {
            return new Fsqd<>(200, "尚未与前端建立连接", null);
        }
    }

    @GetMapping("/app/yhm")
    public Fsqd<String> yhm(@RequestParam("token") String token) {
        try {
            String name1 = jwt.getyh(token).get("name").toString();
            return new Fsqd<>(200, "成功", name1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
