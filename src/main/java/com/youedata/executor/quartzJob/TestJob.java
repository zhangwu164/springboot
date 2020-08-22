package com.youedata.executor.quartzJob;

import com.alibaba.fastjson.JSONObject;
import com.youedata.common.DateUtils;
import com.youedata.common.SpringUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * <p>Description:
 *
 * </p>
 * <p>Copyright: Copyright (c) 2018</p>
 * <p>Company:成都优易数据有限公司</p>
 *
 * @author wgj
 * @version V1.0
 * @PackageName:com.youedata.executor.quartzJob
 * @ProjectName:SpringBootTemp
 * @date 2019/9/18 10/06
 */
public class TestJob implements Job {

    private static final Logger log = LoggerFactory.getLogger(TestJob.class);

//    CodeDao codeDao = SpringUtil.getBean(CodeDao.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
//        List<CodePo> lists = codeDao.getCodeList();
        log.info("-------------------job stime:{}--------------------",new Object[]{DateUtils.getNowTime()});
//        for(CodePo codePo : lists){
//            log.info(JSONObject.toJSONString(codePo));
//        }
        log.info("-------------------job etime:{}--------------------",new Object[]{DateUtils.getNowTime()});
    }

}
