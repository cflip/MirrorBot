package com.cflip.mirrorbot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Chain {
	private final Map<String, ArrayList<String>> chain = new HashMap<>();
	private final ArrayList<String> beginnings = new ArrayList<>();

	public void addWords(String message) {
		String[] words = message.split(" ");
		boolean first = true;

		for (int i = 0; i < words.length; i++) {
			String word = words[i];

			// In the future, the first word to be added might
			// not always be the first element in the words array
			if (first) {
				beginnings.add(word);
				first = false;
			}

			if (!chain.containsKey(word)) chain.put(word, new ArrayList<>());

			String nextWord = null;
			if (i + 1 < words.length) nextWord = words[i + 1];
			if (nextWord != null) chain.get(word).add(nextWord);
		}
	}

	public void removeWords(String message) {
		String[] words = message.split(" ");

		for (String word : words) {
			chain.remove(word);
			beginnings.remove(word);
		}
	}

	public String createMessage(int maxWords) {
		Random random = new Random();

		String current = beginnings.get(random.nextInt(beginnings.size()));
		StringBuilder result = new StringBuilder(current);

		for (int i = 0; i < maxWords; i++) {
			ArrayList<String> possibilities = chain.get(current);
			if (possibilities.isEmpty()) continue;

			String next = possibilities.get(random.nextInt(possibilities.size()));
			if (next == null) continue;

			result.append(" ").append(next);
			current = next;
		}

		return result.toString();
	}
}