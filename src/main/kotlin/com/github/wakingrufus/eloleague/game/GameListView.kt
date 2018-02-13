package com.github.wakingrufus.eloleague.game

import com.github.wakingrufus.eloleague.league.LeagueModel
import javafx.beans.property.ReadOnlyStringWrapper
import javafx.stage.StageStyle
import mu.KLogging
import tornadofx.*

class GameListView : Fragment("Games") {
    companion object : KLogging()

    val leagueModel: LeagueModel by inject()

    override val root = fieldset("Games") {
        val table = tableview(leagueModel.games) {
            column("Time", GameItem::timestamp)
            column<GameItem, String>("Team 1") {
                ReadOnlyStringWrapper(it.value.team1Players.joinToString(transform = { player -> player.name }))
            }
            column("Team 1 score", GameItem::team1ScoreProperty)
            column<GameItem, String>("Team 2") {
                ReadOnlyStringWrapper(it.value.team2Players.joinToString(transform = { player -> player.name }))
            }
            column("Team 2 score", GameItem::team2ScoreProperty)
            columnResizePolicy = SmartResize.POLICY
        }
        buttonbar {
            button("Copy Game") {
                enableWhen(table.selectionModel.selectedItemProperty().isNotNull)
                action {
                    editGame(GameItem().apply {
                        team1Players.setAll(table.selectedItem?.team1Players)
                        team2Players.setAll(table.selectedItem?.team2Players)
                    })
                }
            }
            button("Add Game").setOnAction {
                // gameModel.rebind { item = GameItem() }
                editGame(GameItem())
            }
            button("Edit Game") {
                enableWhen(table.selectionModel.selectedItemProperty().isNotNull)
                action {
                    editGame(table.selectedItem)
                }
            }
        }
    }


    private fun editGame(gameItem: GameItem?) {
        find<GameView>(mapOf(
                "gameItem" to gameItem,
                "league" to leagueModel.item
        )).apply {
            openModal(stageStyle = StageStyle.UTILITY, block = true)
        }
    }
}
