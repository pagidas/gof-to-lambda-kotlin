package org.example.oop

import java.math.BigDecimal
import kotlin.reflect.KClass
import kotlin.test.assertEquals

private data class Liquor(val pricePerUnit: Long): Visitable {
    override fun <T> accept(visitor: Visitor<T>): T {
        return visitor.visit(this)
    }
}
private data class Tobacco(val pricePerWeight: Long): Visitable {
    override fun <T> accept(visitor: Visitor<T>): T {
        return visitor.visit(this)
    }
}
private data class Rates(val standard: Int, val overrides: Map<KClass<out Visitable>, Int>)

private interface Visitor<T> {
    fun visit(liquor: Liquor): T
    fun visit(tobacco: Tobacco): T
}

private interface Visitable {
    fun <T> accept(visitor: Visitor<T>): T
}

private class NetPriceVisitor: Visitor<Long> {
    override fun visit(liquor: Liquor): Long {
        return liquor.pricePerUnit
    }

    override fun visit(tobacco: Tobacco): Long {
        return tobacco.pricePerWeight
    }
}

private class VATVisitor(private val rates: Rates): Visitor<Long> {
    override fun visit(liquor: Liquor): Long {
        val rate = rates.overrides[liquor::class] ?: rates.standard

        return BigDecimal(liquor.pricePerUnit)
            .multiply(BigDecimal(rate).divide(BigDecimal(100)))
            .setScale(0)
            .toLong()
    }

    override fun visit(tobacco: Tobacco): Long {
        val rate = rates.overrides[tobacco::class] ?: rates.standard

        return BigDecimal(tobacco.pricePerWeight)
            .multiply(BigDecimal(rate).divide(BigDecimal(100)))
            .setScale(0)
            .toLong()
    }
}

private class GrossPriceVisitor(rates: Rates): Visitor<Long> {
    private val netPrice: NetPriceVisitor = NetPriceVisitor()
    private val vat: VATVisitor = VATVisitor(rates)

    override fun visit(liquor: Liquor): Long {
        return liquor.accept(netPrice) + liquor.accept(vat)
    }

    override fun visit(tobacco: Tobacco): Long {
        return tobacco.accept(netPrice) + tobacco.accept(vat)
    }
}

fun main() {
    val rates = Rates(20, mapOf(Tobacco::class to 25))

    val netPrice = NetPriceVisitor()
    val vat = VATVisitor(rates)
    val grossPrice = GrossPriceVisitor(rates)

    val liquor: Visitable = Liquor(35000)
    val tobacco: Visitable = Tobacco(5500)

    println("running OOP visitor pattern..")
    assertEquals(35000L, liquor.accept(netPrice))
    assertEquals(7000L, liquor.accept(vat))
    assertEquals(42000L, liquor.accept(grossPrice))

    assertEquals(5500L, tobacco.accept(netPrice))
    assertEquals(1375L, tobacco.accept(vat))
    assertEquals(6875L, tobacco.accept(grossPrice))
    println("tests passed!")
}

