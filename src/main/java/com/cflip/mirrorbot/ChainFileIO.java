package com.cflip.mirrorbot;

import java.io.*;
import java.util.List;

public class ChainFileIO {
	public static void save(long id, Chain chain) throws IOException {
		File file = new File("cache/" + id + ".dat");
		File dir = new File("cache/");

		if (!dir.exists() && !dir.mkdirs()) {
			System.err.println("Failed to create cache directory! " + file.getAbsolutePath());
			return;
		}

		if (!file.exists() && !file.createNewFile()) {
			System.err.println("Failed to create file at " + file.getAbsolutePath());
			return;
		}

		ObjectOutputStream outStream = new ObjectOutputStream(new FileOutputStream(file));
		outStream.writeObject(chain);
		outStream.close();
	}

	public static Chain load(File file, List<String> blacklist) throws IOException, ClassNotFoundException {
		ObjectInputStream inStream = new ObjectInputStream(new FileInputStream(file));
		Chain result = (Chain) inStream.readObject();
		inStream.close();

		result.remove(blacklist.toArray(new String[0]));

		return result;
	}

	public static long getIdFromFileName(File file) throws NumberFormatException {
		return Long.parseLong(file.getName().replaceFirst("[.][^.]+$", ""));
	}
}
