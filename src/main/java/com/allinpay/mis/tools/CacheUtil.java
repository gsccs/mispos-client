package com.allinpay.mis.tools;


public class CacheUtil
{
  private static String timeOut;
  private static String comport;
  private static String posType;
  private static Long papers;
  private static String system;
  private static String printpath;
  private static String mISHEADTAG;
  private static String localDateTime;
  private static Boolean isSettle;
  public static final long REJCODE_SUCCESS = 0L;
  public static final long REJCODE_INIT_ERROR = -100L;
  public static final long REJCODE_CONFIG_READ_ERROR = -101L;
  public static final long REJCODE_COMM_SETBUF_ERROR = -102L;
  public static final long REJCODE_USER_CANCEL = -104L;
  public static final long REJCODE_COMM_TIMEOUT = -105L;
  public static final long REJCODE_COMM_UNKNOW = -106L;
  public static final long REJCODE_COMM_INUSE = -107L;
  public static final long REJCODE_COMM_OPEN_ERROR = -108L;
  public static final long REJCODE_COMM_GETBUF_ERROR = -109L;
  public static final long REJCODE_UNKNOW_TRANS = -112L;
  public static final long REJCODE_SOCKET_INIT_ERROR = -113L;
  public static final long REJCODE_SOCKET_RECEIVE_ERROR = -114L;
  public static final long REJCODE_SOCKET_SEND_ERROR = -115L;
  public static final long REJCODE_COMM_SEND_ERROR = -118L;
  public static final long REJCODE_COMM_RECEIVE_ERROR = -119L;
  public static final long REJCODE_COMM_TEST_ERROR = -123L;
  public static final long REJCODE_GETCOMM_ERROR = -124L;
  public static final long REJCODE_SYSTEMFILE_ERROR = -202L;
  public static final long REJCODE_CHECKREQUEST_ERROR = -209L;
  public static final String CR_WINDOWS = "\r\n";
  public static final String CR_LINUX = "\n";
  public static final int TEST_TIMEOUT = 5;
  public static final int CONFIG_TIMEOUT = 0;
  public static final long MANAGER_TRANSTYPE_COMTEST = 7L;
  public static final long BANKCARD_TRANSTYPE_LOGIN = 1L;
  public static final long BANKCARD_TRANSTYPE_SALE = 2L;
  public static final long BANKCARD_TRANSTYPE_VOID = 3L;
  public static final long BANKCARD_TRANSTYPE_REFUND = 4L;
  public static final long BANKCARD_TRANSTYPE_TIPS = 5L;
  public static final long BANKCARD_TRANSTYPE_PREAUTH = 6L;
  public static final long BANKCARD_TRANSTYPE_PREAUTHVOID = 7L;
  public static final long BANKCARD_TRANSTYPE_AUTHCOMPLETE = 8L;
  public static final long BANKCARD_TRANSTYPE_ADDAUTH = 9L;
  public static final long BANKCARD_TRANSTYPE_AUTHOFFLINE = 10L;
  public static final long BANKCARD_TRANSTYPE_AUTHCOMPLETEVOID = 11L;
  public static final long BANKCARD_TRANSTYPE_OFFLINE = 12L;
  public static final long BANKCARD_TRANSTYPE_ADJUST = 13L;
  public static final long BANKCARD_TRANSTYPE_SETTLE = 14L;
  public static final long BANKCARD_TRANSTYPE_QUERYLAST = 15L;
  public static final long BANKCARD_TRANSTYPE_REPRINT = 16L;
  public static final long BANKCARD_TRANSTYPE_DATASEND = 17L;
  public static final long BANKCARD_TRANSTYPE_QUERY = 18L;
  public static final long BANKCARD_TRANSTYPE_REPRINTSETTLE = 19L;
  public static final long BANKCARD_TRANSTYPE_QUERYDETAIL = 20L;
  public static final long BANKCARD_TRANSTYPE_SETUP = 21L;
  public static final long BANKCARD_TRANSTYPE_SETCOMM = 22L;
  public static final long BANKCARD_TRANSTYPE_MIAFRE_SALE = 23L;
  public static final long BANKCARD_TRANSTYPE_INSTALLMENT = 24L;
  public static final long BANKCARD_TRANSTYPE_INSTALLMENT_VOID = 25L;
  public static final long BANKCARD_TRANSTYPE_MICROPAYMENT = 26L;
  public static final long BANKCARD_TRANSTYPE_MICROQUERY = 27L;
  public static final long BANKCARD_TRANSTYPE_SPECIFY_SAVE = 28L;
  public static final long BANKCARD_TRANSTYPE_UNSPECIFY_SAVE = 29L;
  public static final long BANKCARD_TRANSTYPE_CASH_RECHARGE = 30L;
  public static final long BANKCARD_TRANSTYPE_AUTO_SETTLE = 31L;
  public static final long BANKCARD_TRANSTYPE_GET_COMPORT = 127L;
  public static final long BANKCARD_TRANSTYPE_PUBLICKEY_LOAD = 129L;
  public static final long BANKCARD_TRANSTYPE_ICPARA_LOAD = 130L;
  public static final String BANKCARD_TAG_POSAPPNAME = "9F00";
  public static final String BANKCARD_TAG_TRANSTYPE = "9F01";
  public static final String BANKCARD_TAG_TRANSAMOUNT = "9F02";
  public static final String BANKCARD_TAG_MERCHANTNAME = "9F03";
  public static final String BANKCARD_TAG_MERCHANTNUMBER = "9F04";
  public static final String BANKCARD_TAG_TERMINALNUMBER = "9F05";
  public static final String BANKCARD_TAG_PERSONNUMBER = "9F06";
  public static final String BANKCARD_TAG_ACQNUMBER = "9F07";
  public static final String BANKCARD_TAG_ISSNUMBER = "9F08";
  public static final String BANKCARD_TAG_ISSNAME = "9F09";
  public static final String BANKCARD_TAG_POSCNUMBER = "9F0A";
  public static final String BANKCARD_TAG_CARDNUMBER = "9F0B";
  public static final String BANKCARD_TAG_SWIPETYPE = "9F0C";
  public static final String BANKCARD_TAG_BATCHNUMBER = "9F0D";
  public static final String BANKCARD_TAG_REFERENCENUMBER = "9F0E";
  public static final String BANKCARD_TAG_AUTHNUMBER = "9F0F";
  public static final String BANKCARD_TAG_HOSTSERIALNUMBER = "9F10";
  public static final String BANKCARD_TAG_TRANSDATE = "9F11";
  public static final String BANKCARD_TAG_TRANSTIME = "9F12";
  public static final String BANKCARD_TAG_MEM = "9F13";
  public static final String BANKCARD_TAG_REJCODE = "9F14";
  public static final String BANKCARD_TAG_CONTENT = "9F15";
  public static final String BANKCARD_TAG_IPADDRESS = "9F16";
  public static final String BANKCARD_TAG_PORT = "9F17";
  public static final String BANKCARD_TAG_TIMEOUT = "9F18";
  public static final String BANKCARD_TAG_REJCODEEXPLAIN = "9F19";
  public static final String BANKCARD_TAG_TRANSCHECK = "9F1A";
  public static final String BANKCARD_TAG_EXPIREDATE = "9F1B";
  public static final String BANKCARD_TAG_TIPS = "9F1C";
  public static final String BANKCARD_TAG_TOTAL = "9F1D";
  public static final String BANKCARD_TAG_STORENUMBER = "9F1E";
  public static final String BANKCARD_TAG_POSNUMBER = "9F1F";
  public static final String BANKCARD_TAG_OLDTRACENUMBER = "9F20";
  public static final String BANKCARD_TAG_BALANCEAMOUNT = "9F23";
  
  public static void init()
  {
    timeOut = PropUtil.getMisValue("TIMEOUT");
    comport = PropUtil.getMisValue("COMPORT");
    //properties.getProperty("COMMRATE");
    posType = PropUtil.getMisValue("POSTYPE");
    papers = Long.valueOf(PropUtil.getMisValue("PAPERS"));
    system = PropUtil.getMisValue("SYSTEM");
    printpath = PropUtil.getMisValue("PRINTPATH");
    isSettle = Boolean.valueOf(Boolean.parseBoolean(PropUtil.getMisValue("ISSETTLE")));
    mISHEADTAG = PropUtil.getMisValue("MISHEADTAG");
  }
  
  
  public static String getComport()
  {
    return comport;
  }
  
  public static Long getPapers()
  {
    return papers;
  }
  
  public static String getPosType()
  {
    return posType;
  }
  
  public static String getSystem()
  {
    return system;
  }
  
  public static String getTimeOut()
  {
    return timeOut;
  }
  
  public static void setComm(String comport)
  {
	  comport = comport;
  }
  
  public static void setPapers(Long papers)
  {
    papers = papers;
  }
  
  public static void setPosType(String posType)
  {
    posType = posType;
  }
  
  public static void setSystem(String system)
  {
    system = system;
  }
  
  public static void setTimeOut(String timeOut)
  {
    timeOut = timeOut;
  }
  
  public static String getPrintpath()
  {
    return printpath;
  }
  
  public static void setPrintpath(String printpath)
  {
    printpath = printpath;
  }
  
  public static Boolean getIsSettle()
  {
    return isSettle;
  }
  
  public static void setIsSettle(Boolean isSettle)
  {
    isSettle = isSettle;
  }
  
