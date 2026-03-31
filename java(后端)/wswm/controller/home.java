package wswm.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import wswm.bean.goodsbean;
import wswm.service.spxr;
import wswm.utils.Fsqd;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@Slf4j
public class home {
    @Autowired
    public spxr spxr;

    @GetMapping("/home/getgoods")
    public Fsqd<List<goodsbean>> getgoods() {
        if (spxr.allgoods() != null) {
            List<goodsbean> list1 = spxr.allgoods();
            return new Fsqd<>(200, "请求成功", list1);
        } else {
            log.info("请求失败");
            return new Fsqd<>(400, "请求失败", null);
        }
    }

    @PostMapping("/home/addgood")
    public Fsqd<String> addgood(@RequestParam("good_name") String good_name,
                                @RequestParam(value = "good_text", required = false) String good_text,
                                @RequestParam("good_price") Integer good_price,
                                @RequestParam("type_name") String type_name,
                                @RequestParam("good_photo") MultipartFile mfile) throws IOException {
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
                        goodsbean gd = new goodsbean();
                        gd.setGood_name(good_name);
                        gd.setGood_text(good_text);
                        gd.setGood_price(good_price);
                        gd.setType_name(type_name);
                        gd.setGood_photo(fullUrl);

                        if (spxr.addgood(gd)) {
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

    @DeleteMapping("/home/delgood")
    public Fsqd<String> delgood(@RequestParam("id") Integer id) {
        try {
            String yuanfile = spxr.origood(id);
            String ze = "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}\\.[^/]+";
            Pattern pattern2 = Pattern.compile(ze);
            Matcher matcher2 = pattern2.matcher(yuanfile);
            if (matcher2.find()) {
                String fileName2 = "E:\\编程学习文件夹\\wswm\\src\\main\\resources\\static\\" + matcher2.group();
                String fileName3 = "E:\\编程学习文件夹\\wswm\\target\\classes\\static\\" + matcher2.group();
                if (new File(fileName2).delete() && new File(fileName3).delete()) {
                    log.info("删除原用户图片成功成功");
                } else {
                    log.warn("删除原用户图片失败");
                }
            }
            if (spxr.delgood(id)) {
                return new Fsqd<>(200, "删除成功", null);
            } else {
                return new Fsqd<>(400, "删除失败", null);
            }
        } catch (Exception e) {
            log.warn("未找到id为{}的原图片", id);
            if (spxr.delgood(id)) {
                return new Fsqd<>(200, "删除成功,但原图片删除错误", null);
            } else {
                return new Fsqd<>(400, "删除失败", null);
            }
        }
    }

    @GetMapping("/home/getgood")
    public Fsqd<goodsbean> getgood(@RequestParam("id") Integer id) {
        goodsbean go;
        try {
            go = spxr.getgood(id);
        } catch (Exception e) {
            return new Fsqd<>(400, "未查询到", null);
        }
        if (go != null) {
            return new Fsqd<>(200, "返回查询结果成功", go);
        } else {
            return new Fsqd<>(400, "未询结果为null", null);
        }
    }

    @PostMapping("/home/changegood")
    public Fsqd<String> changegood(@RequestParam("id") Integer id,
                                   @RequestParam("good_name") String good_name,
                                   @RequestParam(value = "good_text", required = false) String good_text,
                                   @RequestParam("good_price") Integer good_price,
                                   @RequestParam("type_name") String type_name,
                                   @RequestParam(value = "good_photo", required = false) MultipartFile mfile) throws IOException {
        try {
            if (mfile == null) {
                goodsbean gd = new goodsbean();
                gd.setId(id);
                gd.setGood_name(good_name);
                gd.setGood_text(good_text);
                gd.setGood_price(good_price);
                gd.setType_name(type_name);
                if (spxr.updagood(gd)) {
                    return new Fsqd<>(200, "修改成功,但原图片没有改动", null);
                } else {
                    return new Fsqd<>(400, "修改失败", null);
                }
            }
            UUID jmname = UUID.randomUUID();
            String ytp = mfile.getOriginalFilename();
            String ze = "\\.[^.]+$";
            Pattern pattern2 = Pattern.compile(ze);
            Matcher matcher2 = pattern2.matcher(ytp);
            String phurl = spxr.origood(id);
            String ze1 = "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}\\.[^/]+";
            Pattern pattern1 = Pattern.compile(ze1);
            Matcher matcher1 = pattern1.matcher(phurl);
            if (matcher2.find() && matcher1.find()) {
                String fn1 = "E:\\编程学习文件夹\\wswm\\src\\main\\resources\\static\\" + matcher1.group();
                String fn2 = "E:\\编程学习文件夹\\wswm\\target\\classes\\static\\" + matcher1.group();
                String fileName2 = "E:\\编程学习文件夹\\wswm\\src\\main\\resources\\static\\" + jmname + matcher2.group();
                String fileName3 = "E:\\编程学习文件夹\\wswm\\target\\classes\\static\\" + jmname + matcher2.group();
                if (new File(fn1).delete() && new File(fn2).delete()) {
                    log.info("删除原用户图片成功成功");
                    goodsbean gd = new goodsbean();
                    gd.setId(id);
                    gd.setGood_name(good_name);
                    gd.setGood_text(good_text);
                    gd.setGood_price(good_price);
                    gd.setType_name(type_name);
                    gd.setGood_photo("http://localhost:8080/" + jmname + matcher2.group());
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
                        if (spxr.updgood(gd)) {
                            return new Fsqd<>(200, "修改成功", null);
                        } else {
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

    @GetMapping("/home/fenleigood")
    public Fsqd<List<goodsbean>> fenleigood(@RequestParam("name") String type_name) {
        try {
            List<goodsbean> gd = spxr.gettype(type_name);
            return new Fsqd<>(200, "查询成功", gd);
        } catch (Exception e) {
            return new Fsqd<>(400, "查询分类菜品失败", null);
        }
    }

    @GetMapping("/home/getgoodsname")
    public Fsqd<List<String>> getgoodsname() {
        try {
            List<String> gd = spxr.getgoodsname();
            return new Fsqd<>(200, "查询成功", gd);
        } catch (Exception e) {
            return new Fsqd<>(400, "查询分类菜品失败", null);
        }
    }

    @GetMapping("/home/glgood")
    public Fsqd<List<String>> glgood(Integer id) {
        try {
            if (spxr.glcz(id)) {
                return new Fsqd<>(200, "查询成功", null);
            } else {
                return new Fsqd<>(400, "失败,有关联菜单的菜品或出现异常", null);
            }
        } catch (Exception e) {
            return new Fsqd<>(400, "查询失败", null);
        }
    }

    @PostMapping("/home/segood")
    public Fsqd<String> segood(@RequestParam("id") Integer id, @RequestParam("good_name") String good_name) {
        try {
            if (spxr.select1(id, good_name)) {
                return new Fsqd<>(200, "名称可使用", null);
            } else {
                return new Fsqd<>(400, "名称不可使用", null);
            }
        } catch (Exception e) {
            log.warn("名称查询错误");
            return new Fsqd<>(400, "错误", null);
        }
    }
    @PostMapping("/home/se2good")
    public Fsqd<String> se2good(@RequestParam("good_name") String good_name) {
        try {
            if (spxr.select2(good_name)) {
                return new Fsqd<>(200, "名称可使用", null);
            } else {
                return new Fsqd<>(400, "名称不可使用", null);
            }
        } catch (Exception e) {
            log.warn("名称查询错误");
            return new Fsqd<>(400, "名称查询错误", null);
        }
    }
}