package com.github.wakingrufus.eloleague.swiss

import javafx.scene.text.FontWeight
import mu.KLogging
import tornadofx.*

class SwissPairingView : Fragment() {
    companion object : KLogging()

    val pairing: SwissPairingItem by param()


    override val root = vbox {
        children.bind(pairing.teamsProperty.value) { team ->
            hbox(spacing = 1.em.value) {
                label("${team.name} (${pairing.gamePoints(team.id)})") {
                    if (pairing.getWinningTeam() == team.id) {
                        style {
                            fontWeight = FontWeight.BOLD
                        }
                    }
                }
            }
        }
    }
}


