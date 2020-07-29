package com.cflip.mirrorbot.command;

import com.cflip.mirrorbot.ChainManager;
import com.cflip.mirrorbot.MirrorBot;
import discord4j.core.object.entity.Message;
import discord4j.rest.util.Color;

import java.time.Instant;

public class HelpCommand implements Command {
	@Override
	public void run(Message message, MirrorBot.Config config, ChainManager chainManager) {
		message.getChannel().subscribe(channel -> channel.createEmbed(embed -> embed
			.setAuthor("MirrorBot Help/Info", "https://github.com/cflip/MirrorBot", null)
			.setDescription(
				"**MirrorBot** uses words in your server's text chat to come up with new messages.\n\n" +
				"Mention MirrorBot in your message to get a reply that might sound like what members " +
				"of your server would say in that channel.\n\nMirrorBot can also be configured to " +
				"have a random chance of replying whenever someone sends a message."
			)
			.setFooter("Enjoy!", null)
			.setTimestamp(Instant.now()) // Looks cool to have the time there even though it's not really useful
			.setColor(Color.BISMARK)
		).block());
	}
}
