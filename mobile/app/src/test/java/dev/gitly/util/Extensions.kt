package dev.gitly.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

// Infix function for evaluating value equality
infix fun Int?.shouldEqual(other: Int) = this != null && this == other

// Helper function for starting an observer for a block and removing it when done
fun <T> LiveData<T>.observeForTesting(block: () -> Unit) {
    val observer = Observer<T> { Unit }

    // fixme: unable to observe data
    try {
        //observeForever(observer)
        block()
    } finally {
        //removeObserver(observer)
    }
}