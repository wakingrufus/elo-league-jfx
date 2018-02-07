package com.github.wakingrufus.eloleague.player

import javafx.collections.ObservableList
import javafx.stage.StageStyle
import mu.KLogging
import tornadofx.*

class PlayerListBuilder : Fragment() {
    companion object : KLogging()

    val selectedPlayers: ObservableList<PlayerItem> by param()
    val allPlayers: ObservableList<PlayerItem> by param()
    val otherIneligiblePlayers: () -> List<String> by param()

    override val root = vbox {
        vbox {
            style {
                minHeight = 8.em
            }
            children.bind(selectedPlayers) { player: PlayerItem ->
                hbox {
                    label(player.name)
                    button("Remove") {
                        action {
                            selectedPlayers.remove(player)
                        }
                    }
                }
            }
        }
        button("Add Player").setOnAction {
            find<PlayerChooserView>(
                    mapOf("players" to allPlayers
                            .filter { lp -> !selectedPlayers.any { lp.id == it.id } }
                            .filter { lp -> lp.id !in otherIneligiblePlayers() }
                            .observable())
            ).apply {
                openModal(stageStyle = StageStyle.UTILITY, block = true)
            }.choice?.let { selectedPlayers.add(it) }
        }
    }
}