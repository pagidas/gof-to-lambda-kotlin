package org.example.oop

import java.math.BigDecimal
import kotlin.test.assertEquals

private data class Product(val pricePerUnit: Long)
private data class Item(val product: Product, val quantity: Int)

private interface DiscountStrategy {
    fun calculate(subtotal: Long): Long
}

private class NoDiscount: DiscountStrategy {
    override fun calculate(subtotal: Long): Long {
        return subtotal
    }
}
private class HappyHourDiscount: DiscountStrategy {
    override fun calculate(subtotal: Long): Long {
        return subtotal / 2
    }
}
private class CouponDiscount(private val percentageValue: Int): DiscountStrategy {
    override fun calculate(subtotal: Long): Long {
        val discountInMinorUnits = BigDecimal.valueOf(subtotal)
            .multiply(percentageValue.toBigDecimal().divide(BigDecimal(100)))
            .setScale(0)
            .toLong()
        return subtotal - discountInMinorUnits
    }
}

private class Bill(private val items: List<Item>, private val discountStrategy: DiscountStrategy) {

    constructor(items: List<Item>): this(items, NoDiscount())

    fun totalPrice(): Long {
        val subtotal = items
            .map { it.quantity * it.product.pricePerUnit }
            .fold(0, Long::plus)
        return discountStrategy.calculate(subtotal)
    }

}

fun main() {
    val items = listOf(
        Item(Product(1000), 5),
        Item(Product(10000), 3)
    )

    println("running OOP strategy pattern..")
    assertEquals(35000L, Bill(items, NoDiscount()).totalPrice())
    assertEquals(17500L, Bill(items, HappyHourDiscount()).totalPrice())
    assertEquals(24500L, Bill(items, CouponDiscount(30)).totalPrice())
    assertEquals(0L, Bill(emptyList()).totalPrice())
    println("tests passed!")
}

