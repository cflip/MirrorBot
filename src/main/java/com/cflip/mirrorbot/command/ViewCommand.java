package com.cflip.mirrorbot.command;

import com.cflip.mirrorbot.MirrorBot;
import discord4j.core.object.entity.Message;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ViewCommand implements Command {
	private static final Pattern pattern = Pattern.compile("<#\\d+>");

	@Override
	public void run(Message message, MirrorBot bot) {
		message.getChannel().subscribe(channel -> {
			Matcher matcher = pattern.matcher(message.getContent());
			if (matcher.find()) {
				String channelName = matcher.group();
				long channelId = Long.parseLong(channelName.replaceAll("\\D", ""));

				String channelMessage = bot.chainManager.createMessage(channelId);

				if (channelMessage.equals("")) {
					channel.createMessage(channelName + " came up with an empty message!").block();
				} else {
					channel.createMessage(channelName + ":\n" + channelMessage).block();
				}
			} else {
				channel.createMessage("Please write a channel name with a # in your message").block();
			}
		});
	}

	@Override
	public String getDescription() {
		return "Create a message based on what a different channel says";
	}
}
