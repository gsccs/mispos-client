package com.allinpay.mis.aip.service;

import java.io.UnsupportedEncodingException;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.core.status.Status;

import com.allinpay.mis.aip.comm.CommData;
import com.allinpay.mis.aip.model.ReqData;
import com.allinpay.mis.aip.model.RspData;
import com.allinpay.mis.client.ui.AppMainWindow;
import com.allinpay.mis.client.ui.panel.StatusPanel;
import com.allinpay.mis.tools.ArrayUtil;
import com.allinpay.mis.tools.CacheUtil;
import com.allinpay.mis.tools.FileUtil;
import com.allinpay.mis.tools.HexUtils;
import com.allinpay.mis.tools.PropUtil;

public class CommDataManager {
	
	private static final Logger logger = LoggerFactory.getLogger(AppMainWindow.class);
	
	private CommData commData;
	long result;
	private int postion = 0;

	private void addCommDataBuf(byte[] data, String tagValue, byte[] dataValue,
			Integer postion) {
		byte[] ascBuf = new byte[4];
		byte[] bcdBuf = new byte[2];
		ascBuf = tagValue.getBytes();
		bcdBuf = FileUtil.AscToBcd(ascBuf, bcdBuf, 4);
		for (int i = 0; i < bcdBuf.length; i++) {
			data[postion.intValue()] = bcdBuf[i];
			postion = Integer.valueOf(postion.intValue() + 1);
		}
		int length = dataValue.length;
		data[postion.intValue()] = ((byte) (length / 256));
		postion = Integer.valueOf(postion.intValue() + 1);
		data[postion.intValue()] = ((byte) (length % 256));
		postion = Integer.valueOf(postion.intValue() + 1);
		for (int i = 0; i < dataValue.length; i++) {
			data[postion.intValue()] = dataValue[i];
			postion = Integer.valueOf(postion.intValue() + 1);
		}
		this.postion = postion.intValue();
	}

	private void addCommDataBuf(byte[] data, String tagValue,
			Integer dataValue, Integer postion) {
		byte[] ascBuf = new byte[4];
		byte[] bcdBuf = new byte[2];
		ascBuf = tagValue.getBytes();
		bcdBuf = FileUtil.AscToBcd(ascBuf, bcdBuf, 4);
		for (int i = 0; i < bcdBuf.length; i++) {
			data[postion.intValue()] = bcdBuf[i];
			postion = Integer.valueOf(postion.intValue() + 1);
		}
		int length = 1;
		data[postion.intValue()] = ((byte) (length / 256));
		postion = Integer.valueOf(postion.intValue() + 1);
		data[postion.intValue()] = ((byte) (length % 256));
		postion = Integer.valueOf(postion.intValue() + 1);
		data[postion.intValue()] = dataValue.byteValue();
		postion = Integer.valueOf(postion.intValue() + 1);
		this.postion = postion.intValue();
	}

	private void addCommDataBuf(byte[] data, String tagValue, String dataValue,
			Integer postion) throws UnsupportedEncodingException {
		byte[] ascBuf = new byte[4];
		byte[] bcdBuf = new byte[2];
		ascBuf = tagValue.getBytes();
		bcdBuf = FileUtil.AscToBcd(ascBuf, bcdBuf, 4);
		for (int i = 0; i < bcdBuf.length; i++) {
			data[postion.intValue()] = bcdBuf[i];
			postion = Integer.valueOf(postion.intValue() + 1);
		}
		byte[] tempBuf = dataValue.getBytes("gbk");
		int length = tempBuf.length;
		data[postion.intValue()] = ((byte) (length / 256));
		postion = Integer.valueOf(postion.intValue() + 1);
		data[postion.intValue()] = ((byte) (length % 256));
		postion = Integer.valueOf(postion.intValue() + 1);
		for (int i = 0; i < tempBuf.length; i++) {
			data[postion.intValue()] = tempBuf[i];
			postion = Integer.valueOf(postion.intValue() + 1);
		}
		this.postion = postion.intValue();
	}

	public void copyDataToResponse(RspData response) {
		Set<String> keySet = this.commData.getCommdata().keySet();
		for (String key : keySet) {
			response.PutValue(key, this.commData.GetValue(key));
		}
	}

