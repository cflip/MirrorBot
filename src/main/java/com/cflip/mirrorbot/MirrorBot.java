package com.cflip.mirrorbot;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.event.domain.message.MessageDeleteEvent;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;

import java.util.Optional;

public class MirrorBot {
	private final User self;
	private final ChainManager chainManager;

	public MirrorBot(String token) {
		GatewayDiscordClient client = DiscordClientBuilder.create(token).build().login().block();

		chainManager = new ChainManager();
		chainManager.loadAll();

		self = client.getSelf().block();

		client.getEventDispatcher().on(MessageCreateEvent.class)
			.map(MessageCreateEvent::getMessage)
			.filter(message -> !message.getAuthor().map(user -> user.equals(self)).orElse(false))
			.subscribe(message -> {
				MessageChannel channel = message.getChannel().block();
				long channelId = channel.getId().asLong();

				chainManager.add(channelId, message.getContent());

				if (Math.random() < 0.15 || message.getUserMentionIds().contains(self.getId())) {
					channel.createMessage(chainManager.createMessage(channelId)).block();
					chainManager.save(channelId);
				}
			});

		client.getEventDispatcher().on(MessageDeleteEvent.class)
			.map(MessageDeleteEvent::getMessage)
			.map(Optional::get)
			.subscribe(message -> chainManager.remove(message.getChannelId().asLong(), message.getContent()));

		client.onDisconnect().block();
	}

	public static void main(String[] args) {
		String token = System.getenv().get("TOKEN");
		if (token == null && args.length > 0) {
			token = args[0];
		} else {
			System.err.println("Failed to get the bot user token!\nPlease set the environment variable 'TOKEN' or pass the token in as the first argument.");
			System.exit(-1);
		}

		new MirrorBot(token);
	}
}