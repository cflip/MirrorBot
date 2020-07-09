package com.cflip.mirrorbot;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;

import java.util.HashMap;
import java.util.Map;

public class MirrorBot {
	public static void main(String[] args) {
		String token = System.getenv().get("TOKEN");
		if (token == null) token = args[0];

		GatewayDiscordClient client = DiscordClientBuilder.create(token).build().login().block();
	 	User self = client.getSelf().block();
		Map<Long, Chain> chainMap = new HashMap();

		client.getEventDispatcher().on(MessageCreateEvent.class)
			.map(MessageCreateEvent::getMessage)
			.filter(message -> !message.getAuthor().map(user -> user.equals(self)).orElse(false))
			.subscribe(message -> {
				MessageChannel channel = message.getChannel().block();
				long channelId = channel.getId().asLong();

				if (!chainMap.containsKey(channelId)) {
					chainMap.put(channelId, new Chain());
				}

				chainMap.get(channelId).add(message.getContent().split(" "));

				if (Math.random() < 0.15 || message.getUserMentionIds().contains(self.getId())) {
					String msg = chainMap.get(channelId).createMessage();
					if (msg != null) channel.createMessage(msg).block();
				}
			});

		client.onDisconnect().block();
	}
}