package com.whz.blog.util;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Md5Utils {

    /**
     * 总加密次数
     * 防止前端参数中的MD5值被伪造
     */
    public static final int ENCRYPT_TIMES = 3;

    /**
     * @Description: 生成MD5
     * @param message
     * @return
     */

    public static String getMD5(String message) {
       return Md5Utils.getMD5(message,Md5Utils.ENCRYPT_TIMES);
    }

    public static String getMD5(String message,int times ) {

        String md5Str = message;
        for (int i = 0; i < times; i++) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5"); // 创建一个md5算法对象
                byte[] messageByte = md5Str.getBytes("UTF-8");
                byte[] md5Byte = md.digest(messageByte); // 获得MD5字节数组,16*8=128位
                md5Str = bytesToHex(md5Byte) ;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return md5Str;
    }


    /**
     * @Description: 二进制转十六进制
     * @param bytes
     * @return
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuffer hexStr = new StringBuffer();
        int num;
        for (int i = 0; i < bytes.length; i++) {
            num = bytes[i];
            //这种做法比较难以理解,字节有8位，由于计算机内部计算使用原码，加上 2^8 次方后会等于原来的值,
            // 由于num单位位int，位数增加后，原本的符号位值被计入值
//            if (num < 0) {
//                num += 256;
//            }
//            if ( num < 16 && num >= 0) {
//                hexStr.append("0");
//            }
            String hex = Integer.toHexString(num & 0xff );
            if( hex.length() == 1 ) {
                hex = '0' + hex;
            }
            hexStr.append( hex );
        }
        //返回大写结果
        return hexStr.toString().toUpperCase();
    }

    /**
     *
     * @Description: 签名：请求参数排序并后面补充key值，最后进行MD5加密，返回大写结果
     * @param params 参数内容
     * @return
     */


    public static String signatures(Map<String, Object> params){
        String signatures = "";
        try {
            List<String> paramsStr = new ArrayList<String>();
            for (String key1 : params.keySet()) {
                if(null != key1 && !"".equals(key1)){
                    paramsStr.add(key1);
                }
            }
            Collections.sort(paramsStr);
            StringBuilder sbff = new StringBuilder();
            for (String kk : paramsStr) {
                String value = params.get(kk).toString();
                if ("".equals(sbff.toString())) {
                    sbff.append(kk + "=" + value);
                } else {
                    sbff.append("&" + kk + "=" + value);
                }
            }
            //加上key值
            signatures = getMD5(sbff.toString()).toUpperCase();
        }catch(Exception e) {
            e.printStackTrace();
        }
        return signatures;
    }

    public static void main(String[] args) {
        System.out.println(getMD5("c1415238952",1));
    }
}
