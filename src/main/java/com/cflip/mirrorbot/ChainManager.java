package com.cflip.mirrorbot;

import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;

import java.util.HashMap;
import java.util.Map;

public class ChainManager {
	private static final int MAX_WORDS = 150;
	private final Map<Long, Chain> chains = new HashMap<>();

	public void addWords(Message message) {
		long channelId = message.getChannelId().asLong();
		if (chains.containsKey(channelId)) {
			chains.get(channelId).addWords(message.getContent());
		} else {
			Chain newChain = new Chain();
			newChain.addWords(message.getContent());
			chains.put(channelId, newChain);
		}
	}

	public String createMessage(MessageChannel channel) {
		long channelId = channel.getId().asLong();
		String result = null;

		if (chains.containsKey(channelId)) {
			result = chains.get(channelId).createMessage(MAX_WORDS);
		}

		return result;
	}
}
