package com.youedata.common;

import org.quartz.*;
import org.quartz.DateBuilder.IntervalUnit;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>Description:
 *
 * </p>
 * <p>Copyright: Copyright (c) 2018</p>
 * <p>Company:成都优易数据有限公司</p>
 *
 * @author wgj
 * @version V1.0
 * @PackageName:org.apache.kafka.connect.util
 * @ProjectName:kafka
 * @date 2018/11/23 13/18
 */
public class QuartzManager {

    public static String jobGroupName="job-youedata-jobGroupName";
    public static String triggerGroupName="trigger-youedata-jobGroupName";
    private static final Logger log = LoggerFactory.getLogger(QuartzManager.class);
    private static StdSchedulerFactory schedulerFactory = new StdSchedulerFactory();
    public static Scheduler sched=null;

    /**
     * Quartz 任务间隔时间
     * MILLISECONDS 毫米
     * SECONDS 秒
     * MINUTES 分钟
     * HOURS 小时
     */
    public enum QuartzIntervalMode {
        //MILLISECONDS,
        SECONDS,
        MINUTES,
        HOURS;
    }

    public static synchronized void instanceScheduler() {
        try {
            if(sched==null) {
                Properties props = new Properties();
                props.put("org.quartz.scheduler.instanceName", "project-num1-youedata-quartz");
                props.put("org.quartz.threadPool.threadCount", "20");
                schedulerFactory.initialize(props);
                sched=schedulerFactory.getScheduler();
                log.info("...Quartz 调度初始化成功！...");
            }
        } catch (SchedulerException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
            log.error("....quartz 调度 初始化错误 {}...",e.getMessage());
        }
    }


    /**
     * @Description: 添加一个定时任务
     *
     * @param jobName 任务名
     * @param triggerName 触发器名
     * @param jobClass  任务
     * @param cron   时间设置，参考quartz说明文档
     */
    public static void addJob(String jobName,String triggerName, Class jobClass, String cron,Map<String,Object> jobDataMap){
        addJob(jobName,jobGroupName,triggerName,triggerGroupName,jobClass,cron,jobDataMap,"");
    }

    /**
     * @Description: 添加一个定时任务
     *
     * @param jobName 任务名
     * @param triggerName 触发器名
     * @param jobClass  任务
     * @param cron   时间设置，参考quartz说明文档
     */
    public static void addJob(String jobName,String triggerName, Class jobClass, String cron,Map<String,Object> jobDataMap,String triggerStartTime){
        addJob(jobName,jobGroupName,triggerName,triggerGroupName,jobClass,cron,jobDataMap,triggerStartTime);
    }

    /**
     * @Description: 添加一个定时任务
     *
     * @param jobName 任务名
     * @param triggerName 触发器名
     * @param jobClass  任务
     * @param cron   时间设置，参考quartz说明文档
     */
    public static void addJob(String jobName,String triggerName, Class jobClass, String cron){
        addJob(jobName,jobGroupName,triggerName,triggerGroupName,jobClass,cron,null,"");
    }

