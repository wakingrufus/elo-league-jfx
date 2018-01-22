package com.github.wakingrufus.eloleague.game

import com.github.wakingrufus.eloleague.league.LeagueModel
import javafx.beans.property.ReadOnlyStringWrapper
import javafx.stage.StageStyle
import mu.KLogging
import tornadofx.*
import java.util.*

class GameListView : Fragment("Games") {
    companion object : KLogging()

    val gameModel: GameModel by inject()
    val leagueModel: LeagueModel by inject()

    override val root = fieldset("Games") {
        tableview(leagueModel.games) {
            column("Time", GameItem::timestamp)
            column<GameItem, String>("Team 1") {
                ReadOnlyStringWrapper(it.value.team1Players.joinToString(transform = { player -> player.name }))
            }
            column("Team 1 score", GameItem::team1ScoreProperty)
            column<GameItem, String>("Team 2") {
                ReadOnlyStringWrapper(it.value.team2Players.joinToString(transform = { player -> player.name }))
            }
            column("Team 2 score", GameItem::team2ScoreProperty)
            bindSelected(gameModel)
            columnResizePolicy = SmartResize.POLICY
        }
        buttonbar {
            button("Add Game").setOnAction {
                gameModel.rebind { item = GameItem(id = UUID.randomUUID().toString()) }
                editGame()
            }
            button("Edit Game").setOnAction {
                editGame()
            }
        }
    }

    private fun editGame() {
        find<GameView>().apply {
            openModal(stageStyle = StageStyle.UTILITY, block = true)
        }
    }
}
