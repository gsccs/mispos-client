package com.allinpay.mis.aip.service;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.allinpay.mis.aip.comm.CommData;
import com.allinpay.mis.client.ui.panel.StatusPanel;
import com.allinpay.mis.tools.ArrayUtil;
import com.allinpay.mis.tools.FileUtil;

/**
 * 网络通讯窗口
 * 
 * @author x.d zhang
 *
 */
public class SocketDataService {
	
	private static final long serialVersionUID = -5472352789997308938L;
	private String ipAddress;
	private Integer portNum;
	private Integer timeout;
	private Socket socket;
	private OutputStream outputStream;
	private InputStream inputStream;
	private byte[] sendSocketBuf = new byte[0];
	private byte[] backSocketBuf = new byte[0];
	private byte[] readbuf;
	private Integer back_len;
	private long result;
	ScheduledExecutorService service = Executors
			.newSingleThreadScheduledExecutor();
	CountDownLatch countDownLatch = new CountDownLatch(1);

	public byte[] getBackSocketBuf() {
		return this.backSocketBuf;
	}

	public SocketDataService(CommData commData) {
		this.ipAddress = commData.GetValue("IpAddress");
		this.portNum = Integer.valueOf(commData.GetValue("PortNumber"));
		this.sendSocketBuf = commData.getContentTo();
		this.timeout = Integer.valueOf(commData.GetValue("TimeOut"));
	}

	

