package session6

import java.util.*

fun main() {
    val order = Order(2000.0, Calendar.getInstance())
    val invoicePath = InvoicePath()
    val availabilityPath = AvailabilityPath()
    val cost = adjustCost(order, invoicePath, availabilityPath)
    println("Cost is $cost")
}

fun adjustCost(
    order: Order,
    invoicePath: InvoicePath,
    availabilityPath: AvailabilityPath
): Double {
    val processConfiguration = getConfiguration()
    val orderInvoicePathFunction = orderInvoicePathFunction(processConfiguration, invoicePath)
    val orderAvailabilityPathFunction = orderAvailabilityPathFunction(processConfiguration, availabilityPath)
    val fright = orderInvoicePathFunction(order)
    val shippingData = orderAvailabilityPathFunction(order)
    return if (shippingData.calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) fright.cost + 1000 else fright.cost + 500
}

fun getConfiguration(): ProcessConfiguration {
    return ProcessConfiguration(
        InvoiceChoice.INV1,
        ShippingChoice.SH1,
        FreightChoice.FR1,
        AvailabilityChoice.AV1,
        ShippingDataChoice.SD1
    )
}

fun orderInvoicePathFunction(
    processConfiguration: ProcessConfiguration,
    invoicePath: InvoicePath
): (Order) -> Freight {
    val requiredInvoiceFunc =
        invoicePath.invoiceFunctions.first { it.first == processConfiguration.invoiceChoice }.second
    val requiredShippingFunc =
        invoicePath.shippingFunctions.first { it.first == processConfiguration.shippingChoice }.second
    val requiredFreightFunc =
        invoicePath.freightFunctions.first { it.first == processConfiguration.freightChoice }.second

    val invoiceToFright =
        requiredInvoiceFunc.compose(requiredShippingFunc).compose(requiredFreightFunc)
    return { x -> invoiceToFright(x) }
}


fun orderAvailabilityPathFunction(
    processConfiguration: ProcessConfiguration,
    availabilityPath: AvailabilityPath
): (Order) -> ShippingData {
    val requiredAvailabilityFunc =
        availabilityPath.availabilityFunctions.first { it.first == processConfiguration.availabilityChoice }.second
    val requiredShippingDataFunc =
        availabilityPath.shippingDataFunctions.first { it.first == processConfiguration.shippingDataChoice }.second
    val availabilityToShippingData = requiredAvailabilityFunc.compose(requiredShippingDataFunc)
    return { x -> availabilityToShippingData(x) }
}

data class InvoicePath(
    val invoiceFunctions: MutableList<Pair<InvoiceChoice, (Order) -> Invoice>> = generateInvoiceList(),
    val shippingFunctions: MutableList<Pair<ShippingChoice, (Invoice) -> Shipping>> = generateShippingList(),
    val freightFunctions: MutableList<Pair<FreightChoice, (Shipping) -> Freight>> = generateFrightList(),
)

data class AvailabilityPath(
    val availabilityFunctions: MutableList<Pair<AvailabilityChoice, (Order) -> Availability>> = generateAvailabilityList(),
    val shippingDataFunctions: MutableList<Pair<ShippingDataChoice, (Availability) -> ShippingData>> = generateShippingDataList()
)

private fun generateInvoiceList() = mutableListOf(
    InvoiceChoice.INV1 to calculateInvoice1(),
    InvoiceChoice.INV2 to calculateInvoice2(),
    InvoiceChoice.INV3 to calculateInvoice3(),
)

private fun generateShippingList() = mutableListOf(
    ShippingChoice.SH1 to calculateShipping1(),
    ShippingChoice.SH2 to calculateShipping2(),
    ShippingChoice.SH3 to calculateShipping3(),
    ShippingChoice.SH4 to calculateShipping4(),
    ShippingChoice.SH5 to calculateShipping5()
)

private fun generateFrightList() = mutableListOf(
    FreightChoice.FR1 to calculateFreight1(),
    FreightChoice.FR2 to calculateFreight2(),
    FreightChoice.FR3 to calculateFreight3()
)

