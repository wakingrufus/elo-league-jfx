package com.github.wakingrufus.eloleague.game

import tornadofx.*

class GameModel : ItemViewModel<GameItem>() {
    var id = bind(GameItem::idProperty)
    var timestamp = bind(GameItem::timestampProperty)
    var team1Players = bind(GameItem::team1PlayersProperty)
    var team2Players = bind(GameItem::team2PlayersProperty)
    var team1Score = bind(GameItem::team1ScoreProperty)
    var team2Score = bind(GameItem::team2ScoreProperty)
}
