package wswm.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import wswm.bean.yh;
import wswm.bean.yhb;
import wswm.service.pl;
import wswm.service.dljy;
import wswm.service.zcjy;
import wswm.utils.Fsqd;
import wswm.utils.jwt;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Slf4j
@RestController
public class index {
    @Autowired
    public zcjy zcjy;
    @Autowired
    public dljy dljy;
    @Autowired
    public pl pl;

    @RequestMapping("/")
    public String home() {
        return "这里是Springboot";
    }

    @PostMapping("/register")
    public Fsqd<String> register(@RequestBody yhb yh) {
        if (zcjy.zhuce(yh)) {
            return new Fsqd<>(200, "注册成功", "");
        }
        return new Fsqd<>(400, "注册失败,用户已存在", "");
    }

    @PostMapping("/login")
    public Fsqd<String> login(@RequestBody yhb user) {
        String usr = user.getUsr();
        String pwd = user.getPwd();
        if (dljy.denglu(usr, pwd)) {
            String ph = dljy.getphoto(usr);
            String token = jwt.givejwt(usr, ph, 300);
            return new Fsqd<>(200, "登录成功,已发放令牌", token);
        }
        log.info("请求登录失败");
        return new Fsqd<>(401, "登录失败,用户不存在", "");
    }

    @PostMapping("/home/changephoto")
    public Fsqd<String> changephoto(@RequestParam("usr") String usr,
                                    @RequestParam(value = "photo", required = false) MultipartFile photo) throws IOException {
        if (photo == null) {
            return new Fsqd<>(400, "修改失败", null);
        } else {
            UUID jmname = UUID.randomUUID();
            String file = photo.getOriginalFilename();
            String ze = "\\.[^.]+$";
            Pattern pattern = Pattern.compile(ze);
            Matcher matcher = pattern.matcher(file);
            try {
                if (matcher.find()) {
                    String hz = matcher.group();
                    String newname = jmname + hz;
                    String wzfilename = "http://localhost:8080//photo1/" + newname;
                    String file2 = "E:\\编程学习文件夹\\wswm\\src\\main\\resources\\static\\photo1\\" + newname;
                    String file3 = "E:\\编程学习文件夹\\wswm\\target\\classes\\static\\photo1\\" + newname;
                    String wafile = zcjy.getphoto(usr);
                    File f2 = new File(file2);
                    File f3 = new File(file3);
                    String ze2 = "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}\\.[^/]+";
                    Pattern pattern2 = Pattern.compile(ze2);
                    Matcher matcher2 = pattern2.matcher(wafile);
                    if (matcher2.find()) {
                        String delfile1 = "E:\\编程学习文件夹\\wswm\\src\\main\\resources\\static\\photo1\\" + matcher2.group();
                        String delfile2 = "E:\\编程学习文件夹\\wswm\\target\\classes\\static\\photo1\\" + matcher2.group();
                        new File(delfile1).delete();
                        new File(delfile2).delete();
                        if (zcjy.upphoto(wzfilename, usr)) {
                            java.io.ByteArrayOutputStream byteArrayOutputStream = new java.io.ByteArrayOutputStream();
                            java.io.InputStream inputStream = photo.getInputStream();
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
                            java.io.FileOutputStream devOutputStream = new java.io.FileOutputStream(f2);
                            devOutputStream.write(fileBytes);
                            devOutputStream.flush();
                            devOutputStream.close();
                            log.info("文件保存到开发目录: {}", f2.getAbsolutePath());
                            // 保存到运行目录
                            java.io.FileOutputStream runtimeOutputStream = new java.io.FileOutputStream(f3);
                            runtimeOutputStream.write(fileBytes);
                            runtimeOutputStream.flush();
                            runtimeOutputStream.close();
                            log.info("文件保存到运行目录: {}", f3.getAbsolutePath());
                            String ph = dljy.getphoto(usr);
                            String token = jwt.givejwt(usr, ph, 300);
                            return new Fsqd<>(200, "修改成功", token);
                        } else {
                            return new Fsqd<>(400, "修改失败,原图片未成功删除", null);
                        }
                    } else {
                        if (zcjy.upphoto(wzfilename, usr)) {
                            java.io.ByteArrayOutputStream byteArrayOutputStream = new java.io.ByteArrayOutputStream();
                            java.io.InputStream inputStream = photo.getInputStream();
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
                            java.io.FileOutputStream devOutputStream = new java.io.FileOutputStream(f2);
                            devOutputStream.write(fileBytes);
                            devOutputStream.flush();
                            devOutputStream.close();
                            log.info("文件保存到开发目录: {}", f2.getAbsolutePath());
                            // 保存到运行目录
                            java.io.FileOutputStream runtimeOutputStream = new java.io.FileOutputStream(f3);
                            runtimeOutputStream.write(fileBytes);
                            runtimeOutputStream.flush();
                            runtimeOutputStream.close();
                            log.info("文件保存到运行目录: {}", f3.getAbsolutePath());
                            String ph = dljy.getphoto(usr);
                            String token = jwt.givejwt(usr, ph, 300);
                            return new Fsqd<>(200, "修改成功,但未找到原图片", token);
                        } else {
                            return new Fsqd<>(400, "修改失败", null);
                        }
                    }
                } else {
                    log.warn("读取后缀名失败");
                    return new Fsqd<>(400, "修改失败", null);
                }
            } catch (Exception e) {
                log.warn("修改错误");
                return new Fsqd<>(400, "修改失败", null);
            }
        }
    }

    @GetMapping("/home/getphoto")
    public Fsqd<String> getphoto(@RequestParam("usr") String usr) {
        String photo = dljy.getphoto(usr);
        if (photo != null) {
            return new Fsqd<>(200, "获取成功", photo);
        } else {
            return new Fsqd<>(400, "获取失败", null);
        }
    }

    @PostMapping("/home/changeusr")
    public Fsqd<String> changeusr(@RequestParam("usr") String usr, @RequestParam("oldusr") String oldusr) {
        if (zcjy.repeatnofind(usr)) {
            if (zcjy.changeusr(usr, oldusr)) {
                String ph = dljy.getphoto(usr);
                String token = jwt.givejwt(usr, ph, 300);
                return new Fsqd<>(200, "修改成功", token);
            } else {
                return new Fsqd<>(400, "修改失败", "");
            }
        } else {
            return new Fsqd<>(400, "修改失败,有重复用户名", "");
        }
    }

    @GetMapping("/allappyh")
    public Fsqd<List<yh>> allyh() {
        List<yh> yh = pl.allappyh();
        return new Fsqd<>(200, "获取成功", yh);
    }
}