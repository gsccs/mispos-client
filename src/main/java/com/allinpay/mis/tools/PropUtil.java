package com.allinpay.mis.tools;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

public class PropUtil {
	
	private static Properties appsProp = new Properties();
	private static Properties cardProp = new Properties();
	private static Properties rejcodeProp = new Properties();
	private static Properties misProp = new Properties();
	private static Properties dbProp = new Properties();
	private static String appName;
	static {
		try {
			String confHome = getAbsolutePath()+"/config/";
			appsProp.load(new BufferedInputStream(new FileInputStream(confHome+"allinpay-apps.properties")));
			cardProp.load(new BufferedInputStream(new FileInputStream(confHome+"allinpay-card.properties")));
			rejcodeProp.load(new BufferedInputStream(new FileInputStream(confHome+"allinpay-rejcode.properties")));
			misProp.load(new BufferedInputStream(new FileInputStream(confHome+"allinpay-mispos.properties")));
			dbProp.load(new BufferedInputStream(new FileInputStream(confHome+"allinpay-db.properties")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getAbsolutePath(){
		File file = new File("");
		String home = file.getAbsolutePath();
		if (home.indexOf(":") == -1) {
			home = "/" + home;
		}
		return home;
	}
	
	public static String getMisValue(String key) {
		return (String) misProp.get(key);
	}
	
	public static String getRejcodeExplCom(String key) {
		return rejcodeProp.getProperty(key);
	}
	
	public static String getRejcodeExpl(String cardType,String code) {
		return rejcodeProp.getProperty(cardType+"."+code);
	}
	
	public static String getCardName(String key) {
		return (String) cardProp.get("card_name_" + key);
	}
	
	public static String getCardCode(String key) {
		return (String) cardProp.get("card_code_" + key);
	}
	
	public static String[] getCardKeyList() {
		String cardkeys = (String) cardProp.get("card_key");
		return cardkeys.split(",");
	}
	
	public static Map<String,String> getCardTrans(final String cardkey) {
		Map<String,String> map = new TreeMap<String, String>( new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				if (o1.startsWith("card_trans_"+cardkey) && o2.startsWith("card_trans_"+cardkey)){
					String o1_v = o1.split("-")[1];
					String o2_v = o2.split("-")[1];
					return Integer.valueOf(o1_v).compareTo(Integer.valueOf(o2_v));
				}else{
					return 0;
				}
			}
		});
		
		Iterator<Object> its = cardProp.keySet().iterator();
		while(its.hasNext()){
			String key = (String)its.next();
			if (key.startsWith("card_trans_"+cardkey)){
				map.put(key, (String)cardProp.get(key));
			}
		}
		return map;
	}
	
	

	
	public static String[] getAppCodes(){
		if (null==appsProp){
			return null;
		}
		String appCodes = appsProp.getProperty("app_codes");
		if (null==appCodes || appCodes.trim().equals("")){
			return null;
		}
		return appCodes.split(",");
	}
	
	public static String getAppName(String mchNo) {
		return (String) appsProp.get("app_name_"+mchNo);
	}
	
	public static String getAppKey(String mchNo) {
		return (String) appsProp.get("app_key_"+mchNo);
	}
	
	public static String getAppPort(String mchNo) {
		return (String) appsProp.get("app_port_"+mchNo);
	}
	
	public static void writeMisProp(String key,String value){
		misProp.setProperty(key, value);
		try {
			misProp.store(new FileOutputStream(getAbsolutePath()+"/config/allinpay-mispos.properties"), "");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void writeProp(Properties properties)
	{
	    try
	    {
	      Enumeration<?> enu = properties.propertyNames();
	      while (enu.hasMoreElements())
	      {
	        String key = (String)enu.nextElement();
	        misProp.setProperty(key, properties.getProperty(key));
	      }
	      misProp.store(new FileOutputStream(getAbsolutePath()+"/config/allinpay-mispos.properties"), "");
	    }
	    catch (IOException e)
	    {
	      e.printStackTrace();
	    }
	}
	
	public static String getAppName() {
		return appName;
	}
	public static void setAppName(String appName) {
		PropUtil.appName = appName;
	}
	
	
	public static String getDBValue(String key){
		return dbProp.getProperty(key);
	}
	
	
	public static Integer getVersion(){
		String version = misProp.getProperty("VERSION","1");
		Integer verInt = Integer.parseInt(version);
		return verInt;
	}
	
	public static String getUpdateUrl(){
		return misProp.getProperty("UPDATEURL","1");
	}
	
	
	/**
     * 获取property
     * @param key
     * @return
     */
    public static String getProperty(String key){
        Properties pps = new Properties();
        try {
            InputStream in = new BufferedInputStream (new FileInputStream(ConstantsTools.PATH_PROPERTY));
            pps.load(in);
            String value = pps.getProperty(key);
            return value;

        }catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
	
}
