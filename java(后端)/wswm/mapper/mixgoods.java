package wswm.mapper;

import org.apache.ibatis.annotations.*;
import wswm.bean.mixgoodsbean;

import java.util.List;

@Mapper
public interface mixgoods {
    @Select("select * from mixgoods")
    public List<mixgoodsbean> getmixgoods();

    @Insert("insert into mixgoods(mix_name,mix_text,mix_price,mix_photo,type_name) values(#{mix_name},#{mix_text},#{mix_price},#{mix_photo},#{type_name})")
    public void addmixgoods(mixgoodsbean gd);

    @Select("select id from mixgoods where mix_name=#{mix_name}")
    public Integer getmixid(String mix_name);

    public void addco1(Integer mixid, @Param("dishNames") List<String> dishNames);

    @Select("select mix_photo from mixgoods where id=#{id}")
    public String find(Integer id);

    @Delete("delete from mixgoods where id=#{id}")
    public void del(Integer id);

    @Select("select * from mixgoods where id=#{id}")
    public mixgoodsbean getgood(Integer id);

    @Update("update mixgoods set mix_name=#{mix_name},mix_text=#{mix_text},mix_price=#{mix_price},type_name=#{type_name} where id=#{id}")
    public void update(mixgoodsbean gd);

    @Update("update mixgoods set  mix_name=#{mix_name},mix_text=#{mix_text},mix_price=#{mix_price},type_name=#{type_name},mix_photo=#{mix_photo} where id=#{id}")
    public void update1(mixgoodsbean gd);

    @Select("select * from mixgoods where type_name=#{type_name}")
    List<mixgoodsbean> type(String type_name);

    @Delete("delete from connect1 where mixid=#{id}")
    public void delco(Integer id);

    @Select("select goodid from connect1 where mixid=#{id}")
    public List<Integer> getco2(Integer id);

    @Select("select good_name from goods where id= #{id}")
    public String getco3(Integer id);

    @Select("select count(*) from mixgoods where id!=#{id} and mix_name=#{mix_name}")
    public Integer dsemix1(Integer id, String mix_name);

    @Select("select count(*) from mixgoods where mix_name= #{mixName}")
    public Integer dsemix2(String mixName);
}
