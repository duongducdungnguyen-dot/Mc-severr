package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.GameMode
import com.example.model.Player
import com.example.model.PluginItem
import com.example.model.ServerConfig
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

class ServerViewModel : ViewModel() {
    private val _isServerOnline = MutableStateFlow(false)
    val isServerOnline = _isServerOnline.asStateFlow()

    private val _cpuUsage = MutableStateFlow(0f)
    val cpuUsage = _cpuUsage.asStateFlow()

    private val _ramUsage = MutableStateFlow(0f)
    val ramUsage = _ramUsage.asStateFlow()

    private val _players = MutableStateFlow<List<Player>>(emptyList())
    val players = _players.asStateFlow()

    private val _plugins = MutableStateFlow<List<PluginItem>>(emptyList())
    val plugins = _plugins.asStateFlow()

    private val _serverConfig = MutableStateFlow(ServerConfig())
    val serverConfig = _serverConfig.asStateFlow()

    private val _consoleLogs = MutableStateFlow<List<String>>(emptyList())
    val consoleLogs = _consoleLogs.asStateFlow()

    init {
        // Initial Mock Data
        _plugins.value = listOf(
            PluginItem("1", "EssentialsPE", "Core commands and features", "2.0.1", true),
            PluginItem("2", "EconomyPlus", "Server economy system", "1.5.0", false),
            PluginItem("3", "WorldEdit", "In-game map editor", "1.1.0", true),
            PluginItem("4", "AntiCheat", "Prevents common hacks", "3.2", false)
        )

        // Mock Server Performance Loop
        viewModelScope.launch {
            while (true) {
                if (_isServerOnline.value) {
                    _cpuUsage.value = Random.nextFloat() * 40f + 10f // 10% to 50%
                    _ramUsage.value = Random.nextFloat() * 30f + 40f // 40% to 70%
                    
                    // Randomly adjust player pings
                    _players.update { list ->
                        list.map { it.copy(ping = (it.ping + Random.nextInt(-10, 11)).coerceIn(10, 300)) }
                    }
                } else {
                    _cpuUsage.value = 0f
                    _ramUsage.value = 0f
                }
                delay(2000)
            }
        }
    }

    fun toggleServerStatus() {
        viewModelScope.launch {
            if (_isServerOnline.value) {
                logToConsole("Stopping server...")
                delay(1500)
                _isServerOnline.value = false
                _players.value = emptyList()
                logToConsole("Server stopped.")
            } else {
                logToConsole("Starting server on port 19132...")
                delay(2000)
                _isServerOnline.value = true
                logToConsole("Server started successfully. Version: ${_serverConfig.value.serverVersion}")
                
                // Add mock players after start
                delay(3000)
                _players.value = listOf(
                    Player("p1", "Steve", 45, GameMode.SURVIVAL),
                    Player("p2", "Alex", 20, GameMode.CREATIVE),
                    Player("p3", "Notch", 120, GameMode.SURVIVAL)
                )
                logToConsole("Steve joined the game.")
                logToConsole("Alex joined the game.")
            }
        }
    }

    fun sendCommand(command: String) {
        logToConsole("> $command")
        if (command == "stop") {
            toggleServerStatus()
        } else if (command == "list") {
            logToConsole("Online players: ${_players.value.joinToString { it.name }}")
        } else {
            logToConsole("Unknown command: $command")
        }
    }

    private fun logToConsole(message: String) {
        _consoleLogs.update { (it + message).takeLast(50) }
    }

    fun updatePlayerGameMode(playerId: String, gameMode: GameMode) {
        _players.update { list ->
            list.map { if (it.id == playerId) it.copy(gameMode = gameMode) else it }
        }
        val player = _players.value.find { it.id == playerId }
        logToConsole("Setted ${player?.name}'s game mode to ${gameMode.displayName}")
    }

    fun kickPlayer(playerId: String) {
        val player = _players.value.find { it.id == playerId }
        _players.update { list -> list.filter { it.id != playerId } }
        logToConsole("Kicked ${player?.name} from the server.")
    }

    fun togglePlugin(pluginId: String) {
        viewModelScope.launch {
            _plugins.update { list ->
                list.map {
                    if (it.id == pluginId) it.copy(isInstalled = !it.isInstalled) else it
                }
            }
            val plugin = _plugins.value.find { it.id == pluginId }
            val action = if (plugin?.isInstalled == true) "Installed" else "Uninstalled"
            logToConsole("$action ${plugin?.name}")
        }
    }

    fun updateConfig(config: ServerConfig) {
        _serverConfig.value = config
        logToConsole("Server configuration updated.")
    }
}
