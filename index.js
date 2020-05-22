const Discord = require("discord.js");
const client = new Discord.Client();
const fs = require("fs");

const {token} = require("./token.json");
const {cacheDir, blacklist} = require("./config.json");

var ngrams = {};
var beginnings = [];

var chainPath = cacheDir + "chain.json";
var beginningPath = cacheDir + "beginnings.json";

function addToChain(string) {
	var words = string.split(" ");
	var first = true;

	for (var i = 0; i < words.length; i++) {
		var gram = words[i];

		if (blacklist.includes(gram)) continue;
			
		if (first) {
			beginnings.push(gram);
			first = false;
		}

		if (!ngrams[gram]) ngrams[gram] = [];

		var nextWord = words[i+1];
		if (!blacklist.includes(nextWord)) ngrams[gram].push(words[i+1]);
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

function save() {
	if (!fs.existsSync(cacheDir)) fs.mkdirSync(cacheDir);
	
	fs.writeFileSync(chainPath, JSON.stringify(ngrams));
	fs.writeFileSync(beginningPath, JSON.stringify(beginnings));
}

function load() {
	var chainData;
	var beginningData;
	
	try {
		chainData = JSON.parse(fs.readFileSync(chainPath));
		beginningData = JSON.parse(fs.readFileSync(beginningPath));
	} catch (err) {
		chainData = {};
		beginningData = [];
		return;
	}

	ngrams = chainData;
	beginnings = beginningData;
}

client.on("ready", () => {
	load(chainPath);
});

client.on("message", message => {
	if (message.member.user !== client.user) {
		addToChain(message.content);
		save();

		if (message.mentions.has(client.user)) {
			message.channel.send(getMessage(100));
		}
	}
});

client.on("messageDelete", message => {
	var words = string.split(" ");

	words.forEach(word => {
		if (ngrams[word]) delete ngrams[word];
		if (beginnings.includes(word)) delete beginnings[beginnings.indexOf(word)];
	});

	save();
});

client.login(token);