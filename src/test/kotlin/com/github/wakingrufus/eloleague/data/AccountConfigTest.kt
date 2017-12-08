package com.github.wakingrufus.eloleague.data

import mu.KLogging
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*


class AccountConfigTest {
    companion object : KLogging()

    @Test
    fun testDataClass() {
        val config1 = LeagueData(id = UUID.randomUUID().toString(), name = "named")
        val config2 = config1.copy()
        assertEquals("objects are equal", config1, config2)
        assertEquals("hashcode is equal", config1.hashCode(), config2.hashCode())
        assertEquals("toString is equal", config1.toString(), config2.toString())

    }
}