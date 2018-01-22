package com.github.wakingrufus.eloleague.swiss

import com.github.wakingrufus.eloleague.league.LeagueModel
import com.github.wakingrufus.eloleague.player.PlayerListBuilder
import mu.KLogging
import tornadofx.*

class SwissTeamView : Fragment() {
    companion object : KLogging()

    val teamModel: SwissTeamModel by inject()
    val leagueModel: LeagueModel by inject()
    val tournament: SwissTournamentModel by inject()

    init {
        teamModel.players.onChange { teamModel.markDirty(teamModel.players) }
    }

    override val root = vbox {
        id = "player-choices"
        form {
            fieldset {
                field("Name") {
                    textfield(teamModel.name)
                }
                this += find<PlayerListBuilder>(mapOf(
                        "selectedPlayers" to teamModel.players.value,
                        "allPlayers" to leagueModel.players.value,
                        "otherIneligiblePlayers" to tournament.teams.value.flatMap { it.players }
                ))
            }
            buttonbar {
                button("Save") {
                    enableWhen(teamModel.dirty.and(teamModel.valid))
                    action {
                        teamModel.commit()
                        if (tournament.teams.value.none { it.id == teamModel.id.value }) {
                            tournament.teams.value.addAll(teamModel.item)
                        }
                        close()
                    }
                }
                button("Cancel") {
                    action {
                        teamModel.rollback()
                        close()
                    }
                }
            }
        }

    }
}