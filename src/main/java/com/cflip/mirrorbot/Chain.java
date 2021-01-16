package com.cflip.mirrorbot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Chain implements Serializable {
	private static final long serialVersionUID = 1;

	private final Map<String, ArrayList<String>> chain = new HashMap<>();
	private final ArrayList<Beginning> beginnings = new ArrayList<>();

	private static class Beginning implements Serializable {
		private static final long serialVersionUID = 2;

		public Beginning(String text, int msgLength) {
			this.text = text;
			this.msgLength = msgLength;
		}

		public String text;
		public int msgLength;
	}

	public void add(String[] words) {
		boolean first = true;

		for (int i = 0; i < words.length; i++) {
			String word = words[i];

			if (first) {
				beginnings.add(new Beginning(word, words.length));
				first = false;
			}

			if (!chain.containsKey(word)) chain.put(word, new ArrayList<>());

			String nextWord = null;
			if (i + 1 < words.length) nextWord = words[i + 1];
			if (nextWord != null) chain.get(word).add(nextWord);
		}
	}

	public void remove(String[] words) {
		for (String word : words) {
			chain.remove(word);
			beginnings.removeIf(beginning -> beginning.text.equals(word));
		}
	}

	public String createMessage() {
		Random random = new Random();

		if (beginnings.size() == 0 || chain.size() == 0) {
			System.out.println("Did not create message since there was not enough data.");
			return "";
		}

		Beginning beginning = beginnings.get(random.nextInt(beginnings.size()));
		String current = beginning.text;
		int wordCount = beginning.msgLength;
		StringBuilder result = new StringBuilder(current);

		for (int i = 0; i < wordCount; i++) {
			ArrayList<String> possibilities = chain.get(current);
			if (possibilities == null || possibilities.isEmpty()) continue;

			String next = possibilities.get(random.nextInt(possibilities.size()));
			if (next == null) continue;

			result.append(" ").append(next);
			current = next;
		}

		return result.toString();
	}
}
