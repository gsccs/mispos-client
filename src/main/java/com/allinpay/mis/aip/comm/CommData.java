package com.allinpay.mis.aip.comm;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CommData implements Serializable
{
  private static final long serialVersionUID = -4879144123313676586L;
  private byte[] sendCommBuf;
  private byte[] backComData;
  private byte[] contentTo;
  private Map<String, String> commdata = new HashMap();
  
  public byte[] getBackComData()
  {
    return this.backComData;
  }
  
  public Map<String, String> getCommdata()
  {
    return this.commdata;
  }
  
  public byte[] getContentTo()
  {
    return this.contentTo;
  }
  
  public byte[] getSendCommBuf()
  {
    return this.sendCommBuf;
  }
  
  public String GetValue(String key)
  {
    key = key.toLowerCase().trim();
    if (this.commdata.containsKey(key)) {
      return (String)this.commdata.get(key);
    }
    return "";
  }
  
  public void PutValue(String key, String value)
  {
    key = key.toLowerCase().trim();
    this.commdata.put(key, value.trim());
  }
  
  public void setBackComData(byte[] backComData)
  {
    this.backComData = backComData;
  }
  
  public void setContentTo(byte[] contentTo)
  {
    this.contentTo = contentTo;
  }
  
  public void setSendCommBuf(byte[] sendCommBuf)
  {
    this.sendCommBuf = sendCommBuf;
  }
}

