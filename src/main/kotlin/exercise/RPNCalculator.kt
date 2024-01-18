package org.example.exercise

class RPNCalculator {

    val operationMap: Map<String, Pair<Double,Double>> = TODO()

    val funStack: FunStack<Double> = TODO()

    fun calc(expr: String): Double = TODO()

    private fun reduce(stack: FunStack<Double>, token: String): FunStack<Double> = TODO()
}
