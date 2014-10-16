package com.skopis.john.crypto;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.tomcat.jni.Hash;
import org.apache.tomcat.jni.Library;

public class App {
	public static String SALT = "test";

	public static void main(String[] args) throws NoSuchAlgorithmException,
			InvalidKeySpecException {

		try {
			Library.initialize(null);
			long startTime = System.currentTimeMillis();

			for (int i = 0; i < 1000; i++) {
				String password = "tesetteset" + i;
				byte[] hash = Hash.pbkdf2HmacSha1(password, SALT);
				//System.out.println(hash);
			}
			long endTime = System.currentTimeMillis();
			long duration = (endTime - startTime);
			System.out.println("(native) Generated 1000 hashes in " + duration + "ms.");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		long startTime = System.currentTimeMillis();
		for (int i = 0; i < 1000; i++) {
			String password = "tesetteset" + i;
			byte[] hash = pbkdf2Hash(password);
		}
		long endTime = System.currentTimeMillis();
		long duration = (endTime - startTime);
		System.out.println("(java) Generated 1000 hashes in " + duration + "ms.");


	}

	private static byte[] pbkdf2Hash(String password)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		int iterations = 4096;
		char[] chars = password.toCharArray();
		byte[] salt = SALT.getBytes();

		PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
		byte[] hash = null;
		hash = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1").generateSecret(spec).getEncoded();
		return hash;
	}
}
