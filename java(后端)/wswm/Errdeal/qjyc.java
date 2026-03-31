package wswm.Errdeal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wswm.utils.Fsqd;

@Slf4j
@RestControllerAdvice
public class qjyc {
    @ExceptionHandler(Exception.class)
    public Fsqd<String> bc(Exception err) {
        log.error("错误信息：{}"+err.getClass().getName(),err.getMessage());
        return new Fsqd<>(500,"服务器错误", err.getMessage());
    }
}
