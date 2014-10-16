package com.skopis.john.crypto;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.tomcat.jni.Hash;
import org.apache.tomcat.jni.Library;


public class WPA2Hash implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5572369025901670475L;
	private static final Logger LOGGER = LoggerFactory.getLogger(WPA2Hash.class);
	private String ssid;

	public WPA2Hash(String ssid) {
		this.ssid = ssid;
		init();
	}

	public byte[] getHash(String password) throws Exception {
		byte[] h = Hash.pbkdf2HmacSha1(password, ssid);
		return h;
	}

	private void init() {
		try {
			Library.initialize(null);
			LOGGER.info("Successfully initialized JNI");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// We need to make sure the native library is loaded when we deserialize.
	private void readObject(ObjectInputStream o)
			throws IOException, ClassNotFoundException {  

		ssid = (String) o.readObject();  
		init();
	}
}
