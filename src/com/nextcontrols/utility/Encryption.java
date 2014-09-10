package com.nextcontrols.utility;

import java.security.MessageDigest;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean
@ApplicationScoped
public class Encryption {
		
		public static Encryption sProps;
		private  Encryption()  {}

		public synchronized static Encryption getInstance() {
			if (sProps == null) {
				sProps = new Encryption();
			}
			return sProps;
		}

		public String encryptPassword(String password) {
			//encrypting the password before saving it
			String temp = "9001"+password+"next";
			byte[] defaultBytes = temp.getBytes();
	        StringBuffer hexString = new StringBuffer();
	        try{
	            MessageDigest algorithm = MessageDigest.getInstance("MD5");
	            algorithm.reset();
	            algorithm.update(defaultBytes);
	            byte messageDigest[] = algorithm.digest();
	                           for (int i=0;i<messageDigest.length;i++) {
	                String hex = Integer.toHexString(0xFF & messageDigest[i]);
	                if(hex.length()==1)
	                    hexString.append('0');
	                hexString.append(hex);
	                }
	            }catch (Exception e){e.printStackTrace();}
			return hexString.toString();
		}
		
}

