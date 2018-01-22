package com.github.wakingrufus.eloleague.player

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.stage.StageStyle
import mu.KLogging
import tornadofx.*

class PlayerListBuilder : Fragment() {
    companion object : KLogging()

    val selectedPlayers: ObservableList<PlayerItem> by param()
    val allPlayers: ObservableList<PlayerItem> by param()
    val otherIneligiblePlayers: ObservableList<PlayerItem> by param(FXCollections.observableArrayList())


    val choosePlayer: (allPlayers: ObservableList<PlayerItem>, selectedPlayers: ObservableList<PlayerItem>) -> PlayerItem? = { allPlayers, selectedPlayers ->
        find<PlayerChooserView>(
                mapOf("players" to allPlayers
                        .filter { lp -> !selectedPlayers.any { lp.id == it.id } }
                        .filter { lp -> !otherIneligiblePlayers.any{ lp.id == it.id} }
                        .observable())
        ).apply {
            openModal(stageStyle = StageStyle.UTILITY, block = true)
        }.choice
    }

    override val root =  vbox {
        vbox {
            style {
                minHeight = 8.em
            }
            children.bind(selectedPlayers) { player: PlayerItem ->
                field(player.name) {
                    button("Remove") {
                        action {
                            selectedPlayers.remove(player)
                        }
                    }
                }
            }
        }
        button("Add Player").setOnAction {
            selectedPlayers.add(choosePlayer(
                    allPlayers,
                    selectedPlayers))
        }
    }
}