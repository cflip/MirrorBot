const Discord = require("discord.js");
const client = new Discord.Client();

const {token} = require("./token.json");

// Send a message to all servers when bot comes online
client.on("ready", () => {
	client.guilds.cache.forEach(guild => {
		// Get the 'general' channel if one exists
		const general = guild.channels.cache.find(ch => ch.name === "general");
		if (!general) return;
	
		general.send("MirrorBot is now online!");
	});
});

client.login(token);