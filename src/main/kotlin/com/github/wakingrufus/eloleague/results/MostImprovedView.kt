package com.github.wakingrufus.eloleague.results

import com.github.wakingrufus.eloleague.player.PlayerItem
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.ZonedDateTime

class MostImprovedView : Fragment() {
    val leagueResultItem: LeagueResultItem by param()
    var startDate = SimpleObjectProperty<LocalDate>(LocalDate.now())
    var endDate = SimpleObjectProperty<LocalDate>(LocalDate.now())
    var winner = SimpleStringProperty()
    override val root = borderpane {
        center {
            form {
                fieldset {
                    field {
                        label("Start Date")
                        datepicker {
                            bind(startDate)
                        }
                    }
                    field {
                        label("End Date")
                        datepicker {
                            bind(endDate)
                        }
                    }
                }
                textfield {
                    isEditable = false
                    bind(winner)
                }
            }

        }
        bottom {
            buttonbar {
                button("Calculate") {
                    action {
                        winner.value = leagueResultItem.games.asSequence()
                                .filter { ZonedDateTime.parse(it.entryDate).isAfter(startDate.value.atStartOfDay().atOffset(ZoneOffset.UTC).toZonedDateTime()) }
                                .filter { ZonedDateTime.parse(it.entryDate).isBefore(endDate.value.plusDays(1).atStartOfDay().atOffset(ZoneOffset.UTC).toZonedDateTime()) }
                                .fold(mapOf<PlayerItem, Int>()) { map, game ->
                                    map + (game.player to (map[game.player] ?: 0) + game.ratingAdjustment)
                                }.maxBy { it.value }?.let {
                                    "${it.key.name} (${it.value})"
                                }
                    }
                }
            }
        }
    }
}