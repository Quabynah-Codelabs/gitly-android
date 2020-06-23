package dev.gitly.util

import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

open class CoroutineContextProvider @Inject constructor() {
    open val main: CoroutineContext by lazy { Dispatchers.Main }
    open val io: CoroutineContext by lazy { Dispatchers.IO }
}

class TestCoroutineProvider : CoroutineContextProvider() {
    override val main: CoroutineContext
        get() = Dispatchers.Unconfined

    override val io: CoroutineContext
        get() = Dispatchers.Unconfined
}