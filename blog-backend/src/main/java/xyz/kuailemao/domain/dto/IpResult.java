package xyz.kuailemao.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
public class IpResult<T> implements Serializable {

    private Integer code;
    private String msg;
    private T data;

    public boolean isSuccess() {
        return Objects.nonNull(this.code) && this.code == 0;
    }
}