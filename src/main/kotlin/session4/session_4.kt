package session4

import session4.GenerateOrder.generateOrderList

fun main() {
    val orderList = generateOrderList()
    orderList
        .map { x -> getOrderWithDiscount(x, getDiscountRules()) }
        .forEach { x -> println(x.discount) }
}

fun getOrderWithDiscount(order: Order, rules: List<Pair<(Order) -> Boolean, (Order) -> Double>>): Order =
    order.apply {
        this.discount = rules.asSequence()
            .filter { it.first(order) }
            .map { it.second(order) }
            .sorted()
            .take(3)
            .average()
    }

fun getDiscountRules(): List<Pair<(Order) -> Boolean, (Order) -> Double>> =
    mutableListOf(
        ::isAQualified to ::calculateA,
        ::isBQualified to ::calculateB,
        ::isCQualified to ::calculateC
    )

fun isAQualified(order: Order): Boolean = order.price > 10
fun calculateA(order: Order): Double = order.price * 0.10

fun isBQualified(order: Order): Boolean = order.price > 20
fun calculateB(order: Order): Double = order.price * 0.20

fun isCQualified(order: Order): Boolean = order.price > 30
fun calculateC(order: Order): Double = order.price * 0.30

data class Order(
    val price: Double,
    var discount: Double = 0.0
)

object GenerateOrder {
    fun generateOrderList() = mutableListOf(
        Order(15.0),
        Order(25.3),
        Order(32.0)
    )
}