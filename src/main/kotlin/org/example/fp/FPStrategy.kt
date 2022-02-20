package org.example.fp

import java.math.BigDecimal
import kotlin.test.assertEquals

private data class Product(val pricePerUnit: Long)
private data class Item(val product: Product, val quantity: Int)

private typealias DiscountStrategy = (subtotal: Long) -> Long

private val noDiscount: DiscountStrategy = { subtotal -> subtotal }

private val happyHourDiscount: DiscountStrategy = { subtotal -> subtotal / 2 }

private fun couponDiscount(percentageValue: Int): DiscountStrategy = { subtotal ->
    val discountInMinorUnits = BigDecimal.valueOf(subtotal)
        .multiply(percentageValue.toBigDecimal().divide(BigDecimal(100)))
        .setScale(0)
        .toLong()
    subtotal - discountInMinorUnits
}

private fun totalPrice(items: List<Item>) = totalPrice(items, noDiscount)

private fun totalPrice(items: List<Item>, discountStrategy: DiscountStrategy): Long =
    items
        .map { it.quantity * it.product.pricePerUnit }
        .fold(0, Long::plus)
        .run(discountStrategy)

fun main() {
    val items = listOf(
        Item(Product(1000), 5),
        Item(Product(10000), 3)
    )

    println("running FP strategy pattern..")
    assertEquals(35000L, totalPrice(items, noDiscount))
    assertEquals(17500L, totalPrice(items, happyHourDiscount))
    assertEquals(24500L, totalPrice(items, couponDiscount(30)))
    assertEquals(0L, totalPrice(emptyList()))
    println("tests passed!")
}

