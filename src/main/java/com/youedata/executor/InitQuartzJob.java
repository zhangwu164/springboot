package com.youedata.executor;


import com.youedata.common.QuartzManager;
import com.youedata.common.SpringUtil;
import com.youedata.config.Config;
import com.youedata.executor.quartzJob.TestJob;

/**
 * <p>Description:
 * 所有Quartzjob 任务 入口
 * </p>
 * <p>Copyright: Copyright (c) 2018</p>
 * <p>Company:成都优易数据有限公司</p>
 *
 * @author wgj
 * @version V1.0
 * @PackageName:com.youedata.daas.connector.jobs
 * @ProjectName:data-connector
 * @date 2018/8/8
 */


public class InitQuartzJob {

    public static String JOB_GROUP_NAME = "YOUEDATA_JOB_GROUP";
    public static String TRIGGER_GROUP_NAME = "YOUEDATA_JOB_GROUP";

    Config config = SpringUtil.getBean(Config.class);

    /**
     * 所有Quartz 调度任务的 启动入口
     */

    public void startQuartzJob() {

        //每天晚上凌晨执行一次
        QuartzManager.addJob("TestJob", JOB_GROUP_NAME, "CycleTaskJob_TRIGGER", TRIGGER_GROUP_NAME, TestJob.class,config.getCronTest());

    }

}