private fun generateAvailabilityList() = mutableListOf(
    AvailabilityChoice.AV1 to calculateAvailability1(),
    AvailabilityChoice.AV2 to calculateAvailability2()
)

private fun generateShippingDataList() = mutableListOf(
    ShippingDataChoice.SD1 to calculateShippingData1(),
    ShippingDataChoice.SD2 to calculateShippingData2(),
    ShippingDataChoice.SD3 to calculateShippingData3(),
    ShippingDataChoice.SD4 to calculateShippingData4()
)

private fun calculateInvoice1(): (Order) -> Invoice =
    { x -> Invoice(x.productCost * 3.6) }

private fun calculateInvoice2(): (Order) -> Invoice =
    { x -> Invoice(x.productCost * 7.69) }

private fun calculateInvoice3(): (Order) -> Invoice =
    { x -> Invoice(x.productCost * 0.9) }

private fun calculateShipping1(): (Invoice) -> Shipping =
    { x -> Shipping(1, x.cost + 1) }

private fun calculateShipping2(): (Invoice) -> Shipping =
    { x -> Shipping(2, x.cost + 2) }

private fun calculateShipping3(): (Invoice) -> Shipping =
    { x -> Shipping(3, x.cost + 3) }

private fun calculateShipping4(): (Invoice) -> Shipping =
    { x -> Shipping(4, x.cost + 4) }

private fun calculateShipping5(): (Invoice) -> Shipping =
    { x -> Shipping(5, x.cost + 5) }

private fun calculateFreight1(): (Shipping) -> Freight =
    { x -> Freight(x.cost) }

private fun calculateFreight2(): (Shipping) -> Freight =
    { x -> Freight(x.cost + 3) }

private fun calculateFreight3(): (Shipping) -> Freight =
    { x -> Freight(x.cost + 5) }

private fun calculateAvailability1(): (Order) -> Availability =
    { Availability(Calendar.getInstance().apply { add(Calendar.MONTH, 6) }) }

private fun calculateAvailability2(): (Order) -> Availability =
    { Availability(Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, 12) }) }

private fun calculateShippingData1(): (Availability) -> ShippingData =
    { x -> ShippingData(x.calendar.apply { add(Calendar.DAY_OF_MONTH, 6) }) }

private fun calculateShippingData2(): (Availability) -> ShippingData =
    { x -> ShippingData(x.calendar.apply { add(Calendar.DAY_OF_MONTH, 14) }) }

private fun calculateShippingData3(): (Availability) -> ShippingData =
    { x -> ShippingData(x.calendar.apply { add(Calendar.DAY_OF_MONTH, 19) }) }

private fun calculateShippingData4(): (Availability) -> ShippingData =
    { x -> ShippingData(x.calendar.apply { add(Calendar.DAY_OF_MONTH, 25) }) }

private fun <T1, T2, T3> ((T1) -> T2).compose(t2: (T2) -> T3): (T1) -> T3 =
    { x -> t2(this(x)) }

data class Order(val productCost: Double, val data: Calendar)
data class Invoice(val cost: Double)
data class Shipping(val shipperId: Int, val cost: Double)
data class Freight(val cost: Double)
data class Availability(val calendar: Calendar)
data class ShippingData(val calendar: Calendar)
data class ProcessConfiguration(
    val invoiceChoice: InvoiceChoice,
    val shippingChoice: ShippingChoice,
    val freightChoice: FreightChoice,
    val availabilityChoice: AvailabilityChoice,
    val shippingDataChoice: ShippingDataChoice
)

enum class InvoiceChoice {
    INV1,
    INV2,
    INV3
}

enum class ShippingChoice {
    SH1,
    SH2,
    SH3,
    SH4,
    SH5
}

enum class FreightChoice {
    FR1,
    FR2,
    FR3
}

enum class AvailabilityChoice {
    AV1,
    AV2
}

enum class ShippingDataChoice {
    SD1,
    SD2,
    SD3,
    SD4
}