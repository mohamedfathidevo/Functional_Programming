package session5

fun main(){
    val list = generateList()
    println("pipeline Test")
    list.asSequence()
        .map { x ->addOne(x) }
        .map { x ->square(x) }
        .map { x -> subtractTen(x) }
        .forEach { x -> print("$x ") }
    println("\ncomposition Test")
    list.asSequence()
        .map { x -> addOneSquareSubtractTen(x) }
        .forEach { x -> print("$x ") }
}

fun generateList() = mutableListOf(3.0, 5.0, 10.0, 7.0)

fun composition(): (Double) -> Double{
    val x = ::addOne
    val y = ::square
    val z = ::subtractTen
    return x.compose(y).compose(z)
}

fun addOneSquareSubtractTen(x: Double): Double{
    val result = composition()
    return result(x)
}

fun addOne(x: Double): Double = x+1
fun square(x: Double): Double = x*x
fun subtractTen(x: Double): Double = x-10

private fun <T1, T2, T3> ((T1) -> T2).compose(t2: (T2) -> T3): (T1) -> T3 =
    { x -> t2(this(x))}