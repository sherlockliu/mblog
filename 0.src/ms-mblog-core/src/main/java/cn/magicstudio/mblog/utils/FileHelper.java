package cn.magicstudio.mblog.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileHelper {
	public static void fileMove(String from, String to) throws Exception {
		try {
			File dir = new File(from);
			File[] files = dir.listFiles();
			if (files == null)
				return;
			File moveDir = new File(to);
			if (!moveDir.exists()) {
				moveDir.mkdirs();
			}
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					fileMove(files[i].getPath(), to + "\\" + files[i].getName());
					files[i].delete();
				}
				File moveFile = new File(moveDir.getPath() + "\\"
						+ files[i].getName());
				if (moveFile.exists()) {
					moveFile.delete();
				}
				files[i].renameTo(moveFile);
				System.out.println(files[i] + " 移动成功");
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public static void copyFileFromDir(String toPath, String fromPath) {
		File file = new File(fromPath);
		createFile(toPath, false);
		if (file.isDirectory()) {
			copyFileToDir(toPath, listFile(file, "*.*"));
		}
	}

	public static void copyDir(String toPath, String fromPath) {
		copyDir(toPath, fromPath, "*.*");
	}

	public static void copyDir(String toPath, String fromPath, String match) {
		File targetFile = new File(toPath);
		createFile(targetFile, false);
		File file = new File(fromPath);
		if ((targetFile.isDirectory()) && (file.isDirectory())) {
			copyFileToDir(targetFile.getAbsolutePath(), listFile(file, match));
		}
	}

	public static void copyFileToDir(String toDir, String[] filePath) {
		if ((toDir == null) || ("".equals(toDir))) {
			return;
		}
		File targetFile = new File(toDir);
		if (!targetFile.exists()) {
			targetFile.mkdir();
		} else if (!targetFile.isDirectory()) {
			return;
		}

		for (int i = 0; i < filePath.length; i++) {
			File file = new File(filePath[i]);
			if (file.isDirectory()) {
				copyFileToDir(toDir + "/" + file.getName(),
						listFile(file, "*.*"));
			} else {
				copyFileToDir(toDir, file, "");
			}
		}
	}

	public static void copyFileToDir(String toDir, File file, String newName) {
		String newFile = "";
		if ((newName != null) && (!"".equals(newName))) {
			newFile = toDir + "/" + newName;
		} else {
			newFile = toDir + "/" + file.getName();
		}
		File tFile = new File(newFile);
		copyFile(tFile, file);
	}

	public static void copyFile(File toFile, File fromFile) {
		if (toFile.exists()) {
			return;
		}
		createFile(toFile, true);

		try {
			InputStream is = new java.io.FileInputStream(fromFile);
			FileOutputStream fos = new FileOutputStream(toFile);
			byte[] buffer = new byte['Ѐ'];
			while (is.read(buffer) != -1) {
				fos.write(buffer);
			}
			is.close();
			fos.close();
		} catch (java.io.FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String[] listFile(File dir, String match) {
		java.io.FilenameFilter filter = new DirFilter(match);

		String absolutPath = dir.getAbsolutePath();
		String[] paths = dir.list(filter);
		String[] files = new String[paths.length];
		for (int i = 0; i < paths.length; i++) {
			files[i] = (absolutPath + "/" + paths[i]);
		}
		return files;
	}

	public static void createFile(String path, boolean isFile) {
		createFile(new File(path), isFile);
	}

	public static void createFile(File file, boolean isFile) {
		if (!file.exists()) {
			if (!file.getParentFile().exists()) {
				createFile(file.getParentFile(), false);
			} else if (isFile) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				file.mkdir();
			}
		}
	}

	static class DirFilter implements java.io.FilenameFilter {
		private String type;

		public DirFilter(String type) {
			this.type = type.toLowerCase();
		}

		public boolean accept(File dir, String name) {
			File f = new File(dir.getAbsolutePath() + File.separator + name);
			if (f.isDirectory())
				return false;
			if (this.type.equalsIgnoreCase("*.*"))
				return true;
			return name.toLowerCase().endsWith(this.type);
		}
	}
}
