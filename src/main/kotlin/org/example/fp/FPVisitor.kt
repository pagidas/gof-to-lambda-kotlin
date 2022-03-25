package org.example.fp

import java.math.BigDecimal
import kotlin.reflect.KClass
import kotlin.test.assertEquals

private data class Liquor(val pricePerUnit: Long): ProductType
private data class Tobacco(val pricePerWeight: Long): ProductType
private data class VatRate(val standard: Int, val overrides: Map<KClass<out ProductType>, Int>)

private sealed interface ProductType

private fun netPrice(product: ProductType): Long =
    when (product) {
        is Liquor -> product.pricePerUnit
        is Tobacco -> product.pricePerWeight
    }
private fun vat(product: ProductType, vatRate: VatRate): Long {
    val rate = vatRate.overrides[product::class] ?: vatRate.standard

    return when (product) {
        is Liquor -> BigDecimal(product.pricePerUnit)
            .multiply(BigDecimal(rate).divide(BigDecimal(100)))
            .setScale(0)
            .toLong()
        is Tobacco -> BigDecimal(product.pricePerWeight)
            .multiply(BigDecimal(rate).divide(BigDecimal(100)))
            .setScale(0)
            .toLong()
    }
}
private fun grossPrice(product: ProductType, vatRate: VatRate): Long = netPrice(product) + vat(product, vatRate)

fun main() {
    val vatRate = VatRate(20, mapOf(Tobacco::class to 25))
    val liquor: ProductType = Liquor(35000)
    val tobacco: ProductType = Tobacco(5500)

    println("running FP visitor pattern..")
    assertEquals(35000L, netPrice(liquor))
    assertEquals(7000L, vat(liquor, vatRate))
    assertEquals(42000L, grossPrice(liquor, vatRate))

    assertEquals(5500L, netPrice(tobacco))
    assertEquals(1375L, vat(tobacco, vatRate))
    assertEquals(6875L, grossPrice(tobacco, vatRate))
    println("tests passed!")
}

