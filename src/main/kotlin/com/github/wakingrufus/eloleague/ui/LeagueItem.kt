package com.github.wakingrufus.eloleague.ui

import com.github.wakingrufus.elo.League
import com.github.wakingrufus.eloleague.data.LeagueData
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.getProperty
import tornadofx.getValue
import tornadofx.property
import tornadofx.setValue


val defaultLeague: League = League(teamSize = 1)

class LeagueItem(id: String,
                 name: String = "",
                 startingRating: Int = defaultLeague.startingRating,
                 xi: Int = defaultLeague.xi,
                 kFactorBase: Int = defaultLeague.kFactorBase,
                 trialPeriod: Int = defaultLeague.trialPeriod,
                 trialKFactorMultiplier: Int = defaultLeague.trialKFactorMultiplier,
                 teamSize: Int = defaultLeague.teamSize) {
    val idProperty = SimpleStringProperty(this,"id",id)
    var id by idProperty

    val nameProperty = SimpleStringProperty(this,"name",name)
    var name by nameProperty

    val startingRatingProperty = SimpleIntegerProperty()
    var startingRating by startingRatingProperty

    val xiProperty = SimpleIntegerProperty()
    var xi by xiProperty

    val kFactorBaseProperty = SimpleIntegerProperty()
    var kFactorBase by kFactorBaseProperty

    val trialPeriodProperty = SimpleIntegerProperty()
    var trialPeriod by trialPeriodProperty

    val trialKFactorMultiplierProperty = SimpleIntegerProperty()
    var trialKFactorMultiplier by trialKFactorMultiplierProperty

    val teamSizeProperty = SimpleIntegerProperty()
    var teamSize by teamSizeProperty

}

class LeagueItem3 {
    val idProperty = SimpleStringProperty()
    var id by idProperty

    val nameProperty = SimpleStringProperty()
    var name by nameProperty

    val startingRatingProperty = SimpleIntegerProperty()
    var startingRating by startingRatingProperty

    val xiProperty = SimpleIntegerProperty()
    var xi by xiProperty

    val kFactorBaseProperty = SimpleIntegerProperty()
    var kFactorBase by kFactorBaseProperty

    val trialPeriodProperty = SimpleIntegerProperty()
    var trialPeriod by trialPeriodProperty

    val trialKFactorMultiplierProperty = SimpleIntegerProperty()
    var trialKFactorMultiplier by trialKFactorMultiplierProperty

    val teamSizeProperty = SimpleIntegerProperty()
    var teamSize by teamSizeProperty

}

class LeagueItem2(id: String,
                  name: String = "",
                  startingRating: Int = defaultLeague.startingRating,
                  xi: Int = defaultLeague.xi,
                  kFactorBase: Int = defaultLeague.kFactorBase,
                  trialPeriod: Int = defaultLeague.trialPeriod,
                  trialKFactorMultiplier: Int = defaultLeague.trialKFactorMultiplier,
                  teamSize: Int = defaultLeague.teamSize) {
    var id by property(id)
    fun idProperty() = getProperty(LeagueItem::id)

    var name by property(name)
    fun nameProperty() = getProperty(LeagueItem::name)

    var startingRating by property(startingRating)
    fun startingRatingProperty() = getProperty(LeagueItem::startingRating)

    var xi by property(xi)
    fun xiProperty() = getProperty(LeagueItem::xi)
    var kFactorBase by property(kFactorBase)
    fun kFactorBaseProperty() = getProperty(LeagueItem::kFactorBase)
    var trialPeriod by property(trialPeriod)
    fun trialPeriodProperty() = getProperty(LeagueItem::trialPeriod)
    var trialKFactorMultiplier by property(trialKFactorMultiplier)
    fun trialKFactorMultiplierProperty() = getProperty(LeagueItem::trialKFactorMultiplier)
    var teamSize by property(teamSize)
    fun teamSizeProperty() = getProperty(LeagueItem::teamSize)

}

fun toData(leagueItem: LeagueItem): LeagueData {
    return LeagueData(
            id = leagueItem.id,
            name = leagueItem.name,
            startingRating = leagueItem.startingRating,
            xi = leagueItem.xi,
            kFactorBase = leagueItem.kFactorBase,
            trialPeriod = leagueItem.trialPeriod,
            trialKFactorMultiplier = leagueItem.trialKFactorMultiplier,
            teamSize = leagueItem.teamSize
    )
}
fun fromData(leagueData: LeagueData): LeagueItem {
    val item = LeagueItem(
            id = leagueData.id,
            name = leagueData.name
    )
    item.startingRatingProperty.set(leagueData.startingRating)
    item.xiProperty.set(leagueData.xi)
    item.kFactorBaseProperty.set(leagueData.kFactorBase)
    item.trialPeriodProperty.set(leagueData.trialPeriod)
    item.trialKFactorMultiplierProperty.set(leagueData.trialKFactorMultiplier)
    item.teamSizeProperty.set(leagueData.teamSize)
    return item
}

fun fromData2(leagueData: LeagueData): LeagueItem3 {
    val item = LeagueItem3()
    item.idProperty.set(leagueData.id)
    item.nameProperty.set(leagueData.name)
    item.startingRatingProperty.set(leagueData.startingRating)
    item.xiProperty.set(leagueData.xi)
    item.kFactorBaseProperty.set(leagueData.kFactorBase)
    item.trialPeriodProperty.set(leagueData.trialPeriod)
    item.trialKFactorMultiplierProperty.set(leagueData.trialKFactorMultiplier)
    item.teamSizeProperty.set(leagueData.teamSize)
    return item
}
