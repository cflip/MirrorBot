package com.cflip.mirrorbot.command;

import com.cflip.mirrorbot.ChainManager;
import com.cflip.mirrorbot.MirrorBot;
import discord4j.core.object.entity.Message;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ViewCommand implements Command {
	private static final Pattern pattern = Pattern.compile("<#\\d+>");

	@Override
	public void run(Message message, MirrorBot.Config config, ChainManager chainManager) {
		message.getChannel().subscribe(channel -> {
			Matcher matcher = pattern.matcher(message.getContent());
			if (matcher.find()) {
				String channelName = matcher.group();
				long channelId = Long.parseLong(channelName.replaceAll("\\D", ""));
				channel.createMessage(channelName + ":\n" + chainManager.createMessage(channelId)).block();
			} else {
				channel.createMessage("Please write a channel name with a # in your message").block();
			}
		});
	}
}
