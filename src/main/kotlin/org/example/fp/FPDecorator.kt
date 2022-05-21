package org.example.fp

import kotlin.system.measureTimeMillis
import kotlin.test.assertEquals

// Giving a name to our function to make the example more readable
private typealias AddThree = (Int) -> Int

private val addThreeLogic: AddThree = { x -> x + 3 }

private val withLogging: (AddThree) -> AddThree = { delegate ->
    { x ->
        println("Attempt to add 3 into: $x")
        val result = delegate(x)
        println("Resulted in: $result")
        result
    }
}

private val withTiming: (AddThree) -> AddThree = { delegate ->
    { x ->
        var result: Int
        val elapsed = measureTimeMillis {
            result = delegate(x)
        }
        println("Operation took $elapsed ms")
        result
    }
}

private val addThreeLogged: AddThree = withLogging(addThreeLogic)
private val addThreeTimed: AddThree = withTiming(addThreeLogic)
private val addThreeLoggedAndTimed: AddThree = withLogging(withTiming(addThreeLogic))

fun main() {
    println("running FP decorator pattern...")
    println("[AddThreeLogic]")
    assertEquals(10, addThreeLogic(7))
    println("[AddThreeLogged]")
    assertEquals(10, addThreeLogged(7))
    println("[AddThreeTimed]")
    assertEquals(10, addThreeTimed(7))
    println("[AddThreeLoggedAndTimed]")
    assertEquals(10, addThreeLoggedAndTimed(7))
    println("tests passed!")
}

