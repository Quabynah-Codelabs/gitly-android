package dev.gitly

import kotlinx.coroutines.delay
import org.junit.Test
import java.util.*

class GenerateIdsTest {

    @Test
    fun generateIds() {
        for (i in 0..5)
            println(UUID.randomUUID())
    }

    @Test
    fun generateTimestamps() {
        for (i in 0..6) {
            println(System.currentTimeMillis())
        }
    }
}