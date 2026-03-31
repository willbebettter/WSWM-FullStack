package wswm.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wswm.bean.*;
import wswm.mapper.appyh;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.*;

@Slf4j
@Service
public class appusr {
    @Autowired
    private appyh app;

    public List<ordwzfh> allorders() {
        try {
            List<ordwzfh> list2 = new ArrayList<>();
            List<String> uu = app.findalluuid();
            for (String uuid : uu) {
                List<orders> li = app.findords(uuid);
                StringJoiner name = new StringJoiner("+");
                String photo;
                Integer foodid2 = li.get(0).getFoodid();
                if (foodid2 < 100000) {
                    photo = app.segood(foodid2).get("good_photo");
                } else {
                    photo = app.semix(foodid2).get("mix_photo");
                }
                Integer sum = li.get(0).getSum();
                String time = li.get(0).getTime();
                String address = li.get(0).getAddress();
                String msg = li.get(0).getMsg();
                Integer status = app.getStatus(uuid);
                for (orders l : li) {
                    Integer foodid = l.getFoodid();
                    if (foodid < 100000) {
                        name.add(app.segood(foodid).get("good_name"));
                    } else {
                        name.add(app.semix(foodid).get("mix_name"));
                    }
                }
                list2.add(0, new ordwzfh(name.toString(), photo, time, sum, status, uuid, msg, address));
            }
            return list2;
        } catch (Exception e) {
            log.warn("获取所有商用订单失败");
            return null;
        }
    }

