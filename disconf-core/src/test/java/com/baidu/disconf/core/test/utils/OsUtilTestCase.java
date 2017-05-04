package com.baidu.disconf.core.test.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import com.baidu.disconf.core.common.utils.OsUtil;

public class OsUtilTestCase {

	@Test
	public void makeSureDeleteLockFile() {
		File baseDir = new File(System.getProperty("user.dir"));
		File srcFile = new File(baseDir, "srcFile.txt");
		try {
			FileUtils.writeStringToFile(srcFile, "test data to transfer");
		} catch (IOException ex) {
		}
		
		File destFile = new File(baseDir, "destFile.txt");
		try {
			OsUtil.transferFileAtom(srcFile, destFile, true);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
		
		destFile.delete();
		
		File lockFile = new File(baseDir, destFile.getName() + ".lock");
		Assert.assertFalse("Failed to delete lcok file: " + lockFile.getName(), lockFile.exists());
	}
	
}