	private void CreateCommData(ReqData request) {
		try {
			this.commData = new CommData();
			if (!request.GetValue("Amount").equals("")) {
				request.PutValue("Amount",FileUtil.formatAmt(request.GetValue("Amount"), 12));
			}
			/*if (!request.GetValue("appname").equals("MANAGER")) {
				this.commData.PutValue("AppName", PropUtil.getAppName());
			} else {
				this.commData.PutValue("AppName", request.GetValue("appname"));
			}*/
			
			this.commData.PutValue("AppName", request.GetValue("appname"));
			this.commData.PutValue("TransType",
					FileUtil.formatType(request.GetValue("TransType")));
			this.commData.PutValue("Amount", request.GetValue("Amount"));
			this.commData.PutValue("OldTraceNumber",
					request.GetValue("OldTraceNumber"));
			this.commData.PutValue("HostserialNumber ",
					request.GetValue("HostserialNumber"));
			this.commData.PutValue("TransDate", request.GetValue("TransDate"));
			this.commData
					.PutValue("AuthNumber", request.GetValue("AuthNumber"));
			this.commData
					.PutValue("CardNumber", request.GetValue("CardNumber"));
			this.commData
					.PutValue("ExpireDate", request.GetValue("ExpireDate"));
			this.commData
					.PutValue("TransCheck", request.GetValue("TransCheck"));
			this.commData.PutValue("Memo", request.GetValue("Memo"));
			this.commData.PutValue("PosNumber", request.GetValue("PosNumber"));
			this.commData.PutValue("Operator", request.GetValue("Operator"));
			this.commData.PutValue("StoreNumber",
					request.GetValue("StoreNumber"));
			this.commData.PutValue("orderNumber",
					request.GetValue("orderNumber"));
			
			this.commData.PutValue("JFDH", request.GetValue("JFDH"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean equqlsTransType(String[] allowTypes, String type) {
		for (int i = 0; i < allowTypes.length; i++) {
			if (type.trim().equals(allowTypes[i].trim())) {
				return true;
			}
		}
		return false;
	}

	private long getBackData(byte[] backCommBuf) {
		byte[] bcdbuf = new byte[2];
		int dataLength = 0;
		int dataPostion = 0;
		String tag = "";
		String value = "";
		try {
			dataLength = HexUtils.toHexInteger(backCommBuf[1]).intValue() * 100
					+ HexUtils.toHexInteger(backCommBuf[2]).intValue();
			FileUtil.writePacket("COM_RECEIVE", this.commData.getBackComData(),
					dataLength + 5);

			byte[] commBuf = ArrayUtil.copy(backCommBuf, 3, dataLength);
			dataPostion = 0;
			while (dataPostion < commBuf.length) {
				bcdbuf = ArrayUtil.copy(commBuf, dataPostion, 2);
				tag = HexUtils.byte2hex(bcdbuf);
				dataPostion += 2;
				bcdbuf = ArrayUtil.copy(commBuf, dataPostion, 2);
				dataLength = getTagValueLength(bcdbuf);
				dataPostion += 2;
				if (tag.trim().equals("9F00")) {
					value = getBufValue(commBuf, dataLength, dataPostion);
					this.commData.PutValue("appname", value);
					dataPostion += dataLength;
				} else if (tag.trim().equals("9F01")) {
					byte[] valueBuf = ArrayUtil.copy(commBuf, dataPostion,
							dataLength);
					value = HexUtils.byte2Bin(valueBuf[0]);
					this.commData.PutValue("TransType", value);
					dataPostion += dataLength;
				} else if (tag.trim().equals("9F02")) {
					value = getBufValue(commBuf, dataLength, dataPostion);
					this.commData.PutValue("Amount", value);
					dataPostion += dataLength;
				} else if (tag.trim().equals("9F03")) {
					value = getBufValue(commBuf, dataLength, dataPostion);
					this.commData.PutValue("MerchantName", value.trim());
					dataPostion += dataLength;
				} else if (tag.trim().equals("9F04")) {
					value = getBufValue(commBuf, dataLength, dataPostion);
					this.commData.PutValue("MerchantNumber", value);
					dataPostion += dataLength;
				} else if (tag.trim().equals("9F05")) {
					value = getBufValue(commBuf, dataLength, dataPostion);
					this.commData.PutValue("TerminalNumber", value);
					dataPostion += dataLength;
				} else if (tag.trim().equals("9F06")) {
					value = getBufValue(commBuf, dataLength, dataPostion);
					this.commData.PutValue("Operator", value);
					dataPostion += dataLength;
				} else if (tag.trim().equals("9F07")) {
					value = getBufValue(commBuf, dataLength, dataPostion);
					this.commData.PutValue("AcqNumber", value);
					dataPostion += dataLength;
				} else if (tag.trim().equals("9F08")) {
					value = getBufValue(commBuf, dataLength, dataPostion);
					this.commData.PutValue("IssNumber", value);
					dataPostion += dataLength;
				} else if (tag.trim().equals("9F09")) {
					value = getBufValue(commBuf, dataLength, dataPostion);
					this.commData.PutValue("IssName", value);
					dataPostion += dataLength;
				} else if (tag.trim().equals("9F0A")) {
					value = getBufValue(commBuf, dataLength, dataPostion);
					this.commData.PutValue("PoscNumber", value);
					dataPostion += dataLength;
				} else if (tag.trim().equals("9F0B")) {
					value = getBufValue(commBuf, dataLength, dataPostion);
					this.commData.PutValue("CardNumber", value);
					dataPostion += dataLength;
				} else if (tag.trim().equals("9F0C")) {
					value = getBufValue(commBuf, dataLength, dataPostion);
					this.commData.PutValue("SwipeType", value);
					dataPostion += dataLength;
				} else if (tag.trim().equals("9F0D")) {
					value = getBufValue(commBuf, dataLength, dataPostion);
					this.commData.PutValue("BatchNumber", value);
					dataPostion += dataLength;
				} else if (tag.trim().equals("9F0E")) {
					value = getBufValue(commBuf, dataLength, dataPostion);
					this.commData.PutValue("PosTraceNumber", value);
					dataPostion += dataLength;
				} else if (tag.trim().equals("9F0F")) {
					value = getBufValue(commBuf, dataLength, dataPostion);
					this.commData.PutValue("AuthNumber", value);
					dataPostion += dataLength;
				} else if (tag.trim().equals("9F10")) {
					value = getBufValue(commBuf, dataLength, dataPostion);
					this.commData.PutValue("HostSerialNumber", value);
					dataPostion += dataLength;
				} else if (tag.trim().equals("9F11")) {
					value = getBufValue(commBuf, dataLength, dataPostion);
					this.commData.PutValue("TransDate", value);
					dataPostion += dataLength;
				} else if (tag.trim().equals("9F12")) {
					value = getBufValue(commBuf, dataLength, dataPostion);
					this.commData.PutValue("TransTime", value);
					dataPostion += dataLength;
				} else if (tag.trim().equals("9F13")) {
					value = getBufValue(commBuf, dataLength, dataPostion);
					this.commData.PutValue("Memo", value);
					dataPostion += dataLength;
				} else if (tag.trim().equals("9F14")) {
					value = getBufValue(commBuf, dataLength, dataPostion);
					this.commData.PutValue("RejCode", value);
					dataPostion += dataLength;
				} else if (tag.trim().equals("9F15")) {
					byte[] valueBuf = ArrayUtil.copy(commBuf, dataPostion,
							dataLength);

					this.commData.setContentTo(valueBuf);

					dataPostion += dataLength;
				} else if (tag.trim().equals("9F16")) {
					value = getBufValue(commBuf, dataLength, dataPostion);
					this.commData.PutValue("IpAddress", value);
					dataPostion += dataLength;
				} else if (tag.trim().equals("9F17")) {
					value = getBufValue(commBuf, dataLength, dataPostion);
					this.commData.PutValue("PortNumber", value);
					dataPostion += dataLength;
				} else if (tag.trim().equals("9F18")) {
					value = getBufValue(commBuf, dataLength, dataPostion);
					this.commData.PutValue("TimeOut", value);
					dataPostion += dataLength;
				} else if (tag.trim().equals("9F19")) {
					value = getBufValue(commBuf, dataLength, dataPostion);
					this.commData.PutValue("RejCodeExplain", value);
					dataPostion += dataLength;
				} else if (tag.trim().equals("9F1A")) {
					value = getBufValue(commBuf, dataLength, dataPostion);
					this.commData.PutValue("TransCheck", value);
					dataPostion += dataLength;
				} else if (tag.trim().equals("9F1B")) {
					value = getBufValue(commBuf, dataLength, dataPostion);
					this.commData.PutValue("ExpireDate", value);
					dataPostion += dataLength;
				} else if (tag.trim().equals("9F1C")) {
					value = getBufValue(commBuf, dataLength, dataPostion);
					this.commData.PutValue("Tips", value);
					dataPostion += dataLength;
				} else if (tag.trim().equals("9F1D")) {
					value = getBufValue(commBuf, dataLength, dataPostion);
					this.commData.PutValue("Total", value);
					dataPostion += dataLength;
				} else if (tag.trim().equals("9F1E")) {
					value = getBufValue(commBuf, dataLength, dataPostion);
					this.commData.PutValue("StoreNumber", value);
					dataPostion += dataLength;
				} else if (tag.trim().equals("9F1F")) {
					value = getBufValue(commBuf, dataLength, dataPostion);
					this.commData.PutValue("PosNumber", value);
					dataPostion += dataLength;
				} else if (tag.trim().equals("9F20")) {
					value = getBufValue(commBuf, dataLength, dataPostion);
					this.commData.PutValue("OldTraceNumber", value);
					dataPostion += dataLength;
				} else if (tag.trim().equals("9F23")) {
					value = getBufValue(commBuf, dataLength, dataPostion);
					this.commData.PutValue("BalanceAmount", value);
					dataPostion += dataLength;
				} else if (tag.trim().equals("9F24")) {
					value = getBufValue(commBuf, dataLength, dataPostion);
					this.commData.PutValue("TVR", value);
					dataPostion += dataLength;
				} else if (tag.trim().equals("9F25")) {
					value = getBufValue(commBuf, dataLength, dataPostion);
					this.commData.PutValue("TSI", value);
					dataPostion += dataLength;
				} else if (tag.trim().equals("9F26")) {
					value = getBufValue(commBuf, dataLength, dataPostion);
					this.commData.PutValue("AID", value);
					dataPostion += dataLength;
				} else if (tag.trim().equals("9F27")) {
					value = getBufValue(commBuf, dataLength, dataPostion);
					this.commData.PutValue("ATC", value);
					dataPostion += dataLength;
				} else if (tag.trim().equals("9F28")) {
					value = getBufValue(commBuf, dataLength, dataPostion);
					this.commData.PutValue("AppLabelName", value);
					dataPostion += dataLength;
				} else if (tag.trim().equals("9F29")) {
					value = getBufValue(commBuf, dataLength, dataPostion);
					this.commData.PutValue("PreferredName", value);
					dataPostion += dataLength;
				} else if (tag.trim().equals("9F2A")) {
					value = getBufValue(commBuf, dataLength, dataPostion);
					this.commData.PutValue("TC", value);
					dataPostion += dataLength;
				} else if (tag.trim().equals("9FFF")) {
					value = getBufValue(commBuf, dataLength, dataPostion);
					this.commData.PutValue("orderNumber", value);
					dataPostion += dataLength;
				} //缴费单号
				else if (tag.trim().equals("9F91")) {
					value = getBufValue(commBuf, dataLength, dataPostion);
					this.commData.PutValue("JFDH", value);
					dataPostion += dataLength;
				}
				//缴费科目
				else if (tag.trim().equals("9F92")) {
					value = getBufValue(commBuf, dataLength, dataPostion);
					this.commData.PutValue("JFKM", value);
					dataPostion += dataLength;
				}
				//客户姓名
				else if (tag.trim().equals("9F93")) {
					value = getBufValue(commBuf, dataLength, dataPostion);
					this.commData.PutValue("KHXM", value);
					dataPostion += dataLength;
				}
				else if (tag.trim().equals("9F95")) {
					value = getBufValue(commBuf, dataLength, dataPostion);
					this.commData.PutValue("YWBH", value);
					dataPostion += dataLength;
				}
				//身份证号
				else if (tag.trim().equals("9F94")) {
					value = getBufValue(commBuf, dataLength, dataPostion);
					this.commData.PutValue("SFZH", value);
					dataPostion += dataLength;
				}else if (tag.trim().equals("9F52")) {
					value = getBufValue(commBuf, dataLength, dataPostion);
					this.commData.PutValue("m_PrintContent", value);
					dataPostion += dataLength;
				} else {
					dataPostion += dataLength;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -109L;
		}
		//byte[] commBuf;
		return 0L;
	}

	private String getBufValue(byte[] commBuf, int dataLength, int dataPostion) {
		String value = "";
		try {
			byte[] valueBuf = ArrayUtil.copy(commBuf, dataPostion, dataLength);
			value = new String(valueBuf, "GBK");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value.trim();
	}

	public CommData getCommData() {
		return this.commData;
	}

	private long getSendCommBuffer(CommData sendCommData) {
		byte[] dataBuf = new byte[1024];
		try {
			this.postion = 0;

			addCommDataBuf(dataBuf, "9F00", sendCommData.GetValue("appname"),
					Integer.valueOf(this.postion));

			addCommDataBuf(dataBuf, "9F01", Integer.valueOf(Integer
					.parseInt(sendCommData.GetValue("TransType"))),
					Integer.valueOf(this.postion));
			if (equqlsTransType(
					new String[] { String.valueOf(2L), String.valueOf(3L),
							String.valueOf(23L), String.valueOf(24L),
							String.valueOf(25L), String.valueOf(4L),
							String.valueOf(5L), String.valueOf(6L),
							String.valueOf(14L), String.valueOf(7L),
							String.valueOf(8L), String.valueOf(9L),
							String.valueOf(10L), String.valueOf(11L),
							String.valueOf(12L), String.valueOf(13L),
							String.valueOf(16L), String.valueOf(22L) },
					sendCommData.GetValue("TransType"))) {
				if (!sendCommData.GetValue("Amount").equals("")) {
					addCommDataBuf(dataBuf, "9F02",
							sendCommData.GetValue("Amount"),
							Integer.valueOf(this.postion));
				}
				if (!sendCommData.GetValue("Operator").trim().equals("")) {
					addCommDataBuf(dataBuf, "9F06",
							sendCommData.GetValue("Operator"),
							Integer.valueOf(this.postion));
				}
				if (!sendCommData.GetValue("AuthNumber").trim().equals("")) {
					addCommDataBuf(dataBuf, "9F0F",
							sendCommData.GetValue("AuthNumber"),
							Integer.valueOf(this.postion));
				}
				if (!sendCommData.GetValue("HostserialNumber").trim()
						.equals("")) {
					addCommDataBuf(dataBuf, "9F10",
							sendCommData.GetValue("HostserialNumber"),
							Integer.valueOf(this.postion));
				}
				if (!sendCommData.GetValue("TransDate").trim().equals("")) {
					addCommDataBuf(dataBuf, "9F11",
							sendCommData.GetValue("TransDate"),
							Integer.valueOf(this.postion));
				}
				if (!sendCommData.GetValue("Memo").trim().equals("")) {
					addCommDataBuf(dataBuf, "9F13",
							sendCommData.GetValue("Memo"),
							Integer.valueOf(this.postion));
				}
				//缴费单号
				if (!sendCommData.GetValue("JFDH").trim().equals("")) {
					addCommDataBuf(dataBuf, "9F91",
							sendCommData.GetValue("JFDH"),
							Integer.valueOf(this.postion));
				}
				
				if (!sendCommData.GetValue("ExpireDate").trim().equals("")) {
					addCommDataBuf(dataBuf, "9F1B",
							sendCommData.GetValue("ExpireDate"),
							Integer.valueOf(this.postion));
				}
				if (!sendCommData.GetValue("StoreNumber").trim().equals("")) {
					addCommDataBuf(dataBuf, "9F1E",
							sendCommData.GetValue("StoreNumber"),
							Integer.valueOf(this.postion));
				}
				if (!sendCommData.GetValue("PosNumber").trim().equals("")) {
					addCommDataBuf(dataBuf, "9F1F",
							sendCommData.GetValue("PosNumber"),
							Integer.valueOf(this.postion));
				}
				if (!sendCommData.GetValue("OldTraceNumber").trim().equals("")) {
					addCommDataBuf(dataBuf, "9F20",
							sendCommData.GetValue("OldTraceNumber"),
							Integer.valueOf(this.postion));
				}
				if (!sendCommData.GetValue("orderNumber").trim().equals("")) {
					addCommDataBuf(dataBuf,
							CacheUtil.BANKCARD_TAG_ORDERNUMBER[0],
							sendCommData.GetValue("orderNumber"),
							Integer.valueOf(this.postion));
				}
			}
			if (equqlsTransType(new String[] { String.valueOf(17L) },
					sendCommData.GetValue("TransType"))) {
				if (!sendCommData.GetValue("RejCode").trim().equals("")) {
					addCommDataBuf(dataBuf, "9F14",
							sendCommData.GetValue("RejCode"),
							Integer.valueOf(this.postion));
				}
				if (sendCommData.getContentTo().length > 0) {
					addCommDataBuf(dataBuf, "9F15",
							sendCommData.getContentTo(),
							Integer.valueOf(this.postion));
				}
			}
			if (!sendCommData.GetValue("TransCheck").trim().equals("")) {
				addCommDataBuf(dataBuf, "9F1A",
						sendCommData.GetValue("TransCheck"),
						Integer.valueOf(this.postion));
			}
			int bufferLength = this.postion;

			byte[] commBuf = new byte[bufferLength + 5];

			commBuf[0] = 2;
			commBuf[(bufferLength + 3)] = 3;

			int dataLength = Integer.parseInt(String.valueOf(this.postion), 16);
			commBuf[1] = ((byte) (dataLength / 256));
			commBuf[2] = ((byte) (dataLength % 256));
			for (int i = 3; i < commBuf.length - 2; i++) {
				commBuf[i] = dataBuf[(i - 3)];
			}
			commBuf[(bufferLength + 4)] = FileUtil.Xor(commBuf, commBuf.length);
			sendCommData.setSendCommBuf(commBuf);
		} catch (Exception e) {
			e.printStackTrace();
			return -102L;
		}
		return 0L;
	}

	private int getTagValueLength(byte[] bcdbuf) {
		if ((bcdbuf[0] < 0) || (bcdbuf[1] < 0)) {
			String a = HexUtils.byte2hex(bcdbuf);
			StringBuffer buffer = new StringBuffer(a);
			for (int i = 0; i < buffer.length(); i++) {
				if (buffer.charAt(i) == '0') {
					buffer.deleteCharAt(i);
				} else {
					a = buffer.toString();
					break;
				}
			}
			int b = Integer.parseInt(a, 16);
			return b;
		}
		if (bcdbuf[0] > 0) {
			int b = bcdbuf[0] * 256
					+ Integer.parseInt(String.valueOf(bcdbuf[1]), 10);
			return b;
		}
		int b = bcdbuf[0] * 100 + bcdbuf[1];
		return b;
	}

	
	
	public long sendAndReceive(ReqData request, RspData response,
			int timeout, CommData revCommData) {
		logger.debug("sendAndReceive!");
		StatusPanel.labelStatusDetail.setText("等待读取串口数据");
		CreateCommData(request);
		if (revCommData != null) {
			this.commData.setContentTo(revCommData.getContentTo());
			this.commData.PutValue("TransType",
					revCommData.GetValue("TransType"));
			this.commData.PutValue("RejCode", revCommData.GetValue("RejCode"));
		}
		this.result = getSendCommBuffer(this.commData);
		if (this.result != 0L) {
			return this.result;
		}
		String comport = PropUtil.getAppPort(request.GetValue("mchNO"));
		SerialDataService frmComDataService = new SerialDataService(comport,timeout,
				this.commData);
		this.result = frmComDataService.showMe();
		if (this.result != 0L) {
			return this.result;
		}
		this.commData.setBackComData(frmComDataService.getdataBack());
		this.result = getBackData(this.commData.getBackComData());

		return this.result;
	}

	public void setCommData(CommData commData) {
		this.commData = commData;
	}
	
}
