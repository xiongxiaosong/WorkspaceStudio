package com.xxs.sdk.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 压缩文件的工具类
 * 
 * @author Administrator
 * @time 2013-12-10
 * @introduce 压缩和解压工具类，实现文件的压缩以及解压，无中文乱码
 */
public class XZipUtil {
	/**
	 * 压缩文件的方法
	 * 
	 * @param sorcfile
	 *            待压缩文件地址
	 * @param zipfile
	 *            压缩文件的目标地址
	 */
	public static void ZipFolderMethod(File sorcfile,File zipfile) {
		try {
			ZipOutputStream zipout = new ZipOutputStream(new FileOutputStream(zipfile));// 创建zip包文件的输出流
//			File file = new File(sorcfile);// 打开要输出的文件。即待压缩文件
			System.out.println("开始压缩");
			ZipFilesMethod(sorcfile.getParent() + File.separator, sorcfile.getName(),
					zipout);// 开始压缩
			zipout.flush();
			zipout.finish();
			zipout.close();
			System.out.println("压缩完成");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 压缩文件，文件夹
	 * 
	 * @param floderstring
	 *            待压缩文件的父路径
	 * @param filestring
	 *            待压缩文件的文件名
	 * @param zipoutstring
	 *            压缩输出流
	 */
	private static void ZipFilesMethod(String floderstring, String filestring,
			ZipOutputStream zipoutstring) {
		if (zipoutstring == null) {// 如果输出流为空，则没有进行压缩，直接返回
			return;
		}
		File file = new File(floderstring + filestring);
		try {
			if (file.isFile()) {// 判断是否是文件
				ZipEntry zipentry = new ZipEntry(filestring);
				FileInputStream inputstream = new FileInputStream(file);
				zipoutstring.putNextEntry(zipentry);
				int length;
				byte[] buffer = new byte[4096];

				while ((length = inputstream.read(buffer)) != -1) {
					zipoutstring.write(buffer, 0, length);// 写入
					zipoutstring.flush();
				}
				zipoutstring.closeEntry();
				inputstream.close();
			} else {
				// 文件夹的方式,获取文件夹下的子文件
				String fileList[] = file.list();

				// 如果没有子文件, 则添加进去即可
				if (fileList.length <= 0) {
					ZipEntry zipEntry = new ZipEntry(filestring
							+ File.separator);
					zipoutstring.putNextEntry(zipEntry);
					zipoutstring.closeEntry();
				}

				// 如果有子文件, 遍历子文件
				for (int i = 0; i < fileList.length; i++) {
					ZipFilesMethod(floderstring, filestring + File.separator
							+ fileList[i], zipoutstring);
				}// end of for
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 解压文件的方法
	 * 
	 * @param zipfile
	 *            压缩文件的路径(待解压文件的路劲)
	 * @param outfile
	 *            指定路径(解压后文件的输出路径)
	 */
	public static void UnZipFolderMethod(String zipfile, String outfile) {
		try {
			ZipInputStream instream = new ZipInputStream(new FileInputStream(
					zipfile));// 获取待解压文件的输入流对象
			ZipEntry zipEntry;
			String szName = "";
			System.out.println("开始解压");
			while ((zipEntry = instream.getNextEntry()) != null) {// 循环读取zip包里面的文件
				szName = zipEntry.getName();
				szName.trim();
				if (zipEntry.isDirectory()) {// 判断是否为文件夹（目录）
					// szName = szName.substring(0, szName.length() - 1);
					File folder = new File(outfile + File.separator + szName);
					if (!folder.exists()) {
						folder.mkdirs();// 创建文件夹
					}
				} else {
					String name = (outfile + File.separator + szName).trim();
					File file = new File(name);
					File xxs = new File(file.getParent());
					if (!xxs.exists()) {// 判断父目录是否存在
						xxs.mkdirs();// 不存在则创建所有的父目录文件夹
					}
					file.createNewFile();
					FileOutputStream out = new FileOutputStream(file);
					int len;
					byte[] buffer = new byte[1024];
					// read (len) bytes into buffer
					while ((len = instream.read(buffer)) != -1) {
						// write (len) byte from buffer at the position 0
						out.write(buffer, 0, len);
						out.flush();
					}
					out.close();
					instream.close();
				}
			}
			System.out.println("解压完成");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
