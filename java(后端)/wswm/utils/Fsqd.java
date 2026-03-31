package wswm.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Fsqd<T> {
    private int status; // 状态码
    private String message; // 消息
    private T data; // 实际数据
}
