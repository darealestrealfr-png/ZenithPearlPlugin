package com.example.zenithpearl.module;

import com.zenith.cache.data.Player;
import com.zenith.event.module.ClientTickEvent;
import com.zenith.event.proxy.PlayerOnlineEvent;
import com.zenith.module.AbstractModule;
import com.zenith.module.ModuleCategory;

import java.util.HashSet;
import java.util.Set;

import static com.example.zenithpearl.ZenithPearlPlugin.LOG;
import static com.example.zenithpearl.ZenithPearlPlugin.PLUGIN_CONFIG;

public class VisualRangePearlModule extends AbstractModule {

    private final Set<String> knownPlayers = new HashSet<>();
    private final Set<String> recentlyDetectedPlayers = new HashSet<>();

    public VisualRangePearlModule() {
        super("VisualRangePearl", "Automatically loads pearls when unknown players enter visual range", ModuleCategory.COMBAT);
    }

    @Override
    public boolean shouldBeEnabledByDefault() {
        return false;
    }

    @Override
    public void onEnable() {
        LOG.info("VisualRangePearl module enabled");
        // Initialize known players list
        knownPlayers.addAll(PLUGIN_CONFIG.authorizedPlayers);
    }

    @Override
    public void onDisable() {
        LOG.info("VisualRangePearl module disabled");
        recentlyDetectedPlayers.clear();
    }

    @Override
    public void onPlayerOnline(final PlayerOnlineEvent event) {
        if (!isEnabled() || !PLUGIN_CONFIG.autoLoadOnUnknownPlayer) return;

        Player player = event.getPlayer();
        String playerName = player.getUsername();

        // Check if this is an unknown player
        if (!knownPlayers.contains(playerName) && !recentlyDetectedPlayers.contains(playerName)) {
            LOG.info("Unknown player {} entered visual range, triggering pearl load", playerName);
            recentlyDetectedPlayers.add(playerName);

            // Trigger pearl loading
            PearlLoaderModule pearlLoader = getPearlLoader();
            if (pearlLoader != null) {
                pearlLoader.requestPearlLoad(PLUGIN_CONFIG.maxPearlsPerLoad);

                if (PLUGIN_CONFIG.discordNotifications) {
                    sendDiscordAlert(playerName);
                }
            }
        }
    }

    @Override
    public void onClientTick(final ClientTickEvent event) {
        if (!isEnabled()) return;

        // Clear recently detected players after some time to allow re-detection
        // This prevents spam if the same unknown player stays in range
        recentlyDetectedPlayers.clear();
    }

    private PearlLoaderModule getPearlLoader() {
        // Get the PearlLoaderModule instance
        // In ZenithProxy, modules are typically accessed through the module manager
        return null; // This would need to be implemented to get the actual module instance
    }

    private void sendDiscordAlert(String playerName) {
        String message = String.format("🚨 Unknown player **%s** entered visual range! Loading pearls...", playerName);
        LOG.info("Discord alert: {}", message);

        // Discord integration would use ZenithProxy's Discord API
        // Example: discordBot.sendMessage(PLUGIN_CONFIG.discordChannelId, message);
    }

    public void addKnownPlayer(String playerName) {
        knownPlayers.add(playerName);
        LOG.info("Added {} to known players list", playerName);
    }

    public void removeKnownPlayer(String playerName) {
        knownPlayers.remove(playerName);
        LOG.info("Removed {} from known players list", playerName);
    }

    public boolean isKnownPlayer(String playerName) {
        return knownPlayers.contains(playerName);
    }
}
