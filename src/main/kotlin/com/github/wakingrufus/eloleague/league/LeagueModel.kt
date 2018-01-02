package com.github.wakingrufus.eloleague.league

import tornadofx.ItemViewModel

class LeagueModel : ItemViewModel<LeagueItem>() {
    var id = bind { item?.idProperty }
    var name = bind { item?.nameProperty }
    var startingRating = bind { item?.startingRatingProperty }
    var xi = bind { item?.xiProperty }
    var kFactorBase = bind { item?.kFactorBaseProperty }
    var trialPeriod = bind { item?.trialPeriodProperty }
    var trialKFactorMultiplier = bind { item?.trialKFactorMultiplierProperty }
    var players = bind { item?.playersProperty }
    var games = bind { item?.gamesProperty }

}
