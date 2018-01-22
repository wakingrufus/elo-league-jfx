package com.github.wakingrufus.eloleague.swiss

import com.github.wakingrufus.eloleague.league.LeagueModel
import com.github.wakingrufus.eloleague.player.PlayerListBuilder
import javafx.beans.property.ReadOnlyStringWrapper
import javafx.stage.StageStyle
import mu.KLogging
import tornadofx.*

class SwissView : Fragment("Swiss Tournament") {
    companion object : KLogging()

    val league: LeagueModel by inject()
    val tournament: SwissTournamentModel by param()
    val teamModel: SwissTeamModel by inject()

    init {
        tournament.teams.onChange { tournament.markDirty(tournament.teams) }
    }

    override val root = borderpane {
        center {
            form {
                fieldset("Edit Tournament") {
                    field("Name") {
                        textfield(tournament.name)
                    }

                    buttonbar {
                        button("Save") {
                            enableWhen(tournament.dirty.and(tournament.valid))
                            action {
                                tournament.commit()
                                if (league.tournaments.value.none { it.id == tournament.id.value }) {
                                    league.tournaments.value.addAll(tournament.item)
                                }
                                tournament.item = null
                                close()
                            }
                        }
                        button("Cancel") {
                            action {
                                tournament.rollback()
                                tournament.item = null
                                close()
                            }
                        }
                    }
                }
                fieldset("Teams") {
                    val teamsTable = tableview(tournament.teams) {
                        column("Name", SwissTeamItem::name)
                        column<SwissTeamItem, String>("Members") {
                            ReadOnlyStringWrapper(it.value.players.
                                    joinToString(transform = { player -> player.name }))
                        }
                        bindSelected(teamModel)
                        columnResizePolicy = SmartResize.POLICY
                    }
                    teamsTable.columns[0].sortType = javafx.scene.control.TableColumn.SortType.DESCENDING
                    teamsTable.columns[0].sortableProperty().set(true)
                    teamsTable.sortOrder.add(teamsTable.columns[0])
                    teamsTable.sort()
                    tournament.teams.value.onChange {
                        while (it.next()) {
                            if (!it.wasPermutated()) {
                                teamsTable.sort()
                            }
                        }
                    }

                    buttonbar {
                        button("New Team").setOnAction {
                            teamModel.rebind { item = SwissTeamItem() }
                            editTeam()
                        }
                        button("Edit Tournament").setOnAction {
                            editTeam()
                        }
                    }
                }

            }
        }
    }

    private fun editTeam() {
        find<SwissTeamView>().apply {
            openModal(stageStyle = StageStyle.UTILITY, block = true)
        }
    }
}


