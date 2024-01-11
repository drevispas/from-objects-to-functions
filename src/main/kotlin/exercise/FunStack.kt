package org.example.exercise

data class FunStack<T>(val stack: List<T> = emptyList()) {

    fun size() = stack.size

    fun push(a: T): FunStack<T> {
        val newStack = FunStack(listOf(a) + stack)
        println("newStack = $newStack")
        return newStack
    }

    fun pop(): Pair<T, FunStack<T>> {
        val b = stack.first()
        val newStack = FunStack(stack.drop(1))
        return b to newStack
    }
}
