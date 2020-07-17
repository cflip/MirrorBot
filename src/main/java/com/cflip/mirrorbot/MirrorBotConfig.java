package com.cflip.mirrorbot;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class MirrorBotConfig {
	public float messageChance;
	public List<String> blacklist;

	public MirrorBotConfig() {
		messageChance = 0;
		blacklist = new ArrayList<>();
	}

	public MirrorBotConfig(String filePath) throws FileNotFoundException {
		JsonReader reader = new JsonReader(new FileReader(new File(filePath)));
		Gson gson = new Gson();
		gson.fromJson(reader, MirrorBotConfig.class);
	}
}
