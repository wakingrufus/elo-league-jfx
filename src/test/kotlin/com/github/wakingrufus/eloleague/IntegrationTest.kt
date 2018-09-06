package com.github.wakingrufus.eloleague

import com.github.wakingrufus.eloleague.data.ConfigData
import com.github.wakingrufus.eloleague.dao.DataHandler
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import javafx.stage.Stage
import mu.KLogging
import org.junit.Test
import org.testfx.framework.junit.ApplicationTest

class IntegrationTest : ApplicationTest() {

    companion object : KLogging()

    override fun start(stage: Stage) {
        val config = ConfigData()
        val configHandler = mock<DataHandler> {
            on { readFileConfig() } doReturn config
        }
        EloLeagueApplication().start(stage)
    }

    @Test
    fun should_drag_file_into_trashcan() {
        /*
        // given:


        // when:
        clickOn("#newIdButton");

        // then:
        verifyThat("#desktop", hasChildren(0, ".file"));
        */
    }


}