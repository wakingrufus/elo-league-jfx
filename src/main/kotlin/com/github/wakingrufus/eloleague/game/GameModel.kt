package com.github.wakingrufus.eloleague.game

import tornadofx.ItemViewModel

class GameModel : ItemViewModel<GameItem>() {
    var id = bind { item?.idProperty }
    var timestamp = bind { item?.timestampProperty }
    var team1Players = bind { item?.team1PlayersProperty }
    var team2Players = bind { item?.team2PlayersProperty }
    var team1Score = bind { item?.team1ScoreProperty }
    var team2Score = bind { item?.team2ScoreProperty }
}
