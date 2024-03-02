package xyz.kuailemao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.kuailemao.domain.dto.LogDTO;
import xyz.kuailemao.domain.dto.LogDeleteDTO;
import xyz.kuailemao.domain.entity.Log;
import xyz.kuailemao.domain.response.ResponseResult;
import xyz.kuailemao.domain.vo.PageVO;


/**
 * (Log)表服务接口
 *
 * @author kuailemao
 * @since 2023-12-12 09:12:32
 */
public interface LogService extends IService<Log> {
    /**
     * 搜索/显示操作日志
     *
     * @param LogDTO  查询条件
     * @param aLong
     * @param current
     * @return 数据列表
     */
    PageVO searchLog(LogDTO LogDTO, Long aLong, Long current);

    /**
     *  删除/清空操作日志
     * @param logDeleteDTO id集合
     * @return  响应结果
     */
    ResponseResult<Void> deleteLog(LogDeleteDTO logDeleteDTO);

}
