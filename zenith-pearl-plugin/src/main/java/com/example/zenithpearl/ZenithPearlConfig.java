package com.example.zenithpearl;

import com.zenith.cache.data.Player;
import java.util.HashSet;
import java.util.Set;

public class ZenithPearlConfig {
    // Auto-load pearls when unknown players enter visual range
    public boolean autoLoadOnUnknownPlayer = true;

    // List of authorized players who can request pearl loading via /msg
    public Set<String> authorizedPlayers = new HashSet<>();

    // Discord integration settings
    public boolean discordNotifications = true;
    public String discordChannelId = "";

    // Pearl loading settings
    public int pearlLoadDelay = 1000; // milliseconds between pearl throws
    public int maxPearlsPerLoad = 10;

    // Visual range settings
    public int visualRangeRadius = 64;

    public ZenithPearlConfig() {
        // Add some example authorized players
        authorizedPlayers.add("Notch");
        authorizedPlayers.add("jeb_");
    }
}
