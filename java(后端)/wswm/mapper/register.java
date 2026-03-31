package wswm.mapper;

import org.apache.ibatis.annotations.*;
import wswm.bean.yhb;

import java.time.LocalDateTime;


@Mapper
public interface register {
    @Select("select count(*) from yonghu where phonenumber=#{num}")
    public Integer select(String num);

    @Insert("insert into yonghu (usr, pwd, phonenumber, checkcode, created_time) values (#{usr}, #{pwd}, #{phonenumber}, #{checkcode}, #{time})")
    public void insert(String usr, String pwd, String phonenumber, String checkcode, LocalDateTime time);

    @Update("update yonghu set photo= #{wzfilename} where usr=#{usr}")
    public void updatephoto(String wzfilename, String usr);

    @Select("select count(*) from yonghu where usr= #{usr}")
    public Integer selectUsr(String usr);

    @Update("update yonghu set usr= #{usr} where usr= #{oldusr}")
    public void changeusr(String usr, String oldusr);

    @Select("select photo from yonghu where usr= #{usr}")
    public String getphoto(String usr);
}
