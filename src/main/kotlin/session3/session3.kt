package session3

val order = Order(1, 10.0, 10.0, 10.0)

fun main(){
    var product = ProductType.Food
    val a = productParameterFood(order.productIndex)
    val b = productParameterBeverage(order.productIndex)
    val c = productParameterRawMaterial(order.productIndex)
    var p = if(product == ProductType.Food) a else if (product == ProductType.Beverage) b else c
    println(calculateDiscount(p, order))
}

fun calculateDiscount(productFunc: (Double) -> Double, order: Order): Double{
    val parameters = productFunc(order.productIndex)
    return parameters + order.quantity + order.unitPrice
}

fun productParameterFood(productIndex: Double): (Double) -> Double{
    return { x -> x + productIndex + 100}
}
fun productParameterRawMaterial(productIndex: Double): (Double) -> Double{
    return { x -> x + productIndex + 300}
}
fun productParameterBeverage(productIndex: Double): (Double) -> Double{
    return { x -> x + productIndex + 200}
}

enum class ProductType{
    Food,
    Beverage,
    RawMaterial
}

data class Order(val orderId: Int, val productIndex: Double, val quantity: Double, val unitPrice: Double)