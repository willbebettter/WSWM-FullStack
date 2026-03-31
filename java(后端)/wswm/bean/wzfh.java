package wswm.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class wzfh {
    private String name;
    private String photo;
    private String time;
    private Integer sum;
    private Integer status;
    private String uuid;
}
