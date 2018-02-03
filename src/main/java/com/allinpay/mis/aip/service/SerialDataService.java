package com.allinpay.mis.aip.service;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.allinpay.mis.aip.comm.CommData;
import com.allinpay.mis.aip.comm.CommRxtx;
import com.allinpay.mis.client.ui.panel.StatusPanel;
import com.allinpay.mis.tools.FileUtil;
import com.allinpay.mis.tools.HexUtils;

/**
 * 串口通讯窗口
 * 
 * @author x.d zhang
 *
 */
public class SerialDataService{
	
	private static final long serialVersionUID = 3672710712281566803L;
	private long result;
	private int timeOut;
	private String comport;
	private CommRxtx commRxtx;
	private CommData commData;
	private byte[] dataBack;
	private final int waitTime = 500;
	ScheduledFuture<?> future;
	ScheduledExecutorService service = Executors
			.newSingleThreadScheduledExecutor();
	CountDownLatch countDownLatch = new CountDownLatch(1);

	public byte[] getdataBack() {
		return this.dataBack;
	}


	
	public SerialDataService(String comport,int time, CommData commData) {
		this.comport = comport;
		this.commData = commData;
		this.timeOut = (time * 1000);
	}

	public long showMe() {
		this.commRxtx = new CommRxtx();
		this.result = this.commRxtx.init(comport);
		if (this.result != 0L) {
			hideMe();
			return this.result;
		}
		FileUtil.writePacket("COM_SEND", this.commData.getSendCommBuf(),
				this.commData.getSendCommBuf().length);
		this.result = this.commRxtx.writeData(this.commData.getSendCommBuf());
		if (this.result != 0L) {
			hideMe();
			return this.result;
		}
		this.future = this.service.scheduleWithFixedDelay(new TimerTask(), 50L,
				500L, TimeUnit.MILLISECONDS);
		try {
			this.countDownLatch.await();
			this.future.cancel(true);
			this.service.shutdown();
			hideMe();
		} catch (InterruptedException e) {
			hideMe();
			e.printStackTrace();
		}
		return this.result;
	}

	public void hideMe() {
		FileUtil.writePacket("FrmComDataService hideMe!");
		if(null!=commRxtx){
			this.commRxtx.closeSerialPort();
			this.commRxtx = null;
		}
	}

	private class TimerTask implements Runnable {
		
		private TimerTask() {
		
		}

		public void run() {
			//setVisible(true);
			
			if (SerialDataService.this.timeOut > 0) {
				SerialDataService.this.timeOut -= waitTime;
				if (SerialDataService.this.timeOut % 1000 == 0) {
					//FrmComDataService.this.setVisible(true);
					//lblTitle.setText("等待读取串口数据:"+ FrmComDataService.this.timeOut / 1000);
					StatusPanel.labelStatusDetail.setText("等待读取串口数据:"+ SerialDataService.this.timeOut / 1000);
					//System.out.println("等待读取串口数据:"+ FrmComDataService.this.timeOut / 1000);
				}
				byte[] data = SerialDataService.this.commRxtx.getCommbuffer();
				if ((data.length != 0) && (data[0] == 2)) {
					int len = HexUtils.toHexInteger(data[1]).intValue() * 100
							+ HexUtils.toHexInteger(data[2]).intValue() + 5;
					if (len == data.length) {
						SerialDataService.this.dataBack = data;
						SerialDataService.this.result = 0L;
						System.out.println("线程计数器:"+ SerialDataService.this.countDownLatch.getCount());
						FileUtil.writePacket("serial data read succeed!");
						SerialDataService.this.countDownLatch.countDown();
					} else if (SerialDataService.this.timeOut <= 0) {
						System.out.println("接收队列接收到数据长度" + data.length+ " 目标长度是" + (len + 5));
						System.out.println("60s内接收数据不完整");
					}
				}
			} else {
				SerialDataService.this.result = -105L;
				SerialDataService.this.countDownLatch.countDown();
				return;
			}
		}
	}
}
