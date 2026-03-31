package wswm.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class goodsbean {
    public Integer  id;
    public String good_name;
    public String good_text;
    public Integer good_price;
    public String good_photo;
    public String type_name;
    public Integer good_sale;
}
