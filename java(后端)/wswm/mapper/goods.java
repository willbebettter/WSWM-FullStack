package wswm.mapper;

import org.apache.ibatis.annotations.*;
import wswm.bean.goodsbean;

import java.util.List;
import java.util.Map;

@Mapper
public interface goods {
    @Select("select * from goods")
    public List<goodsbean> allgoods();

    @Insert("insert into goods(good_name,good_text,good_price,type_name,good_photo) values(#{good_name},#{good_text},#{good_price},#{type_name},#{good_photo})")
    public void addgoods(goodsbean gd);

    @Delete("delete from goods where id=#{id}")
    public void delgoods(Integer id);

    @Update("update goods set good_name=#{good_name},good_text=#{good_text},good_price=#{good_price},type_name=#{type_name},good_photo=#{good_photo} where id=#{id}")
    public void updgoods(goodsbean gd);

    @Select("select good_photo from goods where id=#{id}")
    public String getgoodphoto(Integer id);

    @Select("select * from goods where id=#{id}")
    public goodsbean getgood(Integer id);

    @Update("update goods set good_name=#{good_name},good_text=#{good_text},good_price=#{good_price},type_name=#{type_name} where id=#{id} ")
    public void updgoods2(goodsbean gd);

    @Select("select * from goods where type_name=#{type_name}")
    public List<goodsbean> getgoodsbytype(String type_name);

    @Select("select good_name from goods")
    public List<String> getgoodsname();

    @Select("select mixid from connect1 where goodid=#{id}")
    public List<Integer> glgood(Integer id);

    @Select("select count(*) from goods where id!=#{id} and good_name=#{good_name}")
    public Integer getgoodname(Integer id, String good_name);

    @Select("select count(*) from goods where good_name=#{goodName}")
    public Integer getgoodname2(String goodName);
}
