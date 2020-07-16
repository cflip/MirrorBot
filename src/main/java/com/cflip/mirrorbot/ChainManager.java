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
			ChainFileIO.save(id, chainMap.get(id));
		} catch (IOException e) {
			System.err.println("Failed to save chain with id " + id);
			e.printStackTrace();
		}
	}

	public void loadAll() {
		File dir = new File("cache/");
		if (dir.exists()) {
			File[] files = dir.listFiles();
			if (files == null) return;

			for (File file : files) {
				try {
					chainMap.put(ChainFileIO.getIdFromFileName(file), ChainFileIO.load(file));
				} catch (IOException | ClassNotFoundException e) {
					System.err.println("Failed to read chain from file " + file.getAbsolutePath());
					e.printStackTrace();
				}
			}
		}
	}

	public String createMessage(long id) {
		return chainMap.containsKey(id) ? chainMap.get(id).createMessage() : "";
	}
}
