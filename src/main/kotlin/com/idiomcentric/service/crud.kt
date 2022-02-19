package com.idiomcentric.service

sealed interface Creation<out T : Any> {
    data class Retrieved<out T : Any>(val value: T) : Creation<T>
    object Error : Creation<Nothing>
}

sealed interface Retrieval<out T : Any> {
    data class Retrieved<out T : Any>(val value: T) : Retrieval<T>
    object NotFound : Retrieval<Nothing>
}

sealed interface Update {
    object Updated : Update
    object NotFound : Update
}

sealed interface Deletion {
    object Deleted : Deletion
    object NotFound : Deletion
}
