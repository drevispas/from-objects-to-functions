package org.example.exercise

class RPNCalculator {


    fun calc(e: String): Double {
        var funStack = FunStack<Double>()
        val ops = e.split(" ")
        ops.forEach {
            val number = it.toDoubleOrNull()
            println("n = $number")
            funStack = if (number != null) {
                funStack.push(number)
            } else {
                val (operand1, newFunStack1) = funStack.pop()
                val (operand2, newFunStack2) = newFunStack1.pop()
                val temp = when(it) {
                    "+" -> operand2 + operand1
                    "-" -> operand2 - operand1
                    "*" -> operand2 * operand1
                    "/" -> operand2 / operand1
                    else -> error("Unsupported operator `it`!")
                }
                newFunStack2.push(temp)
            }
        }
        return funStack.pop().first
    }
}
