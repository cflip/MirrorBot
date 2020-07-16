package com.cflip.mirrorbot;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ChainManager {
	private final Map<Long, Chain> chainMap = new HashMap<>();

	public void add(long id, String words) {
		if (!chainMap.containsKey(id)) {
			chainMap.put(id, new Chain());
		}

		chainMap.get(id).add(words.split(" "));
	}

	public void remove(long id, String words) {
		if (chainMap.containsKey(id)) {
			chainMap.get(id).remove(words.split(" "));
		}
	}

	public void save(long id) {
		try {
			File dir = new File("cache/");
			if (!dir.exists()) dir.mkdirs();

			File file = new File("cache/" + id + ".dat");
			if (!file.exists()) {
				if (!file.createNewFile() && file.mkdirs()) {
					System.err.println("Failed to create file");
					return;
				}
			}

			ObjectOutputStream outStream = new ObjectOutputStream(new FileOutputStream(file));
			outStream.writeObject(chainMap.get(id));
		} catch (IOException e) {
			System.err.println("Failed to save chain with id " + id);
			e.printStackTrace();
		}
	}

	public void loadAll() {
		File dir = new File("cache/");
		if (dir.exists()) {
			for (File file : dir.listFiles()) {
				long id = Long.parseLong(file.getName().replaceFirst("[.][^.]+$", ""));

				try {
					ObjectInputStream inStream = new ObjectInputStream(new FileInputStream(file));
					Chain chain = (Chain) inStream.readObject();
					chainMap.put(id, chain);
				} catch (IOException | ClassNotFoundException e) {
					System.err.println("Failed to read file " + id + " from cache");
					e.printStackTrace();
				}
			}
		}
	}

	public String createMessage(long id) {
		return chainMap.containsKey(id) ? chainMap.get(id).createMessage() : "";
	}
}
