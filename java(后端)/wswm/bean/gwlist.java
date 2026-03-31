package wswm.bean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class gwlist {
    public Integer id;
    public String name1;
    public String type;
    public String text;
    public Integer price;
    public String photo;
    public Integer sale;
    public Integer count;
}