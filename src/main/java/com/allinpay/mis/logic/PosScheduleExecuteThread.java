package com.allinpay.mis.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.allinpay.mis.aip.service.ComPosService;
import com.allinpay.mis.client.ui.AppMainWindow;
import com.allinpay.mis.client.ui.panel.StatusPanel;
import com.allinpay.mis.tools.*;

/**
 * 定时任务执行器，继承于执行器线程类
 *
 * @author x.d zhang
 */
public class PosScheduleExecuteThread extends ExecuteThread {
	
	private static final Logger logger = LoggerFactory.getLogger(AppMainWindow.class);
	
    public void run() {
    	
        if (StatusPanel.isRunning == false) {
            StatusPanel.isRunning = true;
            this.setName("ScheduleExecuteThread");
            StatusPanel.buttonStartNow.setEnabled(false);
            long enterTime = System.currentTimeMillis(); // 毫秒数
            StatusPanel.progressTotal.setMaximum(6);
            // 初始化变量
            init();
            // 测试连接
            boolean isLinked = true;
            		//testLink();
            
            if (isLinked) {
                StatusPanel.progressTotal.setValue(1);
                
                // 分析配置文件
                boolean isAnalyseSuccess = analyseConfigFile();
                if (isAnalyseSuccess) {
                    StatusPanel.progressTotal.setValue(2);
                    // 备份
                    if ("true".equals(ConstantsTools.CONFIGER.getAutoBak())) {
                        backUp();
                    }
                    StatusPanel.progressTotal.setValue(3);
                    // 建立新快照
                    boolean isSnapSuccess = newSnap();
                    if (isSnapSuccess) {
                        StatusPanel.progressTotal.setValue(4);
                        // 对比快照,并根据对比结果生成SQL
                        boolean isDiffSuccess = diffSnap();
                        if (isDiffSuccess) {
                            StatusPanel.progressTotal.setValue(5);
                            // 执行SQL
                            boolean isExecuteSuccess = executeSQL();
                            if (isExecuteSuccess) {
                                StatusPanel.progressTotal.setValue(6);

                                // 设置持续时间
                                long leaveTime = System.currentTimeMillis(); // 毫秒数
                                float minutes = (float) (leaveTime - enterTime) / 1000; // 秒数
                                StatusLog.setKeepTime(String.valueOf(minutes));
                                // 设置成功次数+1
                                String success = String
                                        .valueOf((Long.parseLong(ConstantsTools.CONFIGER.getSuccessTime()) + 1));
                                StatusLog.setSuccess(success);
                                StatusLog.setStatusDetail(PropUtil.getProperty("ds.logic.currentSyncFinish"), LogLevel.INFO);
                            } else {
                                // 恢复快照备份
                                SnapManage.recoverSnapBak();

                                String fail = String
                                        .valueOf((Long.parseLong(ConstantsTools.CONFIGER.getFailTime()) + 1));
                                StatusLog.setFail(fail);
                            }

                        } else {
                            // 恢复快照备份
                            SnapManage.recoverSnapBak();

                            String fail = String.valueOf((Long.parseLong(ConstantsTools.CONFIGER.getFailTime()) + 1));
                            StatusLog.setFail(fail);
                        }

                    } else {
                        // 恢复快照备份
                        SnapManage.recoverSnapBak();

                        String fail = String.valueOf((Long.parseLong(ConstantsTools.CONFIGER.getFailTime()) + 1));
                        StatusLog.setFail(fail);
                    }

                } else {
                    String fail = String.valueOf((Long.parseLong(ConstantsTools.CONFIGER.getFailTime()) + 1));
                    StatusLog.setFail(fail);
                }

            } else {
                String fail = String.valueOf((Long.parseLong(ConstantsTools.CONFIGER.getFailTime()) + 1));
                StatusLog.setFail(fail);
            }
            // 设置显示下一次执行时间
            StatusPanel.labelNextTime.setText(PropUtil.getProperty("ds.ui.schedule.nextTime") + Utils.getNextSyncTime());
            StatusPanel.isRunning = false;
        }
    }
}
