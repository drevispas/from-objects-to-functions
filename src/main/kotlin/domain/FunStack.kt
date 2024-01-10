package org.example.domain

data class FunStack<T>(val stack: List<T> = mutableListOf<T>()) {

    fun size() = stack.size

    fun push(a: T): FunStack<T> {
        val newStack = FunStack<T>(stack.toMutableList())
        newStack.stack.addFirst(a)
        return newStack
    }

    fun pop(): Pair<T,FunStack<T>> {
        val newStack = FunStack<T>(stack.toMutableList())
        val b = newStack.stack[0]
        newStack.stack.removeFirst()
        return b to newStack
    }
}
