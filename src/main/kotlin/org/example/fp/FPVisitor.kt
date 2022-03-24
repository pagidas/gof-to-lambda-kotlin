package org.example.fp

import java.math.BigDecimal
import kotlin.reflect.KClass
import kotlin.test.assertEquals

private data class Liquor(val pricePerUnit: Long): Goods
private data class Tobacco(val pricePerWeight: Long): Goods
private data class Rates(val standard: Int, val overrides: Map<KClass<out Goods>, Int>)

private sealed interface Goods

private fun netPrice(goods: Goods): Long =
    when (goods) {
        is Liquor -> goods.pricePerUnit
        is Tobacco -> goods.pricePerWeight
    }
private fun vat(goods: Goods, rates: Rates): Long {
    val rate = rates.overrides[goods::class] ?: rates.standard

    return when (goods) {
        is Liquor -> BigDecimal(goods.pricePerUnit)
            .multiply(BigDecimal(rate).divide(BigDecimal(100)))
            .setScale(0)
            .toLong()
        is Tobacco -> BigDecimal(goods.pricePerWeight)
            .multiply(BigDecimal(rate).divide(BigDecimal(100)))
            .setScale(0)
            .toLong()
    }
}
private fun grossPrice(goods: Goods, rates: Rates): Long =
    netPrice(goods) + vat(goods, rates)

fun main() {
    val rates = Rates(20, mapOf(Tobacco::class to 25))
    val liquor: Goods = Liquor(35000)
    val tobacco: Goods = Tobacco(5500)

    println("running FP visitor pattern..")
    assertEquals(35000L, netPrice(liquor))
    assertEquals(7000L, vat(liquor, rates))
    assertEquals(42000L, grossPrice(liquor, rates))

    assertEquals(5500L, netPrice(tobacco))
    assertEquals(1375L, vat(tobacco, rates))
    assertEquals(6875L, grossPrice(tobacco, rates))
    println("tests passed!")
}

