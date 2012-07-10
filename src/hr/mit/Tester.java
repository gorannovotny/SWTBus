package hr.mit;

import hr.mit.utils.PrintUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Tester {

	/**
	 * @param args
	 * @throws NoSuchAlgorithmException 
	 * @throws UnsupportedEncodingException 
	 */
	public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		String pass = "TEST";
		md.update(pass.getBytes());
		System.out.println(bytesToHex(md.digest()));
		md.update(pass.getBytes("CP1250"));
		System.out.println(bytesToHex(md.digest()));
		md.update(pass.getBytes("UTF-16"));
		System.out.println(bytesToHex(md.digest()));
		
		System.out.println(bytesToHex(("0123\r".getBytes())));
		
	}

	public static String bytesToHex(byte[] b) {
		char hexDigit[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		StringBuffer buf = new StringBuffer();
		for (int j = 0; j < b.length; j++) {
			buf.append(hexDigit[(b[j] >> 4) & 0x0f]);
			buf.append(hexDigit[b[j] & 0x0f]);
		}
		return buf.toString();
	}
	
}
