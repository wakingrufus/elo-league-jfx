package com.github.wakingrufus.eloleague.ui

import tornadofx.ItemViewModel
import tornadofx.toProperty


class LeagueModel2 : ItemViewModel<LeagueItem>() {
    val id = bind(LeagueItem::id)
    val name = bind(LeagueItem::name)
    val startingRating = bind(LeagueItem::startingRating)
    val xi = bind(LeagueItem::xi)
    val kFactorBase = bind(LeagueItem::kFactorBase)
    val trialPeriod = bind(LeagueItem::trialPeriod)
    val trialKFactorMultiplier = bind(LeagueItem::trialKFactorMultiplier)
    val teamSize = bind(LeagueItem::teamSize)

}


class LeagueModel: ItemViewModel<LeagueItem>() {
    var id = bind {item?.idProperty}
    var name = bind{item?.nameProperty}
    var startingRating = bind{item?.startingRatingProperty}
    var xi = bind{item?.xiProperty}
    var kFactorBase = bind{item?.kFactorBaseProperty}
    var trialPeriod = bind{item?.trialPeriodProperty}
    var trialKFactorMultiplier = bind{item?.trialKFactorMultiplierProperty}
    var teamSize = bind{item?.teamSizeProperty}

}
