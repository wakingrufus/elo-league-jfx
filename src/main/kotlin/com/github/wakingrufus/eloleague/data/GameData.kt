package com.github.wakingrufus.eloleague.data

data class GameData(
        val id: String,
        val team1Score: Int,
        val team2Score: Int,
        val timestamp: Long,
        val team1PlayerIds: List<String>,
        val team2PlayerIds: List<String>
)