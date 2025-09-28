# ZenithPearlPlugin

An advanced pearl loading plugin for ZenithProxy that provides automated pearl loading functionality with Discord integration.

## Features

- **Automatic Pearl Loading**: Automatically loads all pearls when unknown players enter visual range
- **Manual Pearl Loading**: Authorized players can request pearl loading via in-game commands
- **Discord Integration**: Receive notifications and control pearl loading through Discord
- **Configurable Settings**: Customize behavior through configuration files

## Installation

1. Build the plugin:
   ```bash
   ./gradlew build
   ```

2. Copy the built JAR from `build/libs/` to your ZenithProxy `plugins` folder

3. Restart ZenithProxy

## Configuration

The plugin creates a configuration file that can be edited to customize behavior:

```json
{
  "autoLoadOnUnknownPlayer": true,
  "authorizedPlayers": ["Player1", "Player2"],
  "discordNotifications": true,
  "discordChannelId": "your-discord-channel-id",
  "pearlLoadDelay": 1000,
  "maxPearlsPerLoad": 10,
  "visualRangeRadius": 64
}
```

## Commands

### In-Game Commands

- `/loadpearl [count]` - Request pearl loading (authorized players only)
- `/pearlstatus` - Show current status and authorized players
- `/pearlstatus add <player>` - Add a player to authorized list
- `/pearlstatus remove <player>` - Remove a player from authorized list

### Discord Commands

- `!loadpearl [count]` - Request pearl loading via Discord
- `!pearlstatus` - Show pearl loading status
- `!addplayer <player>` - Add authorized player
- `!removeplayer <player>` - Remove authorized player

## Modules

- **PearlLoader**: Core module that handles pearl loading mechanics
- **VisualRangePearl**: Monitors visual range and triggers automatic pearl loading

## Discord Setup

1. Ensure ZenithProxy has Discord bot integration configured
2. Set the `discordChannelId` in the configuration to your desired channel
3. The bot will send notifications when pearls are loaded automatically

## Usage

### Automatic Loading
When enabled, the plugin will automatically load pearls whenever an unknown player enters visual range. Authorized players are considered "known" and won't trigger automatic loading.

### Manual Loading
Authorized players can send a private message to the bot with `/loadpearl` to request pearl loading. The bot will load the specified number of pearls (or the maximum if no count is specified).

### Discord Control
Use Discord commands to remotely control and monitor pearl loading activity.

## Security

- Only authorized players can request pearl loading
- Configuration is stored securely and can be edited by server administrators
- Discord commands respect ZenithProxy's permission system

## Dependencies

- ZenithProxy 1.21.4+
- Java 21+

## Building from Source

```bash
./gradlew build
```

## License

This plugin is provided as-is for use with ZenithProxy.
