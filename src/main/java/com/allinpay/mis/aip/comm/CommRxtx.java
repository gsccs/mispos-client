package com.allinpay.mis.aip.comm;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

import com.allinpay.mis.tools.ArrayUtil;
import com.allinpay.mis.tools.HexUtils;

/**
 * 
 * @author x.d zhang
 *
 */
public class CommRxtx implements SerialPortEventListener {
	
	static CommPortIdentifier portId;
	static Enumeration<CommPortIdentifier> portList;
	InputStream inputStream;
	OutputStream outputStream;
	SerialPort serialPort;

	public static void main(String[] args) {
		try {
			CommRxtx commRxtx = new CommRxtx();
			commRxtx.init("com1");
			commRxtx.writeData(new byte[] { 1, 2, 3 });
			commRxtx.closeSerialPort();
			commRxtx.init("com1");
			commRxtx.writeData(new byte[] { 1, 2, 3 });
			commRxtx.closeSerialPort();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	byte[] readBuffer = new byte[2048];
	byte[] commBuffer = new byte[0];
	int targetLength = 0;

	public long closeSerialPort() {
		try {
			this.inputStream.close();
			this.outputStream.close();
			this.serialPort.close();
			Thread.sleep(100L);
		} catch (Exception e) {
			return -108L;
		}
		return 0l;
	}

	public byte[] getCommbuffer() {
		return ArrayUtil.getRealByte(this.commBuffer, this.targetLength);
	}

	public long init(String comport){
		boolean commIsExist = false;
		try {
			CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier(comport);
			if (portId.isCurrentlyOwned()) {
				System.out.println("Error: Port is currently in use");
				return -107L;
			}
			CommPort commPort = portId.open(getClass().getName(), 2000);
			if ((commPort instanceof SerialPort)) {
				commIsExist = true;
				this.serialPort = ((SerialPort) commPort);
				this.serialPort.setSerialPortParams(9600, 8, 1, 0);
				this.inputStream = this.serialPort.getInputStream();
				this.outputStream = this.serialPort.getOutputStream();
				this.serialPort.addEventListener(this);
				this.serialPort.notifyOnDataAvailable(true);
				this.serialPort.notifyOnOutputEmpty(true);
			} else {
				System.out
						.println("Error: Only serial ports are handled by this example.");
			}
		} catch (Exception e1) {
			//e1.printStackTrace();
			return -108L;
		}
		if (!commIsExist) {
			return -106L;
		}
		return 0L;
	}

	public void serialEvent(SerialPortEvent event) {
		try {
			Thread.sleep(500L);
		} catch (Exception e) {
			e.printStackTrace();
		}
		switch (event.getEventType()) {
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
		case 7:
		case 8:
		case 9:
		case 10:
			break;
		case 1:
			int initFlag = 0;
			int packetLength = 0;
			int bufPosition = 0;
			this.commBuffer = new byte[4096];
			int available = 0;
			try {
				available = this.inputStream.available();
			} catch (IOException e1) {
				System.out.println("wangerma444");
				closeSerialPort();
				e1.printStackTrace();
			}
			try {
				if ((this.inputStream != null) && (available > 0)) {
					int len = 0;
					while ((len = this.inputStream.read(this.readBuffer)) > 0) {
						System.out.println("本次读取缓冲区长度" + len);
						for (int i = 0; i < len; i++) {
							this.commBuffer[bufPosition] = this.readBuffer[i];
							bufPosition++;
						}
						if ((initFlag == 0) && (bufPosition >= 2)
								&& (this.commBuffer[0] == 2)) {
							packetLength = HexUtils.toHexInteger(
									this.commBuffer[1]).intValue()
									* 100
									+ HexUtils.toHexInteger(this.commBuffer[2])
											.intValue() + 5;
							initFlag = -1;
						}
						if (bufPosition >= packetLength) {
							this.targetLength = packetLength;
							break;
						}
					}
				}
			} catch (Exception e) {
				closeSerialPort();
				e.printStackTrace();
			}
		}
	}

	public long writeData(byte[] outdata) {
		try {
			this.outputStream.write(outdata);
			this.outputStream.flush();
			return 0L;
		} catch (IOException e) {
			closeSerialPort();
		}
		return -118L;
	}

	public long writeData(byte[] outdata, int dataoffset, int outlen) {
		try {
			this.outputStream.write(outdata, dataoffset, outlen);
			this.outputStream.flush();
			return 0L;
		} catch (IOException e) {
			closeSerialPort();
		}
		return -118L;
	}
}
