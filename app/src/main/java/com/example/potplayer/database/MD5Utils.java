package com.example.potplayer.database;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
public class MD5Utils {
    /**
     * md5加密方法
     * @param password
     * @return
     */
    public static String md5Password(String password){
        try {
//得到一个信息摘要器
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(password.getBytes());
            StringBuffer buffer = new StringBuffer();
//要把每一个byte做一个与运算0xff,0xff是十六进制，十进制为255
            for(byte b:result){
//与运算
                int number = b & 0xff;
                String str = Integer.toHexString(number);
//    System.out.println(str);
//如果位数不够前面加个零
                if(str.length()==1){
                    buffer.append("0");
                }
                buffer.append(str);
            }
//标准的md5加密后的结果
            return buffer.toString();
        } catch (NoSuchAlgorithmException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
    }
}