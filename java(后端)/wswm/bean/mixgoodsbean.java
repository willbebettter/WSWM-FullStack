package wswm.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class mixgoodsbean {
    public Integer id;
    public String mix_name;
    public String mix_text;
    public Integer mix_price;
    public String mix_photo;
    public String type_name;
    public Integer mix_sale;
}