    public boolean findyh(String code) {
        try {
            Integer x = app.getcount(code);
            if (x == 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log.error("获取用户{}数据异常", code);
            throw new RuntimeException("用户获取失败");
        }
    }

    public boolean addyh(String openid, String name) {
        try {
            app.addyh(openid, name);
            return true;
        } catch (Exception e) {
            log.error("用户{}添加失败", openid);
            return false;
        }
    }

    public String getphoto(String openid) {
        try {
            return app.getphoto(openid);
        } catch (Exception e) {
            log.warn("获取图片失败");
            return null;
        }
    }

    public String getname(String code) {
        try {
            return app.getname(code);
        } catch (Exception e) {
            log.warn("获取用户{}信息失败", code);
            return null;
        }
    }


    public List<apppl> allpl() {
        try {
            return app.allpl();
        } catch (Exception e) {
            log.warn("获取所有用户信息失败");
            throw new RuntimeException("获取所有评论信息失败");
        }
    }

    public String getphotobyname(String name) {
        try {
            return app.getphotobyname(name);
        } catch (Exception e) {
            log.warn("获取用户{}图片失败", name);
            return null;
        }
    }

    public boolean addpl(String name, String photo, String content, LocalDateTime time) {
        try {
            app.addpl(name, photo, content, time);
            return true;
        } catch (Exception e) {
            log.warn("添加用户{}评论失败", name);
            return false;
        }
    }

    public Integer updatename(String name1, String name) {
        try {
            Integer count = app.getcountbyname(name);
            if (count == 0) {
                app.updatename(name1, name);
                return 0;
            } else {
                return 1;
            }
        } catch (Exception e) {
            log.warn("用户{}修改失败", name);
            return 2;
        }
    }

    public boolean updatephoto(String name1, String wzurl) {
        try {
            app.updatephoto(name1, wzurl);
            return true;
        } catch (Exception e) {
            log.warn("用户{}修改头像失败", name1);
            return false;
        }
    }

    public void updateplname(String name1, String name) {
        try {
            app.cgna(name1, name);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateplphoto(String name1, String wzurl) {
        try {
            app.cgphoto(name1, wzurl);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String findopenidbyname(String name1) {
        try {
            return app.findopenidbyname(name1);
        } catch (Exception e) {
            log.warn("获取用户{}信息失败", name1);
            return null;
        }
    }

    public void addorder(String openid, Integer foodid, Integer count, Integer sum, String msg, String address, String uuid, LocalDateTime time) {
        try {
            app.addorder(openid, foodid, count, sum, msg, address, uuid, time);
        } catch (Exception e) {
            log.warn("添加用户{}订单失败", openid);
            log.warn(e.toString());
        }
    }

    public List<wzfh> findord(String openid) {
        try {
            List<wzfh> list2 = new ArrayList<>();
            List<String> uu = app.finduuidbyopenuid(openid);
            for (String uuid : uu) {
                List<order> li = app.findord(uuid);
                StringJoiner name = new StringJoiner("+");
                String photo;
                Integer foodid2 = li.get(0).getFoodid();
                if (foodid2 < 100000) {
                    photo = app.segood(foodid2).get("good_photo");
                } else {
                    photo = app.semix(foodid2).get("mix_photo");
                }
                Integer sum = li.get(0).getSum();
                String time = li.get(0).getTime();
                Integer status = app.getStatus(uuid);
                for (order l : li) {
                    Integer foodid = l.getFoodid();
                    if (foodid < 100000) {
                        name.add(app.segood(foodid).get("good_name"));
                    } else {
                        name.add(app.semix(foodid).get("mix_name"));
                    }
                }
                list2.add(0, new wzfh(name.toString(), photo, time, sum, status, uuid));
            }
            return list2;
        } catch (Exception e) {
            log.warn("获取用户{}订单失败", openid);
            return null;
        }
    }

    public String getopenid(String name1) {
        try {
            return app.findopenidbyname2(name1);
        } catch (Exception e) {
            log.warn("获取用户{}信息失败", name1);
            return null;
        }
    }

    public void giveorder(Integer status, String uuid) {
        try {
            app.giveorder(status, uuid);
        } catch (Exception e) {
            log.warn("修改用户{}订单失败", uuid);
        }
    }

    public void tk(String uuid) {
        try {
            app.tk(uuid);
        } catch (Exception e) {
            log.warn("用户{}订单退款失败", uuid);
        }
    }

    public void pj(String uuid, String pj) {
        try {
            app.pj(uuid, pj);
        } catch (Exception e) {
            log.warn("用户{}订单评价失败", uuid);
        }
    }


    public void status1(String uuid) {
        try {
            app.status1(uuid);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void status5(String uuid) {
        try {
            app.status5(uuid);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void statuschg() {
        try {
            app.sestatus();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void statuschg2() {
        try {
            app.sestatus2();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public tj1 tj() {
        try {
            LocalDateTime todaystart = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
            LocalDateTime todayend = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
            List<String> uuids = app.todayuuids(todaystart, todayend);
            Integer counts = 0;
            Integer todaytotal = 0;
            Integer sumtotal = 0;
            List<String> uuids2 = new ArrayList<>();
            for (String uuid : uuids) {
                if (app.sum(uuid) != null) {
                    uuids2.add(app.sum(uuid));
                    counts++;
                }
            }
            for (String uuid2 : uuids2) {
                todaytotal += app.getsumtoday(uuid2);
            }
            List<Integer> sumlist = app.sumall();
            for (Integer i : sumlist) {
                sumtotal += i;
            }
            return new tj1(counts, todaytotal, sumtotal);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Integer> todayuuids() {
        try {
            Year currentYear = Year.now();
            LocalDateTime start = currentYear.atDay(1).atStartOfDay();
            LocalDateTime end = currentYear.atDay(currentYear.length()).atTime(23, 59, 59);
            List<Map<String, Object>> monthsales = app.monthsales(start, end);
            List<Integer> sumall = new ArrayList<>();
            for (Integer i = 0; i < 12; i++) {
                sumall.add(0);
            }
            for (Map<String, Object> map : monthsales) {
                Object monthObj = map.get("month");
                Object suObj = map.get("su");
                // 3. 转为 String，再转为 Integer (这是最安全的转换方式)
                // String.valueOf() 可以处理 null、String、BigDecimal 等所有对象
                Integer month = Integer.valueOf(String.valueOf(monthObj));
                Integer amount = Integer.valueOf(String.valueOf(suObj));
                sumall.set(month - 1, amount);
            }
            return sumall;
        } catch (Exception e) {
            e.printStackTrace(); // 这行会打印错误类型，看看是不是 NumberFormatException
            throw new RuntimeException(e);
        }
    }

    public List<Integer> todayuuids2() {
        try {
            Year currentYear = Year.now();
            LocalDateTime start = currentYear.atDay(1).atStartOfDay();
            LocalDateTime end = currentYear.atDay(currentYear.length()).atTime(23, 59, 59);
            List<Map<String, Object>> monthsales = app.monthsalescount(start, end);
            List<Integer> sumall2 = new ArrayList<>();
            for (Integer i = 0; i < 12; i++) {
                sumall2.add(0);
            }
            for (Map<String, Object> map : monthsales) {
                Object monthObj = map.get("month2");
                Object suObj = map.get("su2");
                // 3. 转为 String，再转为 Integer (这是最安全的转换方式)
                // String.valueOf() 可以处理 null、String、BigDecimal 等所有对象
                Integer month = Integer.valueOf(String.valueOf(monthObj));
                Integer count = Integer.valueOf(String.valueOf(suObj));
                sumall2.set(month - 1, count);
            }
            return sumall2;
        } catch (Exception e) {
            e.printStackTrace(); // 这行会打印错误类型，看看是不是 NumberFormatException
            throw new RuntimeException(e);
        }
    }

    public void addcount(Integer foodid, Integer count) {
        try {
            app.addcount(foodid, count);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void addmixcount(Integer foodid, Integer count) {
        try {
            app.addmixsale(foodid, count);
            List<Integer> fo = app.findgoods(foodid);
            for (Integer fid : fo) {
                app.addsale(fid, count);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
