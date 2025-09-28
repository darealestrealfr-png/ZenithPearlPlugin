package com.example.zenithpearl.command;

import com.zenith.command.Command;
import com.zenith.command.CommandContext;
import com.zenith.command.CommandUsage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import static com.example.zenithpearl.ZenithPearlPlugin.PLUGIN_CONFIG;

public class PearlStatusCommand extends Command {

    @Override
    public String getName() {
        return "pearlstatus";
    }

    @Override
    public @CommandUsage String getUsage() {
        return "/pearlstatus [add|remove] [player]";
    }

    @Override
    public @CommandUsage String getDescription() {
        return "Shows pearl loading status and manages authorized players.";
    }

    @Override
    public boolean isAuthorized(final CommandContext context) {
        String playerName = context.getSender().getUsername();
        return PLUGIN_CONFIG.authorizedPlayers.contains(playerName);
    }

    @Override
    public Component execute(final CommandContext context) {
        if (!isAuthorized(context)) {
            return Component.text("You are not authorized to use this command.", NamedTextColor.RED);
        }

        String[] args = context.getArgs();

        if (args.length == 0) {
            // Show status
            return showStatus();
        }

        if (args.length >= 2) {
            String action = args[0].toLowerCase();
            String playerName = args[1];

            switch (action) {
                case "add":
                    return addAuthorizedPlayer(playerName);
                case "remove":
                    return removeAuthorizedPlayer(playerName);
                default:
                    return Component.text("Invalid action. Use 'add' or 'remove'.", NamedTextColor.RED);
            }
        }

        return Component.text("Usage: " + getUsage(), NamedTextColor.YELLOW);
    }

    private Component showStatus() {
        Component component = Component.text("=== Pearl Loading Status ===\n", NamedTextColor.GOLD);

        component = component.append(Component.text("Auto-load on unknown players: ", NamedTextColor.WHITE))
                            .append(Component.text(PLUGIN_CONFIG.autoLoadOnUnknownPlayer ? "ENABLED" : "DISABLED",
                                    PLUGIN_CONFIG.autoLoadOnUnknownPlayer ? NamedTextColor.GREEN : NamedTextColor.RED))
                            .append(Component.text("\n"));

        component = component.append(Component.text("Discord notifications: ", NamedTextColor.WHITE))
                            .append(Component.text(PLUGIN_CONFIG.discordNotifications ? "ENABLED" : "DISABLED",
                                    PLUGIN_CONFIG.discordNotifications ? NamedTextColor.GREEN : NamedTextColor.RED))
                            .append(Component.text("\n"));

        component = component.append(Component.text("Max pearls per load: ", NamedTextColor.WHITE))
                            .append(Component.text(String.valueOf(PLUGIN_CONFIG.maxPearlsPerLoad), NamedTextColor.AQUA))
                            .append(Component.text("\n"));

        component = component.append(Component.text("Authorized players: ", NamedTextColor.WHITE));
        if (PLUGIN_CONFIG.authorizedPlayers.isEmpty()) {
            component = component.append(Component.text("None", NamedTextColor.GRAY));
        } else {
            component = component.append(Component.text(String.join(", ", PLUGIN_CONFIG.authorizedPlayers), NamedTextColor.AQUA));
        }

        return component;
    }

    private Component addAuthorizedPlayer(String playerName) {
        if (PLUGIN_CONFIG.authorizedPlayers.contains(playerName)) {
            return Component.text("Player '" + playerName + "' is already authorized.", NamedTextColor.YELLOW);
        }

        PLUGIN_CONFIG.authorizedPlayers.add(playerName);

        // Update the visual range module with the new known player
        VisualRangePearlModule visualRange = getVisualRangeModule();
        if (visualRange != null) {
            visualRange.addKnownPlayer(playerName);
        }

        return Component.text("Added '" + playerName + "' to authorized players.", NamedTextColor.GREEN);
    }

    private Component removeAuthorizedPlayer(String playerName) {
        if (!PLUGIN_CONFIG.authorizedPlayers.contains(playerName)) {
            return Component.text("Player '" + playerName + "' is not authorized.", NamedTextColor.YELLOW);
        }

        PLUGIN_CONFIG.authorizedPlayers.remove(playerName);

        // Update the visual range module
        VisualRangePearlModule visualRange = getVisualRangeModule();
        if (visualRange != null) {
            visualRange.removeKnownPlayer(playerName);
        }

        return Component.text("Removed '" + playerName + "' from authorized players.", NamedTextColor.GREEN);
    }

    private VisualRangePearlModule getVisualRangeModule() {
        // This would need to access the module manager
        return null; // Placeholder
    }
}
