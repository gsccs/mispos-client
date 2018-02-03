package com.allinpay.mis.aip.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.allinpay.mis.aip.model.ReqData;
import com.allinpay.mis.aip.model.RspData;
import com.allinpay.mis.client.ui.AppMainWindow;
import com.allinpay.mis.tools.CacheUtil;
import com.allinpay.mis.tools.FileUtil;
import com.allinpay.mis.tools.PropUtil;

public class ComPosService implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = LoggerFactory.getLogger(AppMainWindow.class);
	
	private CommDataManager commDataManager = new CommDataManager();
	private SocketDataService frmSocketDataService;

	public long MC_Tran(Object req, Object res) {
		
		logger.debug("MC_Tran Start!");
		ReqData request = (ReqData) req;
		RspData response = (RspData) res;

		String reqCardType = request.GetValue("cardtype");
		String reqTransType = request.GetValue("TransType");
		String cardClzPath = request.GetValue("cardClzPath");

		Calendar cal = Calendar.getInstance();
		String day = String.valueOf(cal.get(5));

		long result = 0L;
		try {
			
			
			CacheUtil.init();
			response.PutValue("cardtype", request.GetValue("cardtype"));
			response.PutValue("TransType", request.GetValue("TransType"));
			
			String bum = CacheUtil.getTimeOut();
			int timeOut = Integer.parseInt(bum);
			if (Long.parseLong(request.GetValue("transtype")) == 21L) {
				/*FrmGetParameter frmGetParameter = new FrmGetParameter(
						reqCardType, cardClzPath);
				frmGetParameter.setVisible(true);
				response.PutValue("RejCode", "00");
				return 0L;
				*/
				
				//交易金额
				request.PutValue("Amount", "110");
				request.PutValue("memo", "");
			}
			
			
			//撤销
			if ((Long.parseLong(request.GetValue("transtype")) == 3L)
					&& (request.GetValue("OldTraceNumber").equals(""))) {
				//交易金额
				request.PutValue("Amount", "110");
				//原交易凭证号
				request.PutValue("OldTraceNumber", "");
				request.PutValue("memo", "");
			}
			
			
			//退货
			if ((Long.parseLong(request.GetValue("transtype")) == 4L)) {
				//交易金额
				request.PutValue("Amount", "110");
				//交易参考号
				request.PutValue("HostSerialNumber", "");
				//原交易日期
				request.PutValue("TransDate", "");
			}
			
			if (Long.parseLong(request.GetValue("transtype")) == 127L) {
				ReqData request2 = new ReqData();
				request2.PutValue("appname", "MANAGER");
				request2.PutValue("Transtype", Long.toString(7L));
				List<String> list = new ArrayList();
				list = FileUtil.getComList();
				for (String com : list) {
					CacheUtil.setComm(com);
					result = this.commDataManager.sendAndReceive(request2,
							response, 3, null);
					if (result == 0L) {
						if (this.commDataManager.getCommData().GetValue("rejcode").equals("00")) {
							PropUtil.writeMisProp("COMPORT",CacheUtil.getComport());
							response.PutValue("rejcode", "00");
							request2 = null;
							return result;
						}
					}
				}
				request2 = null;
				return -124L;
			}
			//String bum = Manager_Util.getTimeOut();
			//int timeOut = Integer.parseInt(bum);
			if (CacheUtil.getIsSettle().booleanValue()) {
				if (!day.equals(PropUtil.getMisValue("SETTLETIME"))) {
					request.PutValue("TransType", Long.toString(14L));
					result = this.commDataManager.sendAndReceive(request, response,timeOut, null);
					while (this.commDataManager.getCommData().GetValue("TransType")
							.equals(String.valueOf(17L))) {
						this.frmSocketDataService = new SocketDataService(
								this.commDataManager.getCommData());
						result = this.frmSocketDataService.showMe();
						if (result == 0L) {
							this.commDataManager.getCommData().setContentTo(
									this.frmSocketDataService
											.getBackSocketBuf());
							this.commDataManager.getCommData().PutValue("RejCode",
									"00");
						} else {
							this.commDataManager.getCommData().setContentTo(
									this.frmSocketDataService
											.getBackSocketBuf());
							this.commDataManager.getCommData().PutValue("RejCode",
									Long.toString(result));
						}
						result = this.commDataManager.sendAndReceive(request,
								response, timeOut,
								this.commDataManager.getCommData());
						if (result != 0L) {
							return result;
						}
						this.frmSocketDataService = null;
					}
					request.PutValue("TransType", reqTransType);
					PropUtil.writeMisProp("SETTLETIME", String.valueOf(day));
					Thread.sleep(8000L);
				}
			}
			
			//
			result = this.commDataManager.sendAndReceive(request, response,timeOut, null);
			//System.out.println("TransType:"+this.commDataManager.getCommData().GetValue("TransType"));
			while (this.commDataManager.getCommData().GetValue("TransType")
					.equals(String.valueOf(17L))) {
				this.frmSocketDataService = new SocketDataService(this.commDataManager.getCommData());
				result = this.frmSocketDataService.showMe();
				if (result == 0L) {
					this.commDataManager.getCommData().PutValue("RejCode", "00");
				} else {
					this.commDataManager.getCommData().PutValue("RejCode",Long.toString(result));
				}
				this.commDataManager.getCommData().setContentTo(this.frmSocketDataService.getBackSocketBuf());
				result = this.commDataManager.sendAndReceive(request, response,timeOut, this.commDataManager.getCommData());
				if (result != 0L) {
					return result;
				}
				this.frmSocketDataService = null;
			}
			if (result == 0L) {
				this.commDataManager.copyDataToResponse(response);
			}
			if ((CacheUtil.getPosType().equals("SOFTPOS"))
					&& (Long.parseLong(request.GetValue("TransType")) == 1L)) {
				FileUtil.buildTerminalInfo(response);
			}
			if ((CacheUtil.getPosType().equals("SOFTPOS"))
					&& (response.GetValue("RejCode").equals("00"))) {
				if ((Long.parseLong(response.GetValue("TransType")) == 2L)
						|| (Long.parseLong(response.GetValue("TransType")) == 3L)
						|| (Long.parseLong(response.GetValue("TransType")) == 4L)) {
					if (Long.parseLong(request.GetValue("TransType")) == 16L) {
						FileUtil.printTransFileByTemplate(CacheUtil
								.getPapers().longValue(), true, response);
					} else {
						FileUtil.printTransFileForSYB(
								response.GetValue("m_PrintContent"), "POS签购单");
					}
				} else if ((Long.parseLong(response.GetValue("TransType")) == 14L)
						&& (response.GetValue("RejCode").equals("00"))) {
					if (Long.parseLong(request.GetValue("TransType")) == 19L) {
						FileUtil.printSettleFile(true, response);
					} else {
						FileUtil.printSettleFile(false, response);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		FileUtil.writePacket("MC_Tran End!");
		return result;
	}
}
