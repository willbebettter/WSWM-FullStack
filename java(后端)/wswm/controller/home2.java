package wswm.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import wswm.bean.mixgoodsbean;
import wswm.service.tcxr;
import wswm.utils.Fsqd;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@Slf4j
public class home2 {
    @Autowired
    public tcxr tcxr;

    @GetMapping("/home/getmixgoods")
    public Fsqd<List<mixgoodsbean>> getgoods() {
        if (tcxr.allgoods() != null) {
            List<mixgoodsbean> list1 = tcxr.allgoods();
            return new Fsqd<>(200, "请求成功", list1);
        } else {
            log.info("请求失败");
            return new Fsqd<>(400, "请求失败", null);
        }
    }

    @PostMapping("/home/addmixgood")
    public Fsqd<String> addgood(
            @RequestParam("mix_name") String mix_name,
            @RequestParam(value = "mix_text", required = false) String mix_text,
            @RequestParam("mix_price") Integer mix_price,
            @RequestParam("type_name") String type_name,
            @RequestParam("mix_photo") MultipartFile mfile,
            @RequestParam("list") String list) throws IOException {
        if (!mfile.isEmpty()) {
            UUID jmname = UUID.randomUUID();
            String jm = mfile.getOriginalFilename();
            String zzgz = "\\.[^.]+$";
            Pattern pattern = Pattern.compile(zzgz);
            if (jm != null) {
                Matcher matcher = pattern.matcher(jm);
                // 保存文件
                if (matcher.find()) {
                    String fileName = jmname + matcher.group();
                    String fullUrl = "http://localhost:8080/" + fileName;
                    // 开发目录
                    File devDir = new File("E:\\编程学习文件夹\\wswm\\src\\main\\resources\\static\\");
                    if (!devDir.exists()) {
                        devDir.mkdirs();
                    }
                    File devTarget = new File(devDir, fileName);
                    // 运行目录
                    File runtimeDir = new File("E:\\编程学习文件夹\\wswm\\target\\classes\\static\\");
                    if (!runtimeDir.exists()) {
                        runtimeDir.mkdirs();
                    }
                    File runtimeTarget = new File(runtimeDir, fileName);
                    try {
                        // 先将文件读取到内存缓冲区
                        java.io.ByteArrayOutputStream byteArrayOutputStream = new java.io.ByteArrayOutputStream();
                        java.io.InputStream inputStream = mfile.getInputStream();
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
                        java.io.FileOutputStream devOutputStream = new java.io.FileOutputStream(devTarget);
                        devOutputStream.write(fileBytes);
                        devOutputStream.flush();
                        devOutputStream.close();
                        log.info("文件保存到开发目录: {}", devTarget.getAbsolutePath());
                        // 保存到运行目录
                        java.io.FileOutputStream runtimeOutputStream = new java.io.FileOutputStream(runtimeTarget);
                        runtimeOutputStream.write(fileBytes);
                        runtimeOutputStream.flush();
                        runtimeOutputStream.close();
                        log.info("文件保存到运行目录: {}", runtimeTarget.getAbsolutePath());
                        // 保存到数据库
                        mixgoodsbean gd = new mixgoodsbean();
                        gd.setMix_name(mix_name);
                        gd.setMix_text(mix_text);
                        gd.setMix_price(mix_price);
                        gd.setType_name(type_name);
                        gd.setMix_photo(fullUrl);
                        if (tcxr.add(gd, list)) {
                            return new Fsqd<>(200, "添加成功", null);
                        } else {
                            return new Fsqd<>(400, "添加失败", null);
                        }
                    } catch (IOException e) {
                        log.error("文件保存失败: {}", e.getMessage(), e);
                        return new Fsqd<>(500, "文件保存失败", null);
                    }
                } else {
                    log.error("文件名后缀获取异常");
                    return new Fsqd<>(400, "添加失败", null);
                }
            } else {
                log.warn("接收到文件名为空");
                return new Fsqd<>(400, "添加失败", null);
            }
        } else {
            log.error("文件为空");
            return new Fsqd<>(400, "添加失败", null);
        }
    }

    @DeleteMapping("/home/delmixgood")
    public Fsqd<String> delgood(@RequestParam("id") Integer id) {
        try {
            if (tcxr.fin(id) == null) {
                return new Fsqd<>(400, "请求失败", null);
            }
            String pho = tcxr.fin(id);
            String ze = "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}\\.[^/]+";
            Pattern pattern2 = Pattern.compile(ze);
            Matcher matcher2 = pattern2.matcher(pho);
            if (matcher2.find()) {
                String fileName2 = "E:\\编程学习文件夹\\wswm\\src\\main\\resources\\static\\" + matcher2.group();
                String fileName3 = "E:\\编程学习文件夹\\wswm\\target\\classes\\static\\" + matcher2.group();
                if (new File(fileName2).delete() && new File(fileName3).delete()) {
                    log.info("删除原用户图片成功成功");
                } else {
                    if (tcxr.delco(id)) {
                        if (tcxr.del(id)) {
                            return new Fsqd<>(200, "删除成功", null);
                        } else {
                            return new Fsqd<>(400, "删除失败", null);
                        }
                    }
                    else {
                        log.warn("未成功删除对应id为{}的查询连接表", id);
                        return new Fsqd<>(400, "删除失败", null);
                    }
                }
            }
            if (tcxr.delco(id)) {
                if (tcxr.del(id)) {
                    return new Fsqd<>(200, "删除成功", null);
                } else {
                    return new Fsqd<>(400, "删除失败", null);
                }
            }
            else {
                log.warn("未成功删除对应id为{}的查询连接表", id);
                return new Fsqd<>(400, "删除失败", null);
            }
        } catch (Exception e) {
            log.warn("操作失败");
            if (tcxr.del(id)) {
                return new Fsqd<>(200, "删除记录成功,但原图片删除错误", null);
            } else {
                return new Fsqd<>(400, "删除失败", null);
            }
        }

    }

    @GetMapping("/home/getmixgood")
    public Fsqd<mixgoodsbean> getgood(@RequestParam("id") Integer id) {
        mixgoodsbean go;
        try {
            go = tcxr.getgood(id);
        } catch (Exception e) {
            return new Fsqd<>(400, "未查询到", null);
        }
        if (go != null) {
            return new Fsqd<>(200, "返回查询结果成功", go);
        } else {
            return new Fsqd<>(400, "未询结果为null", null);
        }
    }

    @PostMapping("/home/changemixgood")
    public Fsqd<String> changegood(@RequestParam("id") Integer id,
                                   @RequestParam("mix_name") String good_name,
                                   @RequestParam(value = "mix_text", required = false) String good_text,
                                   @RequestParam("mix_price") Integer good_price,
                                   @RequestParam("type_name") String type_name,
                                   @RequestParam("list") String list,
                                   @RequestParam(value = "mix_photo", required = false) MultipartFile mfile) throws IOException {
        try {
            if (mfile == null) {
                mixgoodsbean gd = new mixgoodsbean();
                gd.setId(id);
                gd.setMix_name(good_name);
                gd.setMix_text(good_text);
                gd.setMix_price(good_price);
                gd.setType_name(type_name);
                if(tcxr.updateco(id,list)){
                    if (tcxr.update(gd)) {
                        return new Fsqd<>(200, "修改成功,但原图片未成功删除", null);
                    } else {
                        return new Fsqd<>(400, "修改失败", null);
                    }
                }else {
                    log.warn("修改失败");
                    return new Fsqd<>(400, "修改失败", null);
                }
            }
            UUID jmname = UUID.randomUUID();
            String ytp = mfile.getOriginalFilename();
            String ze = "\\.[^.]+$";
            Pattern pattern2 = Pattern.compile(ze);
            Matcher matcher2 = pattern2.matcher(ytp);
            String phurl = tcxr.fin(id);
            String ze1 = "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}\\.[^/]+";
            Pattern pattern1 = Pattern.compile(ze1);
            Matcher matcher1 = pattern1.matcher(phurl);
            if (matcher2.find()) {
                String fn1 = "E:\\编程学习文件夹\\wswm\\src\\main\\resources\\static\\" + matcher1.group();
                String fn2 = "E:\\编程学习文件夹\\wswm\\target\\classes\\static\\" + matcher1.group();
                String fileName2 = "E:\\编程学习文件夹\\wswm\\src\\main\\resources\\static\\" + jmname + matcher2.group();
                String fileName3 = "E:\\编程学习文件夹\\wswm\\target\\classes\\static\\" + jmname + matcher2.group();
                if (new File(fn1).delete() && new File(fn2).delete()) {
                    log.info("删除原用户图片成功成功");
                    mixgoodsbean gd = new mixgoodsbean();
                    gd.setId(id);
                    gd.setMix_name(good_name);
                    gd.setMix_text(good_text);
                    gd.setMix_price(good_price);
                    gd.setType_name(type_name);
                    gd.setMix_photo("http://localhost:8080/" + jmname + matcher2.group());
                    File f2 = new File(fileName2);
                    File f3 = new File(fileName3);
                    try { // 先将文件读取到内存缓冲区
                        java.io.ByteArrayOutputStream byteArrayOutputStream = new java.io.ByteArrayOutputStream();
                        java.io.InputStream inputStream = mfile.getInputStream();
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
                        if(tcxr.updateco(id,list)){
                            if (tcxr.update1(gd)) {
                                return new Fsqd<>(200, "修改成功", null);
                            } else {
                                return new Fsqd<>(400, "修改失败", null);
                            }
                        }else {
                            log.warn("修改失败");
                            return new Fsqd<>(400, "修改失败", null);
                        }
                    } catch (Exception e) {
                        log.warn(e.getMessage());
                        return new Fsqd<>(400, "修改失败,未能成功保存新图片", null);
                    }
                } else {
                    log.warn("删除原用户图片失败");
                    return new Fsqd<>(400, "原图片删除失败失败", null);
                }
            } else {
                return new Fsqd<>(400, "未找到匹配原图片", null);
            }
        } catch (Exception e) {
            log.warn(e.getMessage());
            return new Fsqd<>(400, "修改失败", null);
        }
    }

    @GetMapping("/home/fenlei2good")
    public Fsqd<List<mixgoodsbean>> fenleigood(@RequestParam("name") String type_name) {
        try {
            List<mixgoodsbean> gd = tcxr.type(type_name);
            return new Fsqd<>(200, "查询成功", gd);
        } catch (Exception e) {
            return new Fsqd<>(400, "查询分类菜品失败", null);
        }
    }
    @GetMapping("/home/getmixgoodname")
    public Fsqd<List<String>> getmixgoodname(@RequestParam("id") Integer id) {
        try {
            List<Integer> gd = tcxr.getmixgoodids(id);
            List<String> gd2 = new ArrayList<>();
            for (Integer goodid : gd) {
                gd2.add(tcxr.getmixgoodnames(goodid));
            }
            return new Fsqd<>(200, "查询成功", gd2);
        } catch (Exception e) {
            return new Fsqd<>(400, "查询失败", null);
        }
    }
    @PostMapping("/home/semixgood")
    public Fsqd<String> semixgood(@RequestParam("id") Integer id, @RequestParam("mix_name") String mix_name) {
        try {
            if (tcxr.dsemix1(id,mix_name)) {
                return new Fsqd<>(200, "名称可使用", null);
            } else {
                return new Fsqd<>(400, "名称重复", null);
            }
        } catch (Exception e) {
            return new Fsqd<>(400, "名称查询错误", null);
        }
    }
    @PostMapping("/home/se2mixgood")
    public Fsqd<String> se2mixgood(@RequestParam("mix_name") String mix_name) {
        try {
            if (tcxr.dsemix2(mix_name)) {
                return new Fsqd<>(200, "名称可使用", null);
            } else {
                return new Fsqd<>(400, "名称重复", null);
            }
        } catch (Exception e) {
            return new Fsqd<>(400, "查询错误", null);
        }
    }
}