package wswm.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class orders {
    private String openid;
    private Integer foodid;
    private Integer sum;
    private String address;
    private String msg;
    private String uuid;
    private String time;
}
