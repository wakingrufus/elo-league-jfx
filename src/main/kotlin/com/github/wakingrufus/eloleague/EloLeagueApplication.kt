package com.github.wakingrufus.eloleague

import com.github.wakingrufus.eloleague.ui.MainView
import com.github.wakingrufus.eloleague.ui.styles.DefaultStyles
import javafx.scene.image.Image
import javafx.scene.text.Text
import javafx.stage.Stage
import mu.KLogging
import tornadofx.App

class EloLeagueApplication : App(MainView::class, DefaultStyles::class) {
    companion object : KLogging()

    override fun start(stage: Stage) {
        super.start(stage)
        val rootEm = Math.rint(Text().layoutBounds.height)

        stage.width = rootEm * 80
        stage.height = rootEm * 60
        stage.icons.add(Image(this.javaClass.getResourceAsStream("/images/avatar-default.png")))
    }
}
