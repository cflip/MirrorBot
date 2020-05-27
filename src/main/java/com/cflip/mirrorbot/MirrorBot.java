package com.cflip.mirrorbot;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;

public class MirrorBot {
	public static void main(String[] args) {
		// Get token from Heroku config.
		String token = System.getenv().get("TOKEN");
		// If there is no token variable then use the first argument.
		if (token == null) token = args[0];

		GatewayDiscordClient client = DiscordClientBuilder.create(token).build().login().block();

		Chain chain = new Chain();

		client.getEventDispatcher().on(MessageCreateEvent.class)
				.map(MessageCreateEvent::getMessage)
				.filter(message -> !message.getAuthor().map(user -> user.equals(client.getSelf().block())).orElse(false))
				.subscribe(message -> {
					chain.addWords(message.getContent());
					message.getChannel().block().createMessage(chain.createMessage(150)).block();
				});

		client.onDisconnect().block();
	}
}