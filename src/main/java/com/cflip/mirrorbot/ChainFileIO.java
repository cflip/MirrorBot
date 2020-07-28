package com.cflip.mirrorbot;

import java.io.*;

public class ChainFileIO {
	public static void save(long id, Chain chain) throws IOException {
		File file = new File("cache/" + id + ".dat");
		if (!file.exists() && !file.createNewFile()) {
			System.err.println("Failed to create file at " + file.getAbsolutePath());
			return;
		}

		ObjectOutputStream outStream = new ObjectOutputStream(new FileOutputStream(file));
		outStream.writeObject(chain);
		outStream.close();
	}

	public static Chain load(File file) throws IOException, ClassNotFoundException {
		ObjectInputStream inStream = new ObjectInputStream(new FileInputStream(file));
		Chain result = (Chain) inStream.readObject();
		inStream.close();
		return result;
	}

	public static long getIdFromFileName(File file) throws NumberFormatException {
		return Long.parseLong(file.getName().replaceFirst("[.][^.]+$", ""));
	}
}
