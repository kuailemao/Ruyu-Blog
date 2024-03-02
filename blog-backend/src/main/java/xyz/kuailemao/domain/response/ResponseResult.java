package xyz.kuailemao.domain.response;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.kuailemao.enums.RespEnum;

/**
 * 统一响应类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseResult<T> {
    /**
     * 状态码
     */
    @Schema(description = "状态码")
    private Integer code;
    /**
     * 提示信息，如果有错误时，前端可以获取该字段进行提示
     */
    @Schema(description = "提示信息")
    private String msg;
    /**
     * 查询到的结果数据，
     */
    @Schema(description = "查询到的结果数据")
    private T data;

    /**
     * 成功响应，不需要返回数据
     */
    public static <T> ResponseResult<T> success() {
        return new ResponseResult<>(RespEnum.SUCCESS.getCode(), RespEnum.SUCCESS.getMsg(), null);
    }

    /**
     * 成功响应，需要返回数据
     */
    public static <T> ResponseResult<T> success(T data) {
        return new ResponseResult<>(RespEnum.SUCCESS.getCode(), RespEnum.SUCCESS.getMsg(), data);
    }

    /**
     * 成功响应，需要返回数据跟信息
     */
    public static <T> ResponseResult<T> success(T data, String msg) {
        return new ResponseResult<>(RespEnum.SUCCESS.getCode(), msg, data);
    }


    /**
     * 失败响应，不需要返回数据
     */
    public static <T> ResponseResult<T> failure(Integer code, String msg) {
        return new ResponseResult<>(code, msg, null);
    }

    /**
     * 失败响应，不需要返回数据
     */
    public static <T> ResponseResult<T> failure() {
        return new ResponseResult<>(RespEnum.FAILURE.getCode(), RespEnum.FAILURE.getMsg(), null);
    }
    public static <T> ResponseResult<T> failure(String msg) {
        return new ResponseResult<>(RespEnum.FAILURE.getCode(), msg, null);
    }
    public static <T> ResponseResult<T> failure(T data)  {
        return new ResponseResult<>(RespEnum.FAILURE.getCode(), RespEnum.FAILURE.getMsg(), data);
    }


    /**
     * 转json字符串
     *
     * @return {@link String}
     */
    public String asJsonString() {
        return JSONObject.toJSONString(this, JSONWriter.Feature.WriteNulls);
    }


}
