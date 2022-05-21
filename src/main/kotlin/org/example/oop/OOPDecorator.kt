package org.example.oop

import kotlin.system.measureTimeMillis
import kotlin.test.assertEquals

private interface AddThree {
    fun calculate(x: Int): Int
}

private class AddThreeLogic: AddThree {
    override fun calculate(x: Int): Int {
        return x + 3
    }
}

private class AddThreeLogged(private val delegate: AddThree): AddThree {
    override fun calculate(x: Int): Int {
        println("Attempt to add 3 into: $x")
        val result = delegate.calculate(x)
        println("Resulted in: $result")
        return result
    }
}

private class AddThreeTimed(private val delegate: AddThree): AddThree {
    override fun calculate(x: Int): Int {
        var result: Int
        val elapsed = measureTimeMillis {
            result = delegate.calculate(x)
        }
        println("Operation took $elapsed ms")
        return result
    }
}

fun main() {
    val addThreeLogic: AddThree = AddThreeLogic()
    val addThreeLogged: AddThree = AddThreeLogged(addThreeLogic)
    val addThreeTimed: AddThree = AddThreeTimed(addThreeLogic)
    val addThreeLoggedAndTimed: AddThree = AddThreeLogged(AddThreeTimed(addThreeLogic))

    println("running OOP decorator pattern...")
    println("[AddThreeLogic]")
    assertEquals(10, addThreeLogic.calculate(7))
    println("[AddThreeLogged]")
    assertEquals(10, addThreeLogged.calculate(7))
    println("[AddThreeTimed]")
    assertEquals(10, addThreeTimed.calculate(7))
    println("[AddThreeLoggedAndTimed]")
    assertEquals(10, addThreeLoggedAndTimed.calculate(7))
    println("tests passed!")
}

