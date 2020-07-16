package com.cflip.mirrorbot;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;

public class MirrorBot {
	public static void main(String[] args) {
		String token = System.getenv().get("TOKEN");
		if (token == null) token = args[0];

		GatewayDiscordClient client = DiscordClientBuilder.create(token).build().login().block();
	 	User self = client.getSelf().block();
		ChainManager chains = new ChainManager();
		chains.loadAll();

		client.getEventDispatcher().on(MessageCreateEvent.class)
			.map(MessageCreateEvent::getMessage)
			.filter(message -> !message.getAuthor().map(user -> user.equals(self)).orElse(false))
			.subscribe(message -> {
				MessageChannel channel = message.getChannel().block();
				long channelId = channel.getId().asLong();

				chains.add(channelId, message.getContent());

				if (Math.random() < 0.15 || message.getUserMentionIds().contains(self.getId())) {
					channel.createMessage(chains.createMessage(channelId)).block();
					chains.save(channelId);
				}
			});

		client.onDisconnect().block();
	}
}