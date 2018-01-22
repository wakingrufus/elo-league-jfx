package com.github.wakingrufus.eloleague.player

import com.github.wakingrufus.eloleague.league.LeagueModel
import tornadofx.*

class PlayerView : Fragment("Edit Player") {

    val playerModel: PlayerModel by inject()
    val leagueModel: LeagueModel by inject()

    override val root = vbox {
        minHeight = 10.em.value
        form {
            fieldset("Edit Player") {
                field("Name") {
                    textfield(playerModel.name)
                }
                hbox {
                    button("Save") {
                        enableWhen(playerModel.dirty.and(playerModel.valid))
                        action {
                            playerModel.commit()
                            if (leagueModel.players.value.none { it.id == playerModel.id.value }) {
                                leagueModel.players.value.addAll(playerModel.item)
                            }
                            close()
                        }
                    }
                    button("Cancel") {
                        action {
                            playerModel.rollback()
                            close()
                        }
                    }
                }
            }
        }

    }
}