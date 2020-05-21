const Discord = require("discord.js");
const client = new Discord.Client();

const {token} = require("./token.json");

var ngrams = {};
var beginnings = [];

function addToChain(string) {
	var words = string.split(" ");
	
	for (var i = 0; i < words.length; i++) {
		var gram = words[i];
		
		if (i==0) beginnings.push(gram);
		if (!ngrams[gram]) ngrams[gram] = [];

		ngrams[gram].push(words[i+1]);
	}
}

function getMessage(length) {
	var currentGram = beginnings[Math.floor(Math.random()*beginnings.length)];
	var result = currentGram;

	for (var i = 0; i < length; i++) {
		var possibilites = ngrams[currentGram];
		if (possibilites == undefined) continue;
		var next = possibilites[Math.floor(Math.random()*possibilites.length)];
		if (next == undefined) continue;
		result += " " + next;
		currentGram = next;
	}

	return result;
}

client.on("message", message => {
	if (message.member.user !== client.user) {
		addToChain(message.content);
		if (message.mentions.has(client.user)) message.channel.send(getMessage(100));
	}
});

client.login(token);