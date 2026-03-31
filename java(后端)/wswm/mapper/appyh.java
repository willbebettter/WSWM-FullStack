package wswm.mapper;

import org.apache.ibatis.annotations.*;
import wswm.bean.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Mapper
public interface appyh {
    @Select("select count(*) from appyh where openid=#{openid}")
    public Integer getcount(String openid);

    @Insert("insert into appyh(openid,name) values(#{openid},#{name})")
    public void addyh(String openid, String name);

    @Select("select photo from appyh where openid=#{openid}")
    public String getphoto(String openid);

    @Select("select name from appyh where openid=#{code}")
    public String getname(String code);

    @Select("select * from pinglun")
    public List<apppl> allpl();

    @Select("select photo from appyh where name=#{name}")
    public String getphotobyname(String name);

    @Insert("insert into pinglun(name,photo,content,time) values(#{name},#{photo},#{content},#{time})")
    public void addpl(String name, String photo, String content, LocalDateTime time);

    @Select("select count(*) from appyh where name=#{name}")
    public Integer getcountbyname(String name);

    @Update("update appyh set name=#{name} where name=#{name1}")
    public void updatename(String name1, String name);

    @Update("update appyh set photo=#{wzurl} where name=#{name1}")
    public void updatephoto(String name1, String wzurl);

    @Update("update pinglun set name=#{name} where name=#{name1}")
    public void cgna(String name1, String name);

    @Update("update pinglun set photo= #{wzurl} where name= #{name1}")
    public void cgphoto(String name1, String wzurl);

    @Select("select openid from appyh where name=#{name1}")
    public String findopenidbyname(String name1);

    @Insert("insert into `order` (openid,foodid,count,sum,msg,address,uuid,time) " +
            "values(#{openid},#{foodid},#{count},#{sum},#{msg},#{address},#{uuid},#{time})")
    public void addorder(String openid, Integer foodid, Integer count, Integer sum, String msg, String address, String uuid, LocalDateTime time);

    @Select("select foodid,sum,`time` from `order` where uuid=#{uuid}")
    public List<order> findord(String uuid);

    @Select("select openid from appyh where name=#{name1}")
    public String findopenidbyname2(String name1);

    @Select("select distinct uuid from `order` where openid=#{openid}")
    public List<String> finduuidbyopenuid(String openid);

    @Select("select good_name,good_photo from goods where id=#{foodid}")
    public Map<String, String> segood(Integer foodid);

    @Select("select mix_name,mix_photo from mixgoods where id= #{foodid}")
    public Map<String, String> semix(Integer foodid);

    @Insert("insert into orderstatus (uuid,status) values (#{uuid},#{status})")
    public void giveorder(Integer status, String uuid);

    @Select("select status from orderstatus where uuid=#{u}")
    public Integer getStatus(String u);

    @Update("update orderstatus set status=5 where uuid=#{uuid}")
    public void tk(String uuid);

    @Update("update orderstatus set status=4,pj=#{pj} where uuid= #{uuid}")
    public void pj(String uuid, String pj);

    @Select("select distinct uuid from `order`")
    public List<String> findalluuid();

    @Select("select * from `order` where uuid= #{uuid}")
    public List<orders> findords(String uuid);

    @Update("update orderstatus set status=1 where uuid= #{uuid}")
    public void status1(String uuid);

    @Update("update orderstatus set status=5 where uuid= #{uuid}")
    public void status5(String uuid);

    @Update("update orderstatus set status=2 where status=1")
    public void sestatus();

    @Update("update orderstatus set status=3 where status=2")
    public void sestatus2();

    @Select("select distinct uuid from `order` where time>=#{toddaystart} and time<=#{todayend}")
    public List<String> todayuuids(LocalDateTime toddaystart, LocalDateTime todayend);

    @Select("select sum from `order` ")
    public Integer sumtotal();

    @Select("select uuid from orderstatus where  uuid=#{uuid} and (status=3 or status=4) ")
    public String sum(String uuid);

    @Select("select distinct sum from `order` where uuid= #{uuid2}")
    public Integer getsumtoday(String uuid2);

    @Select("select sum(distinct sum) from `order` where uuid in (select uuid from orderstatus where status in  (3,4))")
    public List<Integer> sumall();

    @Select("select  date_format(time,'%m') as `month`,sum(distinct sum) as `su` from `order` where uuid in (select uuid from orderstatus where uuid in (select uuid from `order` where time>=#{start} and time<=#{end}) and status in (3,4)) group by date_format(time,'%m')")
    public List<Map<String, Object>> monthsales(LocalDateTime start, LocalDateTime end);

    @Select("select  date_format(time,'%m') as `month2`,count(*) as `su2` from `order` where uuid in (select uuid from orderstatus where uuid in (select uuid from `order` where time>=#{start} and time<=#{end}) and status in (3,4)) group by date_format(time,'%m')")
    public List<Map<String, Object>> monthsalescount(LocalDateTime start, LocalDateTime end);

    @Select("select name,photo from appyh")
    public List<yh> allappyh();

    @Update("update goods set good_sale=good_sale+#{count} where id=#{foodid}")
    public void addcount(Integer foodid, Integer count);

    @Select("select goodid from connect1 where mixid= #{foodid}")
    List<Integer> findgoods(Integer foodid);

    @Update("update goods set good_sale=good_sale+#{count} where id= #{fid}")
    public void addsale(Integer fid, Integer count);

    @Update("update mixgoods set mix_sale=mix_sale+#{count} where id= #{foodid}")
    public void addmixsale(Integer foodid, Integer count);
}
