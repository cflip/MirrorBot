package com.cflip.mirrorbot;

import com.cflip.mirrorbot.command.CommandDispatcher;
import com.cflip.mirrorbot.command.HelpCommand;
import com.cflip.mirrorbot.command.ViewCommand;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.event.domain.message.MessageDeleteEvent;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.object.presence.Activity;
import discord4j.discordjson.json.gateway.StatusUpdate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MirrorBot {
	public User self;
	public ChainManager chainManager;
	public Config config;

	public static class Config {
		public String token;
		public String prefix;
		public int statusUpdateTime;
		public float messageChance;
		public List<String> blacklist;
	}

	public MirrorBot(Config config) {
		this.config = config;

		GatewayDiscordClient client;
		try {
			client = DiscordClientBuilder.create(config.token).build().login().block();
		} catch (Exception e) {
			System.err.println("Could not start bot! Your token might be incorrect.");
			e.printStackTrace();
			return;
		}

		chainManager = new ChainManager();
		chainManager.loadAll();

		self = client.getSelf().block();

		CommandDispatcher commandDispatcher = new CommandDispatcher();
		commandDispatcher.addCommand("help", new HelpCommand());
		commandDispatcher.addCommand("view", new ViewCommand());

		ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
		scheduler.scheduleAtFixedRate(() -> client.updatePresence(getStatus()).block(), 1, config.statusUpdateTime, TimeUnit.SECONDS);

		client.getEventDispatcher().on(MessageCreateEvent.class)
			.map(MessageCreateEvent::getMessage)
			.filter(message -> !message.getAuthor().map(user -> user.equals(self)).orElse(false))
			.subscribe(message -> {
				MessageChannel channel = message.getChannel().block();
				if (message.getContent().startsWith(config.prefix)) {
					commandDispatcher.run(message, this);
				} else {
					long channelId = channel.getId().asLong();
					chainManager.add(channelId, message.getContent(), config.blacklist);

					if (Math.random() < config.messageChance || message.getUserMentionIds().contains(self.getId())) {
						String createdMessage = chainManager.createMessage(channelId);
						if (!createdMessage.equals("")) {
							channel.createMessage(createdMessage).block();
						}
						chainManager.save(channelId);
					}
				}
			});

		client.getEventDispatcher().on(MessageDeleteEvent.class)
			.map(MessageDeleteEvent::getMessage)
			.map(Optional::get)
			.subscribe(message -> chainManager.remove(message.getChannelId().asLong(), message.getContent()));

		client.onDisconnect().block();
	}

	public StatusUpdate getStatus() {
		return StatusUpdate.builder().status("MirrorBot status").afk(false).game(Activity.playing(chainManager.randomMessage())).build();
	}

	public static void main(String[] args) {
		try {
			JsonReader reader = new JsonReader(new FileReader(new File("config.json")));
			Gson gson = new Gson();
			Config config = gson.fromJson(reader, Config.class);
			reader.close();
			new MirrorBot(config);
		} catch (FileNotFoundException e) {
			System.err.println("Failed to find config.json file.");
			System.exit(-1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
