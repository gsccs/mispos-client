package com.allinpay.mis.tools;

public class HexUtils
{
  public static String bin2hex(String bin)
  {
    char[] digital = "0123456789ABCDEF".toCharArray();
    StringBuffer sb = new StringBuffer("");
    byte[] bs = bin.getBytes();
    for (int i = 0; i < bs.length; i++)
    {
      int bit = (bs[i] & 0xF0) >> 4;
      sb.append(digital[bit]);
      bit = bs[i] & 0xF;
      sb.append(digital[bit]);
    }
    return sb.toString();
  }
  
  public static String byte2Bin(byte b)
  {
    return String.valueOf(b);
  }
  
  public static String byte2hex(byte[] b)
  {
    String hs = "";
    String tmp = "";
    for (int n = 0; n < b.length; n++)
    {
      tmp = Integer.toHexString(b[n] & 0xFF);
      if (tmp.length() == 1) {
        hs = hs + "0" + tmp;
      } else {
        hs = hs + tmp;
      }
    }
    tmp = null;
    return hs.toUpperCase();
  }
  
  public static int[] byte2Int(byte[] source)
  {
    int[] target = new int[source.length];
    for (int i = 0; i < source.length; i++) {
      target[i] = source[i];
    }
    return target;
  }
  
  public static int bytes2Bin(byte[] buf)
  {
    String a = byte2hex(buf);
    
    int b = Integer.valueOf(a).intValue();
    
    int c = Integer.parseInt(String.valueOf(b), 16);
    
    return c;
  }
  
  public static int bytes2int(byte[] bytes)
  {
    int num = bytes[0] & 0xFF;
    num |= bytes[1] << 8 & 0xFF00;
    
    return num;
  }
  
  public static String hex2bin(String hex)
  {
    String digital = "0123456789ABCDEF";
    char[] hex2char = hex.toCharArray();
    byte[] bytes = new byte[hex.length() / 2];
    for (int i = 0; i < bytes.length; i++)
    {
      int temp = digital.indexOf(hex2char[(2 * i)]) * 16;
      temp += digital.indexOf(hex2char[(2 * i + 1)]);
      bytes[i] = ((byte)(temp & 0xFF));
    }
    return new String(bytes);
  }
  
  public static byte[] hex2byte(byte[] b)
  {
    if (b.length % 2 != 0) {
      throw new IllegalArgumentException("长度不是偶数");
    }
    byte[] b2 = new byte[b.length / 2];
    for (int n = 0; n < b.length; n += 2)
    {
      String item = new String(b, n, 2);
      

      b2[(n / 2)] = ((byte)Integer.parseInt(item, 16));
    }
    b = null;
    return b2;
  }
  
  public static byte int2byte(int i)
  {
    return (byte)(0xFF & i);
  }
  
  public static byte[] int2bytes(int i)
  {
    byte[] bt = new byte[4];
    bt[0] = ((byte)(0xFF & i));
    bt[1] = ((byte)((0xFF00 & i) >> 8));
    bt[2] = ((byte)((0xFF0000 & i) >> 16));
    bt[3] = ((byte)((0xFF000000 & i) >> 24));
    
    return bt;
  }
  
  public static String toHex(byte b)
  {
    //return "0123456789ABCDEF".charAt(0xF & b >> 4) + "0123456789ABCDEF".charAt(b & 0xF);
	  return ("" + "0123456789ABCDEF".charAt(0xf & b >> 4) + "0123456789ABCDEF".charAt(b & 0xf));
  }
  
  public static Integer toHexInteger(byte b)
  {
    return Integer.valueOf(toHex(b));
  }
}

