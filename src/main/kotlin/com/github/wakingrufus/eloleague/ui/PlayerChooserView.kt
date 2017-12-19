package com.github.wakingrufus.eloleague.ui

import com.github.wakingrufus.eloleague.player.PlayerItem
import javafx.collections.ObservableList
import mu.KLogging
import tornadofx.*

class PlayerChooserView : Fragment() {
    companion object : KLogging()

    val players: ObservableList<PlayerItem> by param()
    var choice: PlayerItem? = null

    override val root = vbox {
        id = "player-choices"
        children.bind(players) {
            button(it.name) {
                id = "account-choice-" + it.name
                action {
                    choice = it
                    close()
                }
            }
        }
    }
}