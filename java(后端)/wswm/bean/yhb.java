package wswm.bean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.sql.Timestamp;
@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class yhb {
    public Integer id;
    public String usr;
    public String pwd;
    public String phonenumber;
    public String checkcode;
    public String photo;
    public Timestamp created_time;
}

