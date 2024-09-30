package xyz.kuailemao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import xyz.kuailemao.domain.entity.BlackList;


/**
 * (BlackList)表数据库访问层
 *
 * @author kuailemao
 * @since 2024-09-05 16:13:19
 */
public interface BlackListMapper extends BaseMapper<BlackList> {

    @Delete("DELETE FROM t_black_list WHERE ip_info -> '$.createIp' = #{ip}")
    long deleteByIp(String ip);
}
