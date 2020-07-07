package com.cflip.mirrorbot;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.channel.MessageChannel;

public class MirrorBot {
	public static void main(String[] args) {
		String token = System.getenv().get("TOKEN");
		if (token == null) token = args[0];

		GatewayDiscordClient client = DiscordClientBuilder.create(token).build().login().block();
		Chain chain = new Chain();

		client.getEventDispatcher().on(MessageCreateEvent.class)
			.map(MessageCreateEvent::getMessage)
			.filter(message -> !message.getAuthor().map(user -> user.equals(client.getSelf().block())).orElse(false))
			.subscribe(message -> {
				chain.add(message.getContent().split(" "));

				if (Math.random() < 0.15) {
					MessageChannel channel = message.getChannel().block();
					String msg = chain.createMessage(150);
					if (msg != null) channel.createMessage(msg).block();
				}
			});

		client.onDisconnect().block();
	}
}