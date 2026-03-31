package wswm.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Data
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class order {
    private Integer foodid;
    private Integer count;
    private String time;
    private Integer sum;
}