    /**
     * @Description: 添加一个定时任务
     *
     * @param jobName 任务名
     * @param jobGroupName  任务组名
     * @param triggerName 触发器名
     * @param triggerGroupName 触发器组名
     * @param jobClass  任务
     * @param cron   时间设置，参考quartz说明文档
     */
    public static void addJob(String jobName, String jobGroupName,String triggerName, String triggerGroupName, Class jobClass, String cron){
        addJob(jobName,jobGroupName,triggerName,triggerGroupName,jobClass,cron,null,"");
    }
    /**
     * @Description: 添加一个定时任务
     *
     * @param jobName 任务名
     * @param jobGroupName  任务组名
     * @param triggerName 触发器名
     * @param triggerGroupName 触发器组名
     * @param jobClass  任务
     * @param cron   时间设置，参考quartz说明文档
     * @param triggerStartTime 任务启动时间  默认 当前时间 yyyy-MM-dd HH:mm:ss
     * @param jobDataMap map数据传输
     */
    public static void addJob(String jobName, String jobGroupName,String triggerName, String triggerGroupName, Class jobClass, String cron , Map<String,Object> jobDataMap, String triggerStartTime) {
        try {
            //Scheduler sched = getInstance();
            // 任务名，任务组，任务执行类
            JobDetail jobDetail= JobBuilder.newJob(jobClass).withIdentity(jobName,jobGroupName).build();
            if(jobDataMap!=null){
                for(String key:jobDataMap.keySet()){
                    jobDetail.getJobDataMap().put(key,jobDataMap.get(key));
                }
            }
            // 触发器
            TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
            // 触发器名,触发器组
            triggerBuilder.withIdentity(triggerName, triggerGroupName);
            if("".equals(triggerStartTime)) {
                triggerBuilder.startNow();
            }else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                if(System.currentTimeMillis()>sdf.parse(triggerStartTime).getTime()){//启动时间为小于当前时间
                    CronExpression exp = new CronExpression(cron);
                    Date startDate = exp.getNextValidTimeAfter(new Date());
                    triggerBuilder.startAt(startDate);
                    log.warn("【...启动时间 {} 已过时，cron重新计算下一次有效启动时间 {} ...】",triggerStartTime,sdf.format(startDate));
                }else{
                    triggerBuilder.startAt(sdf.parse(triggerStartTime));
                }
            }
            // 触发器时间设定
            triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));
            // 创建Trigger对象
            CronTrigger trigger = (CronTrigger) triggerBuilder.build();

            // 调度容器设置JobDetail和Trigger
            sched.scheduleJob(jobDetail, trigger);
            // 启动
            if (!sched.isShutdown()) {
                sched.start();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }




    /**
     * @Description: 添加一个定时 SimpleScheduleBuilder 任务
     *
     * @param jobName 任务名
     * @param jobGroupName  任务组名
     * @param triggerName 触发器名
     * @param triggerGroupName 触发器组名
     * @param jobClass  任务
     * @param jobDataMap 参数传递
     * @param triggerStartTime 任务开始时间 默认延后10秒启动
     * @param interval  任务间隔
     * @param intervalMode     SECONDS 秒   MINUTES 分钟   HOURS 小时
     * @param repeatCount 任务执行次数 0表示一次
     */

    public static void addSimpleScheduleJob(String jobName, String jobGroupName,String triggerName, String triggerGroupName,
                                            Class jobClass,Map<String,Object> jobDataMap,String triggerStartTime,QuartzIntervalMode intervalMode,Integer interval,Integer repeatCount) {
        try {
            //Scheduler sched = getInstance();
            // 任务名，任务组，任务执行类
            JobDetail jobDetail= JobBuilder.newJob(jobClass).withIdentity(jobName,jobGroupName).build();
            if(jobDataMap!=null){
                for(String key:jobDataMap.keySet()){
                    jobDetail.getJobDataMap().put(key,jobDataMap.get(key));
                }
            }
            //创建触发器
            SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule();
            if(intervalMode!=null) {
                if(intervalMode.equals(QuartzIntervalMode.SECONDS)) {
                    simpleScheduleBuilder.withIntervalInSeconds(interval);
                }else if(intervalMode.equals(QuartzIntervalMode.MINUTES)){
                    simpleScheduleBuilder.withIntervalInMinutes(interval);
                }else if(intervalMode.equals(QuartzIntervalMode.HOURS)) {
                    simpleScheduleBuilder.withIntervalInHours(interval);
                }else {
                    simpleScheduleBuilder.withIntervalInSeconds(5);
                }
            }else {
                simpleScheduleBuilder.withIntervalInSeconds(5);
            }

//            if(repeatCount==0) {//一直重复
//            	simpleScheduleBuilder.repeatForever();
//            }else {
            simpleScheduleBuilder.withRepeatCount(repeatCount);
//            }

            // 触发器
            TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
            // 触发器名,触发器组
            triggerBuilder.withIdentity(triggerName, triggerGroupName);
            if("".equals(triggerStartTime)) {
                triggerBuilder.startAt(DateBuilder.futureDate(10,IntervalUnit.SECOND)) ;//延后执行
                //triggerBuilder.startNow();
            }else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                triggerBuilder.startAt(sdf.parse(triggerStartTime));
            }
            // 触发器时间设定
            triggerBuilder.withSchedule(simpleScheduleBuilder);
            // 创建Trigger对象
            SimpleTrigger trigger = (SimpleTrigger) triggerBuilder.build();

            // 调度容器设置JobDetail和Trigger
            sched.scheduleJob(jobDetail, trigger);
            // 启动
            if (!sched.isShutdown()) {
                sched.start();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 一次性任务
     *
     * @param jobName
     * @param triggerName
     * @param jobClass
     * @param triggerStartTime
     */
    public static void addSimpleScheduleJob(String jobName,String triggerName,Class jobClass,String triggerStartTime) {
        addSimpleScheduleJob( jobName,
                jobGroupName,
                triggerName,
                triggerGroupName,
                jobClass,
                null,
                triggerStartTime,
                null,
                0,
                0);
    }
    public static void addSimpleScheduleJob(String jobName,String triggerName,Class jobClass,Map<String,Object> jobDataMap,String triggerStartTime) {
        addSimpleScheduleJob( jobName,
                jobGroupName,
                triggerName,
                triggerGroupName,
                jobClass,
                jobDataMap,
                triggerStartTime,
                null,
                0,
                0);
    }
    public static void addSimpleScheduleJob(String jobName,String triggerName,Class jobClass,Integer repeatCount) {
        addSimpleScheduleJob( jobName,
                jobGroupName,
                triggerName,
                triggerGroupName,
                jobClass,
                null,
                "",
                null,
                0,
                repeatCount);
    }
    public static void addSimpleScheduleJob(String jobName,String triggerName,Class jobClass,Map<String,Object> jobDataMap,Integer repeatCount) {
        addSimpleScheduleJob( jobName,
                jobGroupName,
                triggerName,
                triggerGroupName,
                jobClass,
                jobDataMap,
                "",
                null,
                0,
                repeatCount);
    }
    /**
     *
     * @Description: 添加一个定时 SimpleScheduleBuilder 任务
     *
     * @param jobName 任务名
     * @param triggerName 触发器名
     * @param jobClass  任务
     * @param interval  任务间隔
     * @param intervalMode     SECONDS 秒   MINUTES 分钟   HOURS 小时
     * @param repeatCount 任务执行次数
     */
    public static void addSimpleScheduleJob(String jobName,String triggerName, Class jobClass,QuartzIntervalMode intervalMode,Integer interval,Integer repeatCount){
        addSimpleScheduleJob(jobName,jobGroupName,triggerName,triggerGroupName,jobClass,null,"",intervalMode,interval,repeatCount);
    }

    /**
     * @Description: 添加一个定时 SimpleScheduleBuilder 任务
     *
     * @param jobName 任务名
     * @param triggerName 触发器名
     * @param jobClass  任务
     * @param jobDataMap 参数传递
     * @param interval  任务间隔
     * @param intervalMode     SECONDS 秒   MINUTES 分钟   HOURS 小时
     * @param repeatCount 任务执行次数
     */

    public static void addSimpleScheduleJob(String jobName,String triggerName, Class jobClass,Map<String,Object> jobDataMap,QuartzIntervalMode intervalMode,Integer interval,Integer repeatCount){
        addSimpleScheduleJob(jobName,jobGroupName,triggerName,triggerGroupName,jobClass,jobDataMap,"",intervalMode,interval,repeatCount);
    }

    /**
     * @Description: 修改一个任务的触发时间
     *
     * @param jobName
     * @param jobGroupName
     * @param triggerName 触发器名
     * @param triggerGroupName 触发器组名
     * @param cron   时间设置，参考quartz说明文档
     */
    public static void modifyJobTime(String jobName,
                                     String jobGroupName, String triggerName, String triggerGroupName, String cron) {
        try {
            //Scheduler sched = getInstance();
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
            CronTrigger trigger = (CronTrigger) sched.getTrigger(triggerKey);
            if (trigger == null) {
                return;
            }

            String oldTime = trigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(cron)) {
                /** 方式一 ：调用 rescheduleJob 开始 */
                // 触发器
                TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
                // 触发器名,触发器组
                triggerBuilder.withIdentity(triggerName, triggerGroupName);
                triggerBuilder.startNow();
                // 触发器时间设定
                triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));
                // 创建Trigger对象
                trigger = (CronTrigger) triggerBuilder.build();
                // 方式一 ：修改一个任务的触发时间
                sched.rescheduleJob(triggerKey, trigger);
                /** 方式一 ：调用 rescheduleJob 结束 */

                /** 方式二：先删除，然后在创建一个新的Job  */
                //JobDetail jobDetail = sched.getJobDetail(JobKey.jobKey(jobName, jobGroupName));
                //Class<? extends Job> jobClass = jobDetail.getJobClass();
                //removeJob(jobName, jobGroupName, triggerName, triggerGroupName);
                //addJob(jobName, jobGroupName, triggerName, triggerGroupName, jobClass, cron);
                /** 方式二 ：先删除，然后在创建一个新的Job */
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @Description: 移除一个任务
     *
     * @param jobName
     * @param jobGroupName
     * @param triggerName
     * @param triggerGroupName
     */
    public static void removeJob(String jobName, String jobGroupName,
                                 String triggerName, String triggerGroupName) {
        try {

            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);

            sched.pauseTrigger(triggerKey);// 停止触发器
            sched.unscheduleJob(triggerKey);// 移除触发器
            sched.deleteJob(JobKey.jobKey(jobName, jobGroupName));// 删除任务
            log.info("...{}任务被移除...",jobName);
        } catch (Exception e) {
            log.error("...{}任务被移除...",jobName);
            throw new RuntimeException(e);
        }
    }

    /**
     * @Description:启动所有定时任务
     */
    public static void startJobs() {
        try {
            //Scheduler sched = getInstance();
            sched.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @Description:关闭所有定时任务
     */
    public static void shutdownJobs() {
        try {
            //Scheduler sched = getInstance();
            if (!sched.isShutdown()) {
                sched.shutdown();
                log.info("...关闭所有任务 正常...");
            }
        } catch (Exception e) {
            log.error("...关闭所有任务 异常{}...",e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