	public long showMe() {
		FileUtil.writePacket("FrmSocketDataService showMe!");
		this.result = -113L;
		StatusPanel.labelStatusDetail.setText("正在连接主机，请稍后……");
		StatusPanel.labelStatus.setText(Integer.toString(this.timeout.intValue()));

		SocketThread st = new SocketThread();
		st.start();

		ScheduledFuture<?> future = this.service.scheduleWithFixedDelay(
				new TimerTask(), 50L, 1000L, TimeUnit.MILLISECONDS);
		try {
			this.countDownLatch.await();
			future.cancel(true);
			this.service.shutdown();
			System.out.println("线程结束,关闭串口连接");
			hideMe();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return this.result;
	}

	public void hideMe() {
		try {
			if (this.inputStream != null) {
				this.inputStream.close();
				this.outputStream.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	ActionListener AL_socket = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			SocketDataService tmp17_14 = SocketDataService.this;
			tmp17_14.timeout = Integer.valueOf(tmp17_14.timeout.intValue() - 1);
			StatusPanel.labelStatus.setText(Integer.toString(SocketDataService.this.timeout.intValue()));
			label538: try {
				if ((SocketDataService.this.socket == null)
						|| (!SocketDataService.this.socket.isConnected())) {
					StatusPanel.labelStatusDetail.setText("正在与主机建立连接，请稍候……");
					
					return;
				}
				StatusPanel.labelStatusDetail.setText("正在向主机发送数据，等待主机返回数据……");
				if (((SocketDataService.this.inputStream != null ? 1 : 0) & (SocketDataService.this.inputStream
						.available() > 2 ? 1 : 0)) != 0) {
					byte[] data_len = new byte[2];
					SocketDataService.this.inputStream.read(data_len, 0, 2);
					SocketDataService.this.back_len = Integer
							.valueOf((data_len[0] & 0xFF) * 256
									+ (data_len[1] & 0xFF));
					SocketDataService.this.backSocketBuf = ArrayUtil.join(
							SocketDataService.this.backSocketBuf, data_len);

					SocketDataService.this.readbuf = new byte[SocketDataService.this.back_len
							.intValue()];
					int nIdx = 0;

					int nReadLen = 0;
					while (nIdx < SocketDataService.this.back_len.intValue()) {
						nReadLen = SocketDataService.this.inputStream.read(
								SocketDataService.this.readbuf, nIdx,
								SocketDataService.this.back_len.intValue()
										- nIdx);
						if (nReadLen <= 0) {
							break;
						}
						nIdx += nReadLen;
					}
					SocketDataService.this.backSocketBuf = ArrayUtil.join(
							SocketDataService.this.backSocketBuf,
							SocketDataService.this.readbuf);
					if (SocketDataService.this.backSocketBuf.length < SocketDataService.this.back_len
							.intValue()) {
						break label538;
					}
					FileUtil.writePacket("SOCKET_RECEIVE",
							SocketDataService.this.backSocketBuf,
							SocketDataService.this.backSocketBuf.length);
					SocketDataService.this.result = 0L;
					SocketDataService.this.hideMe();
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			} finally {
				if (SocketDataService.this.timeout.intValue() == 0) {
					SocketDataService.this.result = -114L;
					SocketDataService.this.hideMe();
				}
				if (SocketDataService.this.result == -115L) {
					SocketDataService.this.hideMe();
				}
			}
		}
	};

	class SocketThread extends Thread {
		SocketThread() {
		}

		public void run() {
			try {
				SocketDataService.this.socket = new Socket(
						SocketDataService.this.ipAddress,
						SocketDataService.this.portNum.intValue());
				SocketDataService.this.outputStream = SocketDataService.this.socket
						.getOutputStream();
				SocketDataService.this.inputStream = SocketDataService.this.socket
						.getInputStream();
				System.out.println("MIS-POSP");
				FileUtil.writePacket("SOCKET_SEND",
						SocketDataService.this.sendSocketBuf,
						SocketDataService.this.sendSocketBuf.length);
				SocketDataService.this.outputStream
						.write(SocketDataService.this.sendSocketBuf);
				SocketDataService.this.outputStream.flush();
			} catch (ConnectException e) {
				System.out.println("连接超时");
				SocketDataService.this.result = -115L;
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private class TimerTask implements Runnable {
		private TimerTask() {
		}

		public void run() {
			//setVisible(true);
			//System.out.println(FrmSocketDataService.this.timeout);
			System.out.println("POSP-MIS");
			SocketDataService tmp17_14 = SocketDataService.this;
			tmp17_14.timeout = Integer.valueOf(tmp17_14.timeout.intValue() - 1);
			StatusPanel.labelStatus.setText(Integer.toString(SocketDataService.this.timeout.intValue()));
			label555: try {
				if ((SocketDataService.this.socket == null)
						|| (!SocketDataService.this.socket.isConnected())) {
					StatusPanel.labelStatusDetail.setText("正在与主机建立连接，请稍候……");
					return;
				}
				StatusPanel.labelStatusDetail.setText("正在向主机发送数据，等待主机返回数据……");
				if (((SocketDataService.this.inputStream != null ? 1 : 0) & (SocketDataService.this.inputStream
						.available() > 2 ? 1 : 0)) != 0) {
					byte[] data_len = new byte[2];
					SocketDataService.this.inputStream.read(data_len, 0, 2);
					SocketDataService.this.back_len = Integer
							.valueOf((data_len[0] & 0xFF) * 256
									+ (data_len[1] & 0xFF));
					SocketDataService.this.backSocketBuf = ArrayUtil.join(
							SocketDataService.this.backSocketBuf, data_len);

					SocketDataService.this.readbuf = new byte[SocketDataService.this.back_len
							.intValue()];
					int nIdx = 0;

					int nReadLen = 0;
					while (nIdx < SocketDataService.this.back_len.intValue()) {
						nReadLen = SocketDataService.this.inputStream.read(
								SocketDataService.this.readbuf, nIdx,
								SocketDataService.this.back_len.intValue()
										- nIdx);
						if (nReadLen <= 0) {
							break;
						}
						nIdx += nReadLen;
					}
					SocketDataService.this.backSocketBuf = ArrayUtil.join(
							SocketDataService.this.backSocketBuf,
							SocketDataService.this.readbuf);
					if (SocketDataService.this.backSocketBuf.length < SocketDataService.this.back_len
							.intValue()) {
						break label555;
					}
					FileUtil.writePacket("SOCKET_RECEIVE",
							SocketDataService.this.backSocketBuf,
							SocketDataService.this.backSocketBuf.length);
					SocketDataService.this.result = 0L;
					SocketDataService.this.countDownLatch.countDown();
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			} finally {
				if (SocketDataService.this.timeout.intValue() == 0) {
					SocketDataService.this.result = -114L;
					SocketDataService.this.countDownLatch.countDown();
				}
				if (SocketDataService.this.result == -115L) {
					SocketDataService.this.countDownLatch.countDown();
				}
			}
		}
	}
}
