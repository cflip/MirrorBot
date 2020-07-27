package com.cflip.mirrorbot.command;

import com.cflip.mirrorbot.MirrorBot;
import discord4j.core.object.entity.Message;

public interface Command {
	void run(Message message, MirrorBot.Config config);
}
