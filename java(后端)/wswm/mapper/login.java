package wswm.mapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface login {
    @Select("select count(*) from yonghu where usr=#{usr} and trim(pwd)=trim(#{pwd})")
    public Integer select(String usr , String pwd);
    @Select("select photo from yonghu where usr=#{usr}")
    public String photo(String usr);
}
