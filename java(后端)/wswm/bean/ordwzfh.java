package wswm.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ordwzfh {
    private String name;
    private String photo;
    private String time;
    private Integer sum;
    private Integer status;
    private String uuid;
    private String msg;
    private String address;

}
