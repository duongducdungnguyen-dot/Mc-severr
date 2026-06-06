package com.example.model

data class Player(
    val id: String,
    val name: String,
    val ping: Int,
    val gameMode: GameMode
)

enum class GameMode(val displayName: String) {
    SURVIVAL("Survival"),
    CREATIVE("Creative"),
    SPECTATOR("Spectator"),
    ADVENTURE("Adventure")
}

data class PluginItem(
    val id: String,
    val name: String,
    val description: String,
    val version: String,
    val isInstalled: Boolean
)

data class ServerConfig(
    val serverVersion: String = "1.20.51",
    val maxPlayers: Int = 10,
    val isAutoBackupEnabled: Boolean = true,
    val backupFrequencyHours: Int = 24,
    val mobileNotifications: Boolean = true
)
