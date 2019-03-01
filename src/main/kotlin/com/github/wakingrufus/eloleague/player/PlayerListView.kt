package com.github.wakingrufus.eloleague.player

import javafx.collections.ObservableList
import tornadofx.*

class PlayerListView : Fragment("Player View") {

    val players: ObservableList<PlayerItem> by param()
    val name: String by param()

    override val root = vbox {
        label(name)
        children.bind(players) { player ->
            hbox {
                label(player.name)
                button("Remove") {
                    action {
                        players.remove(player)
                    }
                }
            }
        }
    }
}