package com.github.wakingrufus.eloleague.dao

import org.junit.Test
import kotlin.test.assertEquals

class JacksonUrlDataHandlerTest {
    @Test
    fun test() {
        val actual = JacksonUrlDataHandler(javaClass.classLoader.getResource("elo-test.json")).readFileConfig()
        assertEquals(expected = 1, actual = actual.leagues.size)
    }
}