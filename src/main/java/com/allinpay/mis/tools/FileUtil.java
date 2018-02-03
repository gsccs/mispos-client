package com.allinpay.mis.tools;

import gnu.io.CommPortIdentifier;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import com.allinpay.mis.aip.model.RspData;

public class FileUtil {

	public static String getAbsolutePath() {
		File file = new File("");
		String home = file.getAbsolutePath();
		if (home.indexOf(":") == -1) {
			home = "/" + home;
		}
		return home;
	}
	
	
	public static String getRelativeFilePath(String fileName) {
		File file = new File("");
		String home = file.getAbsolutePath();
		if (home.indexOf(":") == -1) {
			home = "/" + home;
		}
		return home;
	}


	public static List<String> getComList() {
		List<String> list = new ArrayList();
		Enumeration<?> en = CommPortIdentifier.getPortIdentifiers();
		while (en.hasMoreElements()) {
			CommPortIdentifier portIdentifier = (CommPortIdentifier) en
					.nextElement();
			if (portIdentifier.getPortType() == 1) {
				list.add(portIdentifier.getName());
			}
		}
		return list;
	}

	public static byte[] AscToBcd(byte[] source, byte[] dest, int length) {
		byte j = 0;
		byte k = 0;
		for (byte i = 0; i < length - 1; i = (byte) (i + 1)) {
			if ((source[i] >= toAsc("0")) && (source[i] <= toAsc("9"))) {
				j = (byte) (source[i] - toAsc("0"));
			} else if ((source[i] >= toAsc("a")) && (source[i] <= toAsc("f"))) {
				j = (byte) (source[i] - toAsc("a") + 10);
			} else if ((source[i] >= toAsc("A")) && (source[i] <= toAsc("F"))) {
				j = (byte) (source[i] - toAsc("A") + 10);
			}
			i = (byte) (i + 1);
			if ((source[i] >= toAsc("0")) && (source[i] <= toAsc("9"))) {
				k = (byte) (source[i] - toAsc("0"));
			} else if ((source[i] >= toAsc("a")) && (source[i] <= toAsc("f"))) {
				k = (byte) (source[i] - toAsc("a") + 10);
			} else if ((source[i] >= toAsc("A")) && (source[i] <= toAsc("F"))) {
				k = (byte) (source[i] - toAsc("A") + 10);
			}
			dest[(i / 2)] = ((byte) (mult_x(2 * j, 4) + k));
		}
		return dest;
	}

	public static int[] AscToBcd(int[] source, int[] dest, int length) {
		byte j = 0;
		byte k = 0;
		for (byte i = 0; i < length - 1; i = (byte) (i + 1)) {
			if ((source[i] >= toAsc("0")) && (source[i] <= toAsc("9"))) {
				j = (byte) (source[i] - toAsc("0"));
			} else if ((source[i] >= toAsc("a")) && (source[i] <= toAsc("f"))) {
				j = (byte) (source[i] - toAsc("a") + 10);
			} else if ((source[i] >= toAsc("A")) && (source[i] <= toAsc("F"))) {
				j = (byte) (source[i] - toAsc("A") + 10);
			}
			i = (byte) (i + 1);
			if ((source[i] >= toAsc("0")) && (source[i] <= toAsc("9"))) {
				k = (byte) (source[i] - toAsc("0"));
			} else if ((source[i] >= toAsc("a")) && (source[i] <= toAsc("f"))) {
				k = (byte) (source[i] - toAsc("a") + 10);
			} else if ((source[i] >= toAsc("A")) && (source[i] <= toAsc("F"))) {
				k = (byte) (source[i] - toAsc("A") + 10);
			}
			dest[(i / 2)] = (mult_x(2 * j, 4) + k);
		}
		return dest;
	}

	public static byte[] BcdToAsc(byte[] source, int length) {
		byte[] dest = new byte[length];
		for (int i = 0; i < length / 2; i++) {
			dest[(i * 2 + 0)] = ((byte) (source[i] / 16));
			dest[(i * 2 + 1)] = ((byte) (source[i] - dest[(i * 2 + 0)] * 16));
		}
		for (int i = 0; i < dest.length; i++) {
			if ((dest[i] >= 0) && (dest[i] <= 9)) {
				dest[i] = ((byte) (dest[i] + toAsc("0")));
			} else if ((dest[i] >= 10) && (dest[i] <= 15)) {
				dest[i] = ((byte) (dest[i] - 10 + toAsc("A")));
			}
		}
		return dest;
	}

	public static int[] BcdToAsc(int[] source, int length) {
		int[] dest = new int[length];
		for (int i = 0; i < length / 2; i++) {
			dest[(i * 2 + 0)] = (source[i] / 16);
			dest[(i * 2 + 1)] = (source[i] - dest[(i * 2 + 0)] * 16);
		}
		for (int i = 0; i < dest.length; i++) {
			if ((dest[i] >= 0) && (dest[i] <= 9)) {
				dest[i] += toAsc("0");
			} else if ((dest[i] >= 10) && (dest[i] <= 15)) {
				dest[i] = (dest[i] - 10 + toAsc("A"));
			}
		}
		return dest;
	}

	public static String formatAmt(String amt, int needLen) {
		if (amt.length() == 12) {
			return amt;
		}
		int dotPosi = amt.indexOf(".");
		int amtlen = amt.length();
		if (dotPosi != -1) {
			amt = amt.replace(".", "");
			if (amtlen - dotPosi == 3) {
				amtlen--;
			} else if (amtlen - dotPosi == 2) {
				amt = amt + "0";
			}
		} else {
			amt = amt + "00";
			amtlen += 2;
		}
		while (amtlen < needLen) {
			amt = "0" + amt;
			amtlen++;
		}
		return amt;
	}

	public static String formatDate(String date, String s) {
		if (date.length() == 4) {
			date = "20" + date;
		}
		StringBuffer buf = new StringBuffer(date);
		buf.insert(4, s);
		buf.insert(7, s);
		return buf.toString();
	}

	public static String formatTime(String time, String s) {
		StringBuffer buf = new StringBuffer(time);
		buf.insert(2, s);
		buf.insert(5, s);
		return buf.toString();
	}

	public static String getTransName(long transType) {
		String transNameString = "未知交易";
		switch ((int) transType) {
		case 2:
			transNameString = "消费";
			break;
		case 3:
			transNameString = "撤销";
			break;
		case 4:
			transNameString = "退货";
			break;
		case 6:
			transNameString = "预授权";
			break;
		case 7:
			transNameString = "授权撤销";
			break;
		case 8:
			transNameString = "授权完成";
			break;
		case 11:
			transNameString = "授权完成撤销";
			break;
		case 14:
			transNameString = "结算";
			break;
		}
		return transNameString;
	}

	public static int mult_x(int x, int times) {
		for (int i = 1; i < times; i++) {
			x *= 2;
		}
		return x;
	}

	public static void printSettleFile(boolean isReprint,
			RspData response) {
		File file = new File(CacheUtil.getPrintpath());
		file.getParentFile().mkdirs();
		if (file.exists()) {
			file.delete();
		}
		try {
			RandomAccessFile randomFile = new RandomAccessFile(file, "rw");

			long fileLength = randomFile.length();

			randomFile.seek(fileLength);
			randomFile.write(("======================================" + CR)
					.getBytes());
			randomFile.write(("通联支付  POS结算单" + CR).getBytes());
			randomFile.write(("--------------------------------------" + CR)
					.getBytes());
			randomFile.write(("商户名称(MERCHANT NAME)" + CR).getBytes());
			randomFile.write((response.GetValue("MerchantName") + CR)
					.getBytes());
			randomFile.write(("商户编号(MERCHANT NUMBER)" + CR).getBytes());
			randomFile.write((response.GetValue("MerchantNumber") + CR)
					.getBytes());
			randomFile.write(("终端编号(TERMINAL NUMBER)" + CR).getBytes());
			randomFile.write((response.GetValue("TerminalNumber") + CR)
					.getBytes());
			randomFile.write(("操作员号(OPERATOR)"
					+ response.GetValue("PersonNumber") + CR).getBytes());
			randomFile.write(("日期/时间(DATE/TIME):" + CR).getBytes());
			randomFile
					.write((formatDate(response.GetValue("TransDate"), "/")
							+ "  "
							+ formatTime(response.GetValue("TransTime"), ":") + CR)
							.getBytes());
			randomFile.write((response.GetValue("Memo") + CR).getBytes());
			randomFile.write(("--------------------------------------" + CR)
					.getBytes());
			randomFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void printTransFile(long papers, boolean isReprint,
			RspData response) throws IOException {
		File file = new File(CacheUtil.getPrintpath());
		file.getParentFile().mkdirs();
		if (file.exists()) {
			file.delete();
		}
		for (long i = 0L; i < papers; i += 1L) {
			if (i == 0L) {
				writeTransFile(file, isReprint, response, "第一联   商户联");
			} else if (i == 1L) {
				writeTransFile(file, isReprint, response, "第二联   持卡人联");
			} else {
				writeTransFile(file, isReprint, response, "第三联   银行联");
			}
		}
	}

	public static String StrA(String str, int length) {
		String result = "";
		for (int i = 0; i < length; i++) {
			result = result + str;
		}
		return result;
	}

	public static int toAsc_(String str) {
		char[] chr = str.toCharArray();
		return chr[0];
	}

	public static void writePacket(String title, byte[] packet, int length) {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		File file = new File(getAbsolutePath() + "/logs/LOG_"
				+ format.format(date) + ".txt");
		format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			RandomAccessFile randomFile = new RandomAccessFile(file, "rw");

			long fileLength = randomFile.length();

			randomFile.seek(fileLength);
			randomFile.write((format.format(date) + StrA("*", 2) + title
					+ StrA("*", 23) + CR).getBytes());

			String inputStr = "";
			int i;
			for (i = 0; i < length; i++) {
				inputStr = inputStr + HexUtils.toHex(packet[i]) + " ";
				if ((i + 1) % 16 == 0) {
					if (i / 16 < 10) {
						inputStr = "0" + i / 16 + "| " + inputStr;
					} else {
						inputStr = i / 16 + "| " + inputStr;
					}
					randomFile.write((inputStr + CR).getBytes());
					inputStr = "";
				}
			}
			if (length % 16 > 0) {
				if (i / 16 < 10) {
					inputStr = "0" + i / 16 + "| " + inputStr;
				} else {
					inputStr = i / 16 + "| " + inputStr;
				}
				randomFile.write((inputStr + CR).getBytes());
			}
			randomFile.write((StrA("*", 24) + "结束" + StrA("*", 24) + CR)
					.getBytes());

			randomFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void writeSettleFile(File file, boolean isReprint,
			RspData response) {
		try {
			RandomAccessFile randomFile = new RandomAccessFile(file, "rw");

			long fileLength = randomFile.length();

			randomFile.seek(fileLength);

			randomFile.write(("======================================" + CR)
					.getBytes());
			randomFile.write(("通联支付  POS结算单" + CR).getBytes());
			randomFile.write(("--------------------------------------" + CR)
					.getBytes());
			randomFile.write(("商户名称(MERCHANT NAME)" + CR).getBytes());
			randomFile.write((response.GetValue("MerchantName") + CR)
					.getBytes());
			randomFile.write(("商户编号(MERCHANT NUMBER)" + CR).getBytes());
			randomFile.write((response.GetValue("MerchantNumber") + CR)
					.getBytes());
			randomFile.write(("终端编号(TERMINAL NUMBER)" + CR).getBytes());
			randomFile.write((response.GetValue("TerminalNumber") + CR)
					.getBytes());
			randomFile.write(("操作员号(OPERATOR)"
					+ response.GetValue("PersonNumber") + CR).getBytes());
			randomFile.write(("日期/时间(DATE/TIME):" + CR).getBytes());
			randomFile
					.write((formatDate(response.GetValue("TransDate"), "/")
							+ "  "
							+ formatTime(response.GetValue("TransTime"), ":") + CR)
							.getBytes());
			randomFile.write(("--------------------------------------" + CR)
					.getBytes());
			randomFile.write((response.GetValue("Memo") + CR).getBytes());
			randomFile.write(("--------------------------------------" + CR)
					.getBytes());
			if (isReprint) {
				randomFile.write(("重打印/DUPLICATED" + CR).getBytes());
			}
			randomFile.write(("POS版本号:" + PropUtil.getAppName() + CR)
					.getBytes());
			randomFile.write((CR + CR + CR).getBytes());
			randomFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void writeTransFile(File file, boolean isReprint,
			RspData response, String title) {
		try {
			RandomAccessFile randomFile = new RandomAccessFile(file, "rw");

			long fileLength = randomFile.length();

			randomFile.seek(fileLength);
			randomFile.write((title + CR).getBytes());
			randomFile.write(("======================================" + CR)
					.getBytes());
			randomFile.write(("通联支付  POS签购单" + CR).getBytes());
			randomFile.write(("--------------------------------------" + CR)
					.getBytes());
			randomFile.write(("商户名称(MERCHANT NAME)" + CR).getBytes());
			randomFile.write((response.GetValue("MerchantName") + CR)
					.getBytes());
			randomFile.write(("商户编号(MERCHANT NUMBER)" + CR).getBytes());
			randomFile.write((response.GetValue("MerchantNumber") + CR)
					.getBytes());
			randomFile.write(("终端编号(TERMINAL NUMBER)" + CR).getBytes());
			randomFile.write((response.GetValue("TerminalNumber") + CR)
					.getBytes());
			randomFile
					.write(("操作员号(OPERATOR)" + response.GetValue("Operator") + CR)
							.getBytes());
			randomFile.write(("日期/时间(DATE/TIME):" + CR).getBytes());
			randomFile
					.write((formatDate(response.GetValue("TransDate"), "/")
							+ "  "
							+ formatTime(response.GetValue("TransTime"), ":") + CR)
							.getBytes());
			randomFile.write(("--------------------------------------" + CR)
					.getBytes());
			randomFile.write(("交易类型: "
					+ getTransName(Long.parseLong(response
							.GetValue("TransType"))) + CR).getBytes());
			randomFile.write(("发卡行(ISS):" + response.GetValue("IssName") + CR)
					.getBytes());
			randomFile
					.write(("发卡行号(ISS NO):" + response.GetValue("IssNumber") + CR)
							.getBytes());
			randomFile
					.write(("收单行号(ACQ NO):" + response.GetValue("AcqNumber") + CR)
							.getBytes());
			randomFile.write(("卡号(CARD NUMBER):" + CR).getBytes());
			randomFile.write((response.GetValue("CardNumber") + " "
					+ response.GetValue("SwipeType") + CR).getBytes());
			randomFile.write(("批次号(BATCH NO):"
					+ response.GetValue("BatchNumber") + CR).getBytes());
			randomFile.write(("凭证号(VOUCHER NO):"
					+ response.GetValue("PosTraceNumber") + CR).getBytes());
			randomFile.write(("授权号(AUTH. NO):"
					+ response.GetValue("AuthNumber") + CR).getBytes());
			randomFile.write(("交易参考号(REF. NO):"
					+ response.GetValue("HostSerialNumber") + CR).getBytes());
			randomFile.write(("金额(AMOUNT):" + CR).getBytes());
			if ((response.GetValue("TransType") == String.valueOf(4L))
					|| (response.GetValue("TransType") == String.valueOf(3L))) {
				randomFile.write(("-" + response.GetValue("Amount") + CR)
						.getBytes());
			} else {
				randomFile.write((response.GetValue("Amount") + CR).getBytes());
			}
			randomFile.write(("--------------------------------------" + CR)
					.getBytes());
			randomFile.write(("备注/REFERENCE" + CR).getBytes());
			randomFile.write((response.GetValue("Memo") + CR).getBytes());
			if (!response.GetValue("TVR").trim().equals("")) {
				randomFile.write(("TVR:" + response.GetValue("TVR") + CR)
						.getBytes());
				randomFile.write(("TSI:" + response.GetValue("TVR") + CR)
						.getBytes());
				randomFile.write(("AID:" + response.GetValue("TVR") + CR)
						.getBytes());
				randomFile.write(("ATC:" + response.GetValue("TVR") + CR)
						.getBytes());
				randomFile
						.write(("APP LABEL NAME:" + response.GetValue("TVR") + CR)
								.getBytes());
				randomFile
						.write(("PREFERRED NAME:" + response.GetValue("TVR") + CR)
								.getBytes());
				randomFile.write(("TC:" + response.GetValue("TVR") + CR)
						.getBytes());
			}
			randomFile.write(("--------------------------------------" + CR)
					.getBytes());
			if (isReprint) {
				randomFile.write(("重打印/DUPLICATED" + CR).getBytes());
			}
			randomFile.write(("持卡人签名(SIGNATURE):" + CR).getBytes());
			randomFile.write((CR + CR + CR).getBytes());
			randomFile.write(("--------------------------------------" + CR)
					.getBytes());
			randomFile.write(("本人确认以上交易，同意将其计入本卡账户" + CR).getBytes());
			randomFile.write(("I ACKNOWLEDGE SATISFACTORY RECEIPT OF" + CR)
					.getBytes());
			randomFile.write(("RELATIVE GOODS/SERVICE" + CR).getBytes());
			randomFile.write(("--------------------------------------" + CR)
					.getBytes());
			randomFile.write(("POS版本号:" + PropUtil.getAppName() + CR)
					.getBytes());
			randomFile.write(("--------------------------------------" + CR)
					.getBytes());
			randomFile.write((CR + CR + CR).getBytes());
			randomFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void printTransFileForSYB(String content, String title)
			throws FileNotFoundException {
		File file = new File(CacheUtil.getPrintpath());
		file.getParentFile().mkdirs();
		StringBuffer buffer = new StringBuffer();
		if (file.exists()) {
			file.delete();
		}
		String page0 = "";
		String page1 = "";
		String page2 = "";
		String page3 = "";
		content = content.trim();
		String[] contents = content.split("\\[10]");
		System.out.println(contents.toString());
		if (contents.length >= 2) {
			page0 = contents[1];
		}
		contents = content.split("\\[11]");
		if (contents.length >= 2) {
			page1 = contents[1];
		}
		contents = content.split("\\[12]");
		if (contents.length >= 2) {
			page2 = contents[1];
		}
		contents = content.split("\\[13]");
		if (contents.length >= 2) {
			page3 = contents[1];
		}
		String headContent = "";
		StringBuffer modal12Letter = new StringBuffer();
		for (int i = 0; i < 30; i++) {
			headContent = headContent + "=";
			if (i < 12) {
				modal12Letter.append("=");
			}
		}
		headContent = headContent + System.getProperty("line.separator");
		String temp = "通联支付  " + title;
		int middleIndex = temp.length() / 2;
		int headMiddelIndex = headContent.length() / 2;
		int startIndex = headMiddelIndex - middleIndex;
		headContent = headContent.substring(0, startIndex)
				+ temp
				+ headContent.substring(startIndex + temp.length(),
						headContent.length());
		String endContent = "." + System.getProperty("line.separator");
		if ((page1 != null) && (!"".equals(page1)) && (page1.length() > 0)) {
			buffer.append(headContent).append(modal12Letter).append("机 构 联")
					.append(modal12Letter)
					.append(System.getProperty("line.separator")).append(page0)
					.append(page1).append(endContent);
		}
		if ((page2 != null) && (!"".equals(page2)) && (page2.length() > 0)) {
			buffer.append(headContent).append(modal12Letter).append("商 戶 联")
					.append(modal12Letter)
					.append(System.getProperty("line.separator")).append(page0)
					.append(page2).append(endContent);
		}
		if ((page3 != null) && (!"".equals(page3)) && (page3.length() > 0)) {
			buffer.append(headContent).append(modal12Letter).append("持 卡 人 联")
					.append(modal12Letter)
					.append(System.getProperty("line.separator")).append(page0)
					.append(page3).append(endContent);
		}
		if (buffer.length() == 0) {
			buffer.append(headContent).append(page0).append(endContent);
		}
		PrintWriter printWriter = new PrintWriter(file);
		printWriter.write(buffer.toString().toCharArray());
		printWriter.flush();
		printWriter.close();
	}

	public static void printTransFileByTemplate(long papers, boolean isReprint,
			RspData response) throws IOException {
		File file = new File(CacheUtil.getPrintpath());
		file.getParentFile().mkdirs();
		if (file.exists()) {
			file.delete();
		}
		String result = buildTransFile(papers, isReprint, response);

		PrintWriter printWriter = new PrintWriter(file);
		printWriter.write(result.toCharArray());
		printWriter.flush();
		printWriter.close();
	}

	public static void buildTerminalInfo(RspData response)
			throws IOException {
		if ((CacheUtil.getPrintpath() == null)
				|| (CacheUtil.getPrintpath().isEmpty())) {
			return;
		}
		File printFile = new File(CacheUtil.getPrintpath());

		printFile.getParentFile().mkdirs();

		String tempPath = printFile.getParentFile().getAbsolutePath()
				+ System.getProperty("file.separator") + "TerminalInfo.txt";

		File terminalFile = new File(tempPath);
		if (terminalFile.exists()) {
			terminalFile.delete();
		}
		String result = "TERID:" + response.GetValue("TerminalNumber");

		PrintWriter printWriter = new PrintWriter(terminalFile);
		printWriter.write(result.toCharArray());
		printWriter.flush();
		printWriter.close();
	}

	public static String buildTransFile(long papers, boolean isReprint,
			RspData response) {
		StringBuffer sb = new StringBuffer();
		for (long i = 0L; i < papers; i += 1L) {
			if (i == 0L) {
				buildTransFile(sb, isReprint, response, "第一联   商户联");
			} else if (i == 1L) {
				buildTransFile(sb, isReprint, response, "第二联   持卡人联");
			} else {
				buildTransFile(sb, isReprint, response, "第三联   银行联");
			}
		}
		return sb.toString();
	}

	public static void buildTransFile(StringBuffer buffer, boolean isReprint,
			RspData response, String title) {
		try {
			BufferedReader bufReader = new BufferedReader(
					new InputStreamReader(new FileInputStream(
							getRelativeFilePath("Trans.txt")),
							"gb2312"));

			String temp = null;
			String result = "";
			while ((temp = bufReader.readLine()) != null) {
				byte[] buffer1 = temp.getBytes("utf-8");
				temp = new String(buffer1, "UTF-8");
				result = replace(temp, response, isReprint, title);
				buffer.append(result);
				if (!result.isEmpty()) {
					buffer.append(System.getProperty("line.separator"));
				}
			}
			bufReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String replace(String src, RspData response,
			boolean isReprint, String title) {
		if (src == null) {
			return null;
		}
		boolean isIndexed = false;

		String result = src;
		if (src.indexOf("[TITLE]") != -1) {
			isIndexed = true;
			result = src.replace("[TITLE]", title);
		}
		if (src.indexOf("XX商户") != -1) {
			isIndexed = true;
			result = src.replace("XX商户", "通联支付");
		}
		if (src.indexOf("[MERCHANTNAME]") != -1) {
			isIndexed = true;
			result = src.replace("[MERCHANTNAME]",
					response.GetValue("MerchantName"));
		}
		if (src.indexOf("[MERID]") != -1) {
			isIndexed = true;
			result = src
					.replace("[MERID]", response.GetValue("MerchantNumber"));
		}
		if (src.indexOf("[TERID]") != -1) {
			isIndexed = true;
			result = src
					.replace("[TERID]", response.GetValue("TerminalNumber"));
		}
		if (src.indexOf("[OPERNO]") != -1) {
			isIndexed = true;
			result = src.replace("[OPERNO]", response.GetValue("Operator"));
		}
		if (src.indexOf("[DATETIME]") != -1) {
			isIndexed = true;
			result = src.replace("[DATETIME]",
					formatDate(response.GetValue("TransDate"), "/") + "  "
							+ formatTime(response.GetValue("TransTime"), ":"));
		}
		if (src.indexOf("[TRANSTYPE]") != -1) {
			isIndexed = true;
			result = src
					.replace("[TRANSTYPE]", getTransName(Long
							.parseLong(response.GetValue("TransType"))));
		}
		if (src.indexOf("[ISSNAME]") != -1) {
			isIndexed = true;
			result = src.replace("[ISSNAME]", response.GetValue("IssName"));
		}
		if (src.indexOf("[ISSNUMBER]") != -1) {
			isIndexed = true;
			result = src.replace("[ISSNUMBER]", response.GetValue("IssNumber"));
		}
		if (src.indexOf("[ACQNUMBER]") != -1) {
			isIndexed = true;
			result = src.replace("[ACQNUMBER]", response.GetValue("AcqNumber"));
		}
		if (src.indexOf("[CARDNO]") != -1) {
			isIndexed = true;
			result = src.replace("[CARDNO]", response.GetValue("CardNumber")
					+ " " + response.GetValue("SwipeType"));
		}
		if (src.indexOf("[BATCHNO]") != -1) {
			isIndexed = true;
			result = src.replace("[BATCHNO]", response.GetValue("BatchNumber"));
		}
		if (src.indexOf("[TRACENO]") != -1) {
			isIndexed = true;
			result = src.replace("[TRACENO]",
					response.GetValue("PosTraceNumber"));
		}
		if (src.indexOf("[AUTHNO]") != -1) {
			isIndexed = true;
			result = src.replace("[AUTHNO]", response.GetValue("AuthNumber"));
		}
		if (src.indexOf("[HOSTNO]") != -1) {
			isIndexed = true;
			result = src.replace("[HOSTNO]",
					response.GetValue("HostSerialNumber"));
		}
		if (src.indexOf("[AMOUNT]") != -1) {
			isIndexed = true;
			if ((response.GetValue("TransType").equals(String.valueOf(4L)))
					|| (response.GetValue("TransType").equals(String
							.valueOf(3L)))) {
				result = src.replace("[AMOUNT]",
						"-" + formatAmount(response.GetValue("Amount")));
			} else {
				result = src.replace("[AMOUNT]",
						formatAmount(response.GetValue("Amount")));
			}
		}
		if (src.indexOf("[CUPS]") != -1) {
			isIndexed = true;
			result = src.replace("[CUPS]", response.GetValue("CUPS"));
		}
		if (src.indexOf("[SETTLEDATE]") != -1) {
			isIndexed = true;
			result = src.replace("[SETTLEDATE]",
					response.GetValue("SettleDate"));
		}
		if (src.indexOf("[MEMO]") != -1) {
			isIndexed = true;
			result = src.replace("[MEMO]", response.GetValue("Memo"));
		}
		if (src.indexOf("[IC]") != -1) {
			isIndexed = true;
			if ((response.GetValue("TVR") != null)
					&& (!response.GetValue("TVR").isEmpty())) {
				result = src.replace("[IC]", buildIcInfo(response));
			} else {
				result = src.replace("[IC]", "");
			}
		}
		if (src.indexOf("[CARDHOLDERNAME]") != -1) {
			isIndexed = true;
			result = src.replace("[CARDHOLDERNAME]", CR + CR
					+ "--------------------------------------");
		}
		if (src.indexOf("[ENSURETRANS]") != -1) {
			isIndexed = true;
			result = src.replace("[ENSURETRANS]", buildAckInfo());
		}
		if (src.indexOf("[POSVER]") != -1) {
			isIndexed = true;
			result = src.replace("[POSVER]", PropUtil.getAppName());
		}
		if (src.indexOf("[REPRINT]") != -1) {
			isIndexed = true;
			if (isReprint) {
				result = src.replace("[REPRINT]", "重打印/DUPLICATED");
			} else {
				result = src.replace("[REPRINT]", "");
			}
		}
		if (src.indexOf("[CUT]") != -1) {
			isIndexed = true;
			result = src.replace("[CUT]", CR);
		}
		if (isIndexed) {
			return replace(result, response, isReprint, title);
		}
		return result;
	}

	private static String buildIcInfo(RspData response) {
		StringBuffer sb = new StringBuffer();

		sb.append("TVR:" + response.GetValue("TVR") + CR);
		sb.append("TSI:" + response.GetValue("TSI") + CR);
		sb.append("AID:" + response.GetValue("AID") + CR);
		sb.append("ATC:" + response.GetValue("ATC") + CR);
		sb.append("APP LABEL NAME:" + response.GetValue("AppLabelName") + CR);
		sb.append("PREFERRED NAME:" + response.GetValue("PreferredName") + CR);
		sb.append("TC:" + response.GetValue("TC"));
		return sb.toString();
	}

	private static String buildAckInfo() {
		StringBuffer sb = new StringBuffer();
		sb.append("本人确认以上交易，同意将其计入本卡账户" + CR);
		sb.append("I ACKNOWLEDGE SATISFACTORY RECEIPT" + CR);
		sb.append("OF RELATIVE GOODS/SERVICE");
		return sb.toString();
	}

	public static String formatAmount(String value) {
		BigDecimal bigDecimal = new BigDecimal(value);
		BigDecimal newDecimal = bigDecimal.divide(new BigDecimal(100.0D), 2, 1);
		return String.valueOf(newDecimal);
	}

	public static byte Xor(byte[] data, int length) {
		byte result = data[1];
		for (int i = 2; i < data.length - 1; i++) {
			result = (byte) (result ^ data[i]);
		}
		return result;
	}

	private static String CR = "";

	static {
		if (PropUtil.getMisValue("SYSTEM").equals("WINDOWS")) {
			CR = "\r\n";
		} else {
			CR = "\n";
		}
	}

	public static String formatType(String value) {
		long a = Long.parseLong(value);
		value = Long.toString(a);
		return value;
	}

	public static Object getFieldData(Object source, String fieldName)
			throws Exception {
		Field field = source.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		return field.get(source);
	}

	public static void setFieldData(Object target, String fieldName,
			Object fieldData) throws Exception {
		Field field = target.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(target, fieldData);
	}
	
	
	public static void writePacket(String packet) {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		File file = new File(getAbsolutePath() + "/logs/LOG_"
				+ format.format(date) + ".txt");
		format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			RandomAccessFile randomFile = new RandomAccessFile(file, "rw");

			long fileLength = randomFile.length();

			randomFile.seek(fileLength);
			randomFile.write((format.format(date) + ": " + packet + "\r\n")
					.getBytes());
			randomFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static int toAsc(String str) {
		char[] chr = str.toCharArray();
		return chr[0];
	}

}
