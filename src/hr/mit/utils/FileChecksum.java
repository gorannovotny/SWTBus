package hr.mit.utils;

import java.io.FileInputStream;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

public class FileChecksum {
	public static long check(String file) {
		long retval;
		try {
			CheckedInputStream cis = new CheckedInputStream(new FileInputStream(file), new CRC32());
			byte[] buffer = new byte[1024];
			while (cis.read(buffer) > 0) {
			}
			retval = cis.getChecksum().getValue();
			cis.close();
			return retval;
		} catch (Exception e) {
			retval = 0;
		}
		return retval;
	}
}
