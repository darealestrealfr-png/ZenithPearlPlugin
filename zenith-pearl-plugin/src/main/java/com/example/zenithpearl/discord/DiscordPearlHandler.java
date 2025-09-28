package com.example.zenithpearl.discord;

import com.zenith.event.discord.DiscordMessageEvent;
import net.dv8tion.jda.api.entities.Message;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import static com.example.zenithpearl.ZenithPearlPlugin.LOG;
import static com.example.zenithpearl.ZenithPearlPlugin.PLUGIN_CONFIG;

public class DiscordPearlHandler {

    public void onDiscordMessage(DiscordMessageEvent event) {
        Message message = event.getMessage();
        String content = message.getContentRaw();

        if (!content.startsWith("!")) {
            return; // Not a command
        }

        String[] parts = content.substring(1).split("\\s+", 2);
        String command = parts[0].toLowerCase();
        String args = parts.length > 1 ? parts[1] : "";

        switch (command) {
            case "loadpearl":
                handleLoadPearlCommand(message, args);
                break;
            case "pearlstatus":
                handlePearlStatusCommand(message, args);
                break;
            case "addplayer":
                handleAddPlayerCommand(message, args);
                break;
            case "removeplayer":
                handleRemovePlayerCommand(message, args);
                break;
            default:
                // Not a pearl command, ignore
                break;
        }
    }

    private void handleLoadPearlCommand(Message message, String args) {
        // Check if user has permission (you might want to check Discord roles)
        if (!hasPermission(message)) {
            message.reply("You don't have permission to use this command.").queue();
            return;
        }

        int count = PLUGIN_CONFIG.maxPearlsPerLoad; // default

        if (!args.isEmpty()) {
            try {
                count = Integer.parseInt(args);
                count = Math.max(1, Math.min(count, PLUGIN_CONFIG.maxPearlsPerLoad));
            } catch (NumberFormatException e) {
                message.reply("Invalid number: " + args).queue();
                return;
            }
        }

        // Get the pearl loader and request loading
        PearlLoaderModule pearlLoader = getPearlLoader();
        if (pearlLoader != null) {
            pearlLoader.requestPearlLoad(count);

            String userName = message.getAuthor().getName();
            LOG.info("Discord user {} requested pearl load: {} pearls", userName, count);

            message.reply("Loading " + count + " pearls...").queue();

            // Also send to in-game if configured
            sendInGameNotification("Discord user " + userName + " requested pearl load: " + count + " pearls");
        } else {
            message.reply("Pearl loader is not available.").queue();
        }
    }

    private void handlePearlStatusCommand(Message message, String args) {
        if (!hasPermission(message)) {
            message.reply("You don't have permission to use this command.").queue();
            return;
        }

        StringBuilder status = new StringBuilder();
        status.append("**Pearl Loading Status**\n");
        status.append("Auto-load: ").append(PLUGIN_CONFIG.autoLoadOnUnknownPlayer ? "✅" : "❌").append("\n");
        status.append("Discord notifications: ").append(PLUGIN_CONFIG.discordNotifications ? "✅" : "❌").append("\n");
        status.append("Max pearls: ").append(PLUGIN_CONFIG.maxPearlsPerLoad).append("\n");
        status.append("Authorized players: ").append(String.join(", ", PLUGIN_CONFIG.authorizedPlayers));

        message.reply(status.toString()).queue();
    }

    private void handleAddPlayerCommand(Message message, String args) {
        if (!hasPermission(message)) {
            message.reply("You don't have permission to use this command.").queue();
            return;
        }

        if (args.isEmpty()) {
            message.reply("Usage: !addplayer <playername>").queue();
            return;
        }

        String playerName = args.trim();
        if (PLUGIN_CONFIG.authorizedPlayers.contains(playerName)) {
            message.reply("Player '" + playerName + "' is already authorized.").queue();
            return;
        }

        PLUGIN_CONFIG.authorizedPlayers.add(playerName);

        // Update visual range module
        VisualRangePearlModule visualRange = getVisualRangeModule();
        if (visualRange != null) {
            visualRange.addKnownPlayer(playerName);
        }

        message.reply("Added '" + playerName + "' to authorized players.").queue();
        LOG.info("Discord user {} added authorized player: {}", message.getAuthor().getName(), playerName);
    }

    private void handleRemovePlayerCommand(Message message, String args) {
        if (!hasPermission(message)) {
            message.reply("You don't have permission to use this command.").queue();
            return;
        }

        if (args.isEmpty()) {
            message.reply("Usage: !removeplayer <playername>").queue();
            return;
        }

        String playerName = args.trim();
        if (!PLUGIN_CONFIG.authorizedPlayers.contains(playerName)) {
            message.reply("Player '" + playerName + "' is not authorized.").queue();
            return;
        }

        PLUGIN_CONFIG.authorizedPlayers.remove(playerName);

        // Update visual range module
        VisualRangePearlModule visualRange = getVisualRangeModule();
        if (visualRange != null) {
            visualRange.removeKnownPlayer(playerName);
        }

        message.reply("Removed '" + playerName + "' from authorized players.").queue();
        LOG.info("Discord user {} removed authorized player: {}", message.getAuthor().getName(), playerName);
    }

    private boolean hasPermission(Message message) {
        // Check if the user has the required Discord role or is authorized
        // This would need to be implemented based on ZenithProxy's Discord role system
        return true; // Placeholder - implement proper permission checking
    }

    private PearlLoaderModule getPearlLoader() {
        // Access the module manager to get the PearlLoaderModule
        return null; // Placeholder
    }

    private VisualRangePearlModule getVisualRangeModule() {
        // Access the module manager to get the VisualRangePearlModule
        return null; // Placeholder
    }

    private void sendInGameNotification(String message) {
        // Send notification to in-game players
        // This would use ZenithProxy's chat system
        LOG.info("In-game notification: {}", message);
    }
}
