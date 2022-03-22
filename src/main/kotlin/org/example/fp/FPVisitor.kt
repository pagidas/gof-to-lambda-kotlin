package org.example.fp

import java.math.BigDecimal
import kotlin.reflect.KClass
import kotlin.test.assertEquals

private data class Liquor(val price: Long): Goods
private data class Tobacco(val price: Long): Goods

private sealed interface Goods

private fun netPrice(goods: Goods): Long =
    when (goods) {
        is Liquor -> goods.price
        is Tobacco -> goods.price
    }
private fun vat(goods: Goods, standardRate: Int, otherRates: Map<KClass<out Goods>, Int>): Long {
    val rate = otherRates[goods::class] ?: standardRate

    return when (goods) {
        is Liquor -> BigDecimal(goods.price)
            .multiply(BigDecimal(rate).divide(BigDecimal(100)))
            .setScale(0)
            .toLong()
        is Tobacco -> BigDecimal(goods.price)
            .multiply(BigDecimal(rate).divide(BigDecimal(100)))
            .setScale(0)
            .toLong()
    }
}
private fun grossPrice(goods: Goods, standardRate: Int, otherRates: Map<KClass<out Goods>, Int>): Long =
    when (goods) {
        is Liquor -> netPrice(goods) + vat(goods, standardRate, otherRates)
        is Tobacco -> netPrice(goods) + vat(goods, standardRate, otherRates)
    }

fun main() {
    val standardRate = 20
    val otherRates: Map<KClass<out Goods>, Int> = mapOf(Tobacco::class to 25)

    val liquor: Goods = Liquor(35000)
    val tobacco: Goods = Tobacco(5500)

    println("running FP visitor pattern..")
    assertEquals(35000L, netPrice(liquor))
    assertEquals(7000L, vat(liquor, standardRate, otherRates))
    assertEquals(42000L, grossPrice(liquor, standardRate, otherRates))

    assertEquals(5500L, netPrice(tobacco))
    assertEquals(1375L, vat(tobacco, standardRate, otherRates))
    assertEquals(6875L, grossPrice(tobacco, standardRate, otherRates))
    println("tests passed!")
}

