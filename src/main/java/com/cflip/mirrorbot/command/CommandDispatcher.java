package com.cflip.mirrorbot.command;

import com.cflip.mirrorbot.MirrorBot;
import discord4j.core.object.entity.Message;

import java.util.HashMap;
import java.util.Map;

public class CommandDispatcher {
	private final Map<String, Command> commandMap = new HashMap<>();

	public void addCommand(String name, Command command) {
		commandMap.putIfAbsent(name, command);
	}

	public String getCommandListString(String prefix) {
		StringBuilder result = new StringBuilder();

		for (Map.Entry<String, Command> entry : commandMap.entrySet()) {
			String name = prefix + entry.getKey();
			String desc = entry.getValue().getDescription();

			result.append(name).append(": ").append(desc).append("\n");
		}

		return result.toString();
	}

	public void run(Message message, MirrorBot bot) {
		for (Map.Entry<String, Command> entry : commandMap.entrySet()) {
			if (message.getContent().startsWith(bot.config.prefix + entry.getKey())) {
				try {
					entry.getValue().run(message, bot);
				} catch (Exception e) {
					System.err.println("Command error");
					e.printStackTrace();
				}
				break;
			}
		}
	}
}
