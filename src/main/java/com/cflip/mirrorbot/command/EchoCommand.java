package com.cflip.mirrorbot.command;

import com.cflip.mirrorbot.MirrorBot;
import discord4j.core.object.entity.Message;

public class EchoCommand implements Command {
	@Override
	public void run(Message message, MirrorBot.Config config) {
		String msgString = message.getContent();

		message.getChannel().subscribe(channel -> {
			String response;
			try {
				response = msgString.substring(msgString.indexOf(' '));
			} catch (StringIndexOutOfBoundsException e) {
				response = "Please add some text for me to echo!";
			}

			channel.createMessage(response).block();
		});
	}
}
