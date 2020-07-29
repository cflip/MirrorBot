# MirrorBot
A Discord bot that replicates speech in your server.

## Configuration
MirrorBot is configured with a JSON file called ``config.json``.
``example-config.json`` shows what your config file should look like.

Explanation of config options:
- ``token`` - Your bot's token from the [Discord developer portal](https://discord.com/developers/applications)
- ``prefix`` - The prefix that should come before command names
- ``statusUpdateTime`` - Amount of time in minutes before the status should refresh
- ``messageChance`` - The chance MirrorBot will reply to a message on its own. 
This is a percentage ranging from 0 to 1, where 0 is 0%, and 1 is 100% 
- ``blacklist`` - An array of words and/or regular expressions that are ignored by MirrorBot

## Building and running
The releases page contains a pre-built jar to run, 
but if you want to compile from source code or make modifications you need to use Gradle.

You may need to run the command ``chmod +x ./gradlew`` for Gradle to work, depending on your operating system.
You also need JDK 8 or higher.

Then you can use
- ``./gradlew run`` to directly start and run the bot
- ``./gradlew jar`` to compile a runnable jar containing all dependencies

If there are any problems, please let me know by creating an issue!!!