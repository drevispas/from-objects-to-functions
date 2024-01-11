package org.example.domain

data class FunStack<T>(val stack: List<T> = emptyList()) {

    fun size() = stack.size

    fun push(a: T): FunStack<T> {
        val newStack = FunStack(listOf(a) + stack)
        return newStack
    }

    fun pop(): Pair<T,FunStack<T>> {
        val b = stack.first()
        val newStack = FunStack(stack.drop(1))
        return b to newStack
    }
}
