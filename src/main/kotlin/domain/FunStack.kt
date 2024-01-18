package org.example.domain

data class FunStack<T>(val stack: List<T> = emptyList()) {

    fun size() = stack.size

    fun push(a: T): FunStack<T> = FunStack(listOf(a) + stack)

    // stack.first() instead of stack[0]
    fun pop(): Pair<T,FunStack<T>> = stack.first() to FunStack(stack.drop(1))
}
