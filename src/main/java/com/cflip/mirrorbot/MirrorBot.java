package com.cflip.mirrorbot;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.channel.MessageChannel;

public class MirrorBot {
	public static void main(String[] args) {
		// Get token from Heroku config.
		String token = System.getenv().get("TOKEN");
		// If there is no token variable then use the first argument.
		if (token == null) token = args[0];

		GatewayDiscordClient client = DiscordClientBuilder.create(token).build().login().block();
		ChainManager chainManager = new ChainManager();

		client.getEventDispatcher().on(MessageCreateEvent.class)
				.map(MessageCreateEvent::getMessage)
				.filter(message -> !message.getAuthor().map(user -> user.equals(client.getSelf().block())).orElse(false))
				.subscribe(message -> {
					chainManager.addWords(message);

					if (Math.random() < 0.15) {
						MessageChannel channel = message.getChannel().block();
						String msg = chainManager.createMessage(channel);
						if (msg != null) channel.createMessage(msg).block();
					}
				});

		client.onDisconnect().block();
	}
}