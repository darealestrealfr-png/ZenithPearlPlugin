package com.example.zenithpearl;

import com.example.zenithpearl.discord.DiscordPearlHandler;
import com.zenith.event.discord.DiscordMessageEvent;
import com.zenith.plugin.api.Plugin;
import com.zenith.plugin.api.PluginAPI;
import com.zenith.plugin.api.ZenithProxyPlugin;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;

@Plugin(
    id = "zenith-pearl-plugin",
    version = BuildConstants.VERSION,
    description = "Advanced Pearl Loading Plugin for ZenithProxy",
    url = "https://github.com/example/ZenithPearlPlugin",
    authors = {"YourName"},
    mcVersions = {"1.21.4"}
)
public class ZenithPearlPlugin implements ZenithProxyPlugin {
    public static ZenithPearlConfig PLUGIN_CONFIG;
    public static ComponentLogger LOG;
    public static DiscordPearlHandler DISCORD_HANDLER;

    @Override
    public void onLoad(PluginAPI pluginAPI) {
        LOG = pluginAPI.getLogger();
        LOG.info("ZenithPearlPlugin loading...");

        // Initialize configuration
        PLUGIN_CONFIG = pluginAPI.registerConfig("zenith-pearl-plugin", ZenithPearlConfig.class);

        // Initialize Discord handler
        DISCORD_HANDLER = new DiscordPearlHandler();

        // Register modules
        pluginAPI.registerModule(new PearlLoaderModule());
        pluginAPI.registerModule(new VisualRangePearlModule());

        // Register commands
        pluginAPI.registerCommand(new LoadPearlCommand());
        pluginAPI.registerCommand(new PearlStatusCommand());

        // Register Discord event listener
        pluginAPI.getEventBus().subscribe(DiscordMessageEvent.class, DISCORD_HANDLER::onDiscordMessage);

        LOG.info("ZenithPearlPlugin loaded!");
    }
}
