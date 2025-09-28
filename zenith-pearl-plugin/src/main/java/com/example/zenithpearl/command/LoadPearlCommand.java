package com.example.zenithpearl.command;

import com.zenith.command.Command;
import com.zenith.command.CommandContext;
import com.zenith.command.CommandUsage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import static com.example.zenithpearl.ZenithPearlPlugin.LOG;
import static com.example.zenithpearl.ZenithPearlPlugin.PLUGIN_CONFIG;

public class LoadPearlCommand extends Command {

    @Override
    public String getName() {
        return "loadpearl";
    }

    @Override
    public @CommandUsage String getUsage() {
        return "/loadpearl [count]";
    }

    @Override
    public @CommandUsage String getDescription() {
        return "Requests the bot to load pearls. Authorized players only.";
    }

    @Override
    public boolean isAuthorized(final CommandContext context) {
        // Check if the player is authorized
        String playerName = context.getSender().getUsername();
        return PLUGIN_CONFIG.authorizedPlayers.contains(playerName);
    }

    @Override
    public Component execute(final CommandContext context) {
        if (!isAuthorized(context)) {
            return Component.text("You are not authorized to use this command.", NamedTextColor.RED);
        }

        String[] args = context.getArgs();
        int count = PLUGIN_CONFIG.maxPearlsPerLoad; // default

        if (args.length > 0) {
            try {
                count = Integer.parseInt(args[0]);
                count = Math.max(1, Math.min(count, PLUGIN_CONFIG.maxPearlsPerLoad));
            } catch (NumberFormatException e) {
                return Component.text("Invalid number: " + args[0], NamedTextColor.RED);
            }
        }

        // Get the pearl loader module and request loading
        PearlLoaderModule pearlLoader = getPearlLoader();
        if (pearlLoader != null) {
            pearlLoader.requestPearlLoad(count);

            String playerName = context.getSender().getUsername();
            LOG.info("Player {} requested pearl load: {} pearls", playerName, count);

            if (PLUGIN_CONFIG.discordNotifications) {
                sendDiscordNotification(playerName, count);
            }

            return Component.text("Loading " + count + " pearls...", NamedTextColor.GREEN);
        } else {
            return Component.text("Pearl loader module not available.", NamedTextColor.RED);
        }
    }

    private PearlLoaderModule getPearlLoader() {
        // This would need to access the module manager to get the PearlLoaderModule instance
        return null; // Placeholder - actual implementation needed
    }

    private void sendDiscordNotification(String playerName, int count) {
        String message = String.format("💎 Player **%s** requested pearl load: %d pearls", playerName, count);
        LOG.info("Discord notification: {}", message);
        // Discord integration would go here
    }
}
