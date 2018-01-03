package com.github.wakingrufus.eloleague.league

import com.github.wakingrufus.eloleague.isValidInt
import mu.KLogging
import tornadofx.*

class LeagueSettingsView : View("League View") {
    companion object : KLogging()

    val model: LeagueModel by inject()

    override val root = fieldset("League Settings") {
        style {
            paddingAll = 6.px.value
        }
        field("Name") {
            textfield(model.name)
        }
        field("Starting Rating") {
            textfield(model.startingRating).validator {
                if (isValidInt(it)) null else error("must be numeric")
            }
        }
        field("xi") {
            textfield(model.xi).validator {
                if (isValidInt(it)) null else error("must be numeric")
            }
        }
        field("K-Factor Base") {
            textfield(model.kFactorBase).validator {
                if (isValidInt(it)) null else error("must be numeric")
            }
        }
        field("Trial Period") {
            textfield(model.trialPeriod).validator {
                if (isValidInt(it)) null else error("must be numeric")
            }
        }
        field("trialKFactorMultiplier") {
            textfield(model.trialKFactorMultiplier).validator {
                if (isValidInt(it)) null else error("must be numeric")
            }
        }
        buttonbar {
            button("Save") {
                enableWhen(model.dirty.and(model.valid))
                action {
                    model.commit()
                }
            }
            button("Cancel") {
                action {
                    model.rollback()
                }
            }
        }
    }

}