  public static final String[] BANKCARD_TAG_ORDERNUMBER = { "9FFF", 
    "ordernumber" };
  public static final String BANKCARD_TAG_TVR = "9F24";
  public static final String BANKCARD_TAG_TSI = "9F25";
  public static final String BANKCARD_TAG_AID = "9F26";
  public static final String BANKCARD_TAG_ATC = "9F27";
  public static final String BANKCARD_TAG_APPLABELNAME = "9F28";
  public static final String BANKCARD_TAG_PREFERREDNAME = "9F29";
  public static final String BANKCARD_TAG_TC = "9F2A";
  public static final long WANSHANG_TRANSTYPE_LOGIN = 1L;
  public static final long WANSHANG_TRANSTYPE_SALE = 2L;
  public static final long WANSHANG_TRANSTYPE_VOID = 3L;
  public static final long WANSHANG_TRANSTYPE_REFUND = 4L;
  public static final long WANSHANG_TRANSTYPE_PAYMENT = 5L;
  public static final long WANSHANG_TRANSTYPE_RECHARGE = 6L;
  public static final long WANSHANG_TRANSTYPE_QUERY = 7L;
  public static final long WANSHANG_TRANSTYPE_SETTLE = 14L;
  public static final long WANSHANG_TRANSTYPE_QUERYLAST = 15L;
  public static final long WANSHANG_TRANSTYPE_REPRINT = 16L;
  public static final long WANSHANG_TRANSTYPE_DATASEND = 17L;
  public static final long WANSHANG_TRANSTYPE_REPRINTSETTLE = 19L;
  public static final long WANSHANG_TRANSTYPE_QUERYDETAIL = 20L;
  public static final long WANSHANG_TRANSTYPE_SETUP = 21L;
  public static final long WANSHANG_TRANSTYPE_SETCOMM = 22L;
  public static final long WANSHANG_TRANSTYPE_LOADKEY = 48L;
  public static final long WANSHANG_TRANSTYPE_LOADPARA = 49L;
  public static final long WANSHANG_TRANSTYPE_GET_COMPORT = 127L;
  
  
  public static final String WANSHANG_TAG_POSAPPNAME = "9F00";
  public static final String WANSHANG_TAG_TRANSTYPE = "9F01";
  public static final String WANSHANG_TAG_TRANSAMOUNT = "9F02";
  public static final String WANSHANG_TAG_MERCHANTNAME = "9F03";
  public static final String WANSHANG_TAG_MERCHANTNUMBER = "9F04";
  public static final String WANSHANG_TAG_TERMINALNUMBER = "9F05";
  public static final String WANSHANG_TAG_PERSONNUMBER = "9F06";
  public static final String WANSHANG_TAG_ACQNUMBER = "9F07";
  public static final String WANSHANG_TAG_ISSNUMBER = "9F08";
  public static final String WANSHANG_TAG_ISSNAME = "9F09";
  public static final String WANSHANG_TAG_POSCNUMBER = "9F0A";
  public static final String WANSHANG_TAG_CARDNUMBER = "9F0B";
  public static final String WANSHANG_TAG_SWIPETYPE = "9F0C";
  public static final String WANSHANG_TAG_BATCHNUMBER = "9F0D";
  public static final String WANSHANG_TAG_REFERENCENUMBER = "9F0E";
  public static final String WANSHANG_TAG_AUTHNUMBER = "9F0F";
  public static final String WANSHANG_TAG_HOSTSERIALNUMBER = "9F10";
  public static final String WANSHANG_TAG_TRANSDATE = "9F11";
  public static final String WANSHANG_TAG_TRANSTIME = "9F12";
  public static final String WANSHANG_TAG_MEM = "9F13";
  public static final String WANSHANG_TAG_REJCODE = "9F14";
  public static final String WANSHANG_TAG_CONTENT = "9F15";
  public static final String WANSHANG_TAG_IPADDRESS = "9F16";
  public static final String WANSHANG_TAG_PORT = "9F17";
  public static final String WANSHANG_TAG_TIMEOUT = "9F18";
  public static final String WANSHANG_TAG_REJCODEEXPLAIN = "9F19";
  public static final String WANSHANG_TAG_TRANSCHECK = "9F1A";
  public static final String WANSHANG_TAG_EXPIREDATE = "9F1B";
  public static final String WANSHANG_TAG_TIPS = "9F1C";
  public static final String WANSHANG_TAG_TOTAL = "9F1D";
  public static final String WANSHANG_TAG_STORENUMBER = "9F1E";
  public static final String WANSHANG_TAG_POSNUMBER = "9F1F";
  public static final String WANSHANG_TAG_OLDTRACENUMBER = "9F20";
  public static final String WANSHANG_TAG_PRODUCTNUMBER = "9F21";
  public static final String WANSHANG_TAG_PRODUCTNAME = "9F22";
  public static final String WANSHANG_TAG_BALANCEAMOUNT = "9F23";
  
  public static final String WANSHANG_TAG_JFDH = "9F91";
  public static final String WANSHANG_TAG_JFKM = "9F92";
  public static final String WANSHANG_TAG_KHXM = "9F93";
  public static final String WANSHANG_TAG_SFZH = "9F94";
  public static final String WANSHANG_TAG_YWBH = "9F95";
  
  
  
}
