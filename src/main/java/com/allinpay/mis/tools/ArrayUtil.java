package com.allinpay.mis.tools;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class ArrayUtil
{
  public static void addIntList(List<Integer> list, int[] array)
  {
    for (int i = 0; i < array.length; i++) {
      list.add(Integer.valueOf(array[i]));
    }
  }
  
  public static int[] byte2Int(byte[] source)
  {
    int[] target = new int[source.length];
    for (int i = 0; i < source.length; i++) {
      target[i] = source[i];
    }
    return target;
  }
  
  public static byte[] copy(byte[] rSource)
  {
    byte[] aResult = new byte[rSource.length];
    System.arraycopy(rSource, 0, aResult, 0, aResult.length);
    return aResult;
  }
  
  public static byte[] copy(byte[] rSource, int targetlen)
  {
    byte[] aResult = new byte[targetlen];
    System.arraycopy(rSource, 0, aResult, 0, targetlen);
    return aResult;
  }
  
  public static byte[] copy(byte[] rSource, int startIndex, int length)
  {
    byte[] result = new byte[length];
    for (int i = 0; i < result.length; i++)
    {
      result[i] = rSource[startIndex];
      startIndex++;
    }
    return result;
  }
  
  public static int[] copy(int[] rSource)
  {
    int[] aResult = new int[rSource.length];
    System.arraycopy(rSource, 0, aResult, 0, aResult.length);
    return aResult;
  }
  
  public static boolean equals(byte[] a1, byte[] a2)
  {
    if ((a1 == null) || (a2 == null)) {
      return a1 == a2;
    }
    int nLength = a1.length;
    if (nLength != a2.length) {
      return false;
    }
    for (int i = 0; i < nLength; i++) {
      if (a1[i] != a2[i]) {
        return false;
      }
    }
    return true;
  }
  
  public static boolean equals(int[] a1, int[] a2)
  {
    if ((a1 == null) || (a2 == null)) {
      return a1 == a2;
    }
    int nLength = a1.length;
    if (nLength != a2.length) {
      return false;
    }
    for (int i = 0; i < nLength; i++) {
      if (a1[i] != a2[i]) {
        return false;
      }
    }
    return true;
  }
  
  public static byte[] intArray2byteArray(int[] source)
  {
    byte[] target = new byte[source.length];
    for (int i = 0; i < source.length; i++) {
      target[i] = ((byte)(source[i] & 0xFF));
    }
    return target;
  }
  
  public static byte[] join(byte[] a1, byte[] a2)
  {
    byte[] result = new byte[a1.length + a2.length];
    System.arraycopy(a1, 0, result, 0, a1.length);
    System.arraycopy(a2, 0, result, a1.length, a2.length);
    return result;
  }
  
  public static int[] join(int[] a1, int[] a2)
  {
    int[] result = new int[a1.length + a2.length];
    System.arraycopy(a1, 0, result, 0, a1.length);
    System.arraycopy(a2, 0, result, a1.length, a2.length);
    return result;
  }
  
  public static void main(String[] args)
  {
    byte[] a = { 0, 91 };
    
    int x = new Integer(a[0]).intValue() * 256 + new Integer(a[1]).intValue();
    System.out.println(x);
  }
  
  public static byte[] toArray(List<Integer> source, byte[] target, int sIndex, int tIndex)
  {
    for (int i = 0; i < source.size(); i++)
    {
      Integer value = (Integer)source.get(i);
      target[tIndex] = value.byteValue();
      tIndex++;
    }
    return target;
  }
  
  public static int[] toArray(List<Integer> source, int sIndex, int length)
  {
    int[] target = new int[length];
    for (int i = 0; i < target.length; i++)
    {
      target[i] = ((Integer)source.get(sIndex)).intValue();
      sIndex++;
    }
    return target;
  }
  
  public static int toInt(byte[] source, int length)
  {
    int x = 0;
    int result = 0;
    for (int i = 0; i < source.length - 1; i++) {
      x += 8;
    }
    for (int i = 0; i < length; i++)
    {
      result |= source[i] & 255 << x;
      x -= 8;
    }
    return result;
  }
  
  public static int[] toIntArray(byte[] source)
  {
    int[] result = new int[source.length];
    for (int i = 0; i < result.length; i++) {
      source[i] &= 0xFF;
    }
    return result;
  }
  
  public static List<Integer> toList(byte[] source, int sIndex, int length)
  {
    List<Integer> target = new ArrayList();
    for (int i = 0; i < length; i++)
    {
      target.add(Integer.valueOf(source[sIndex] & 0xFF));
      sIndex++;
    }
    return target;
  }
  
  public static byte[] getRealByte(byte[] b, int targetLength)
  {
    //System.out.println("格式化之前报文:" + PacketResolveUtil.ByteArrToHex(b));
    byte[] result;
    if ((targetLength != 0) && (b.length > targetLength)) {
      result = copy(b, 0, targetLength);
    } else {
      result = b;
    }
    //System.out.println("格式化之后报文:" + PacketResolveUtil.ByteArrToHex(result));
    return result;
  }
}
