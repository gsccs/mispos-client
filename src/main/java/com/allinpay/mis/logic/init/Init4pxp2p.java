package com.allinpay.mis.logic.init;

import com.allinpay.mis.client.ui.panel.StatusPanel;
import com.allinpay.mis.tools.DbUtilMySQL;
import com.allinpay.mis.tools.DbUtilSQLServer;
import com.allinpay.mis.tools.LogLevel;
import com.allinpay.mis.tools.StatusLog;
import com.opencsv.CSVWriter;

import java.io.File;

public class Init4pxp2p {

    public static boolean init() {
        StatusLog.setStatusDetail("开始初始化第一次快照，请耐心等待……", LogLevel.INFO);

        boolean isSuccess = true;
        DbUtilMySQL mySql = DbUtilMySQL.getInstance();
        DbUtilSQLServer SQLServer = DbUtilSQLServer.getInstance();
        CSVWriter csvWriterRole = null;
        CSVWriter csvWriterUser = null;
        File snapsDir = null;
        StatusPanel.progressCurrent.setMaximum(7);
        int progressValue = 0;
        StatusPanel.progressCurrent.setValue(progressValue);

		/*Do Sth you need to init*/
        return isSuccess;
    }

}
