package com.cflip.mirrorbot;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ChainManager {
	private final Map<Long, Chain> chainMap = new HashMap<>();

	public void add(long id, String words, List<String> blacklist) {
		if (!chainMap.containsKey(id)) {
			chainMap.put(id, new Chain());
		}

		String[] wordList = words.split(" ");
		for (String regex : blacklist) {
			wordList = Arrays.stream(wordList).filter(s -> !s.matches(regex)).toArray(String[]::new);
		}

		chainMap.get(id).add(wordList);
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

	public void saveAll() {
		for (long id : chainMap.keySet()) save(id);
	}

	public void loadAll(List<String> blacklist) {
		File dir = new File("cache/");
		if (dir.exists()) {
			File[] files = dir.listFiles();
			if (files == null) return;

			for (File file : files) {
				try {
					chainMap.put(ChainFileIO.getIdFromFileName(file), ChainFileIO.load(file, blacklist));
				} catch (IOException | ClassNotFoundException e) {
					System.err.println("Failed to read chain from file " + file.getAbsolutePath());
					e.printStackTrace();
				} catch (NumberFormatException e) {
					System.err.println("File name did not contain a number: " + file.getAbsolutePath());
				}
			}
		}
	}

	public String createMessage(long id) {
		return chainMap.containsKey(id) ? chainMap.get(id).createMessage() : "";
	}

	public String randomMessage() {
		if (chainMap.size() > 0) {
			Random random = new Random();
			List<Chain> chains = new ArrayList<>(chainMap.values());
			return chains.get(random.nextInt(chains.size())).createMessage();
		} else {
			return "";
		}
	}
}
