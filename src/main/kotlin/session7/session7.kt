package session7

fun main(){
//    val q10 = test(10.0)
//    println(q10(4.0))
//    val q20 = test(20.0)
//    println(q20(4.0))
    val myList = GenerateList.getData()
    val grossSalaryList = myList
        .map { (x, y) -> x to grossSalaryCalculator(y) }
    println(grossSalaryList[0].second(80.0))
    println(grossSalaryList[1].second(80.0))
    println(grossSalaryList[2].second(80.0))
    print(grossSalaryList.first { x -> x.first == 'c' }.second(80.0))


}



//fun test(x: Double): (Double) -> Double{
//    val x1 = x + 10
//    return { it -> it + x1 }
//}

fun grossSalaryCalculator(basicSalary: Double): (Double) -> Double{
    val tax = 0.2 * basicSalary
    return { bonus -> bonus + tax + basicSalary }
}

object GenerateList{
    fun getData(): List<Pair<Char, Double>> =
        mutableListOf(
            'a' to 1000.0,
            'b' to 2000.0,
            'c' to 3000.0
        )
}