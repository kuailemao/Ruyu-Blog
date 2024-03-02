package xyz.kuailemao.config;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import xyz.kuailemao.quartz.RefreshTheCache;

/**
 * 定义任务描述和具体的执行时间
 */
//@Configuration
public class QuartzConfig {
    @Bean
    public JobDetail jobDetail() {
        //指定任务描述具体的实现类
        return JobBuilder.newJob(RefreshTheCache.class)
                // 指定任务的名称
                .withIdentity("refreshTheCache")
                // 任务描述
                .withDescription("任务描述：用于每五分钟刷新一次常用数据缓存")
                // 每次任务执行后进行存储
                .storeDurably()
                .build();
    }
    
    @Bean
    public Trigger trigger() {
        //创建触发器
        return TriggerBuilder.newTrigger()
                // 绑定工作任务
                .forJob(jobDetail())
                // 每隔 5 分钟执行一次 job
                .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(60 * 5))
                .build();
    }
}