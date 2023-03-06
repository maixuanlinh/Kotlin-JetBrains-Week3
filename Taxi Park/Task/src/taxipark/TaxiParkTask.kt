package taxipark


/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> {
    // Create a mutable set to hold the fake drivers
    val fakeDrivers = mutableSetOf<Driver>()

    // Iterate over all drivers
    for (driver in allDrivers) {
        // Check if there is any trip with this driver
        val hasTrips = trips.any { it.driver == driver }

        // If there are no trips, add the driver to the fake drivers set
        if (!hasTrips) {
            fakeDrivers.add(driver)
        }
    }

    // Return the set of fake drivers
    return fakeDrivers
}

/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> =
    allPassengers.filter { p -> trips.count { it.passengers.contains(p) } >= minTrips }.toSet()



/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> {
    val trips = trips
        .filter { it.driver == driver }
        .flatMap(Trip::passengers)

    val passengerCount = trips.groupingBy { it }.eachCount()
    return passengerCount.filterValues { it > 1 }.keys.toSet()
}





/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> {
    val smartPassengers = mutableSetOf<Passenger>()
    val passengerDiscountCount = mutableMapOf<Passenger, Int>()
    val passengerNonDiscountCount = mutableMapOf<Passenger, Int>()

    trips.forEach { trip ->
        trip.passengers.forEach { passenger ->
            if (trip.discount != null && trip.discount != 0.0) {
                passengerDiscountCount[passenger] = (passengerDiscountCount[passenger] ?: 0) + 1
            } else {
                passengerNonDiscountCount[passenger] = (passengerNonDiscountCount[passenger] ?: 0) + 1
            }
        }
    }

    passengerDiscountCount.forEach { (passenger, discountCount) ->
        val nonDiscountCount = passengerNonDiscountCount[passenger] ?: 0
        if (discountCount > nonDiscountCount) {
            smartPassengers.add(passenger)
        }
    }

    return smartPassengers
}


/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */

fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? {
    val tripsByDurationPeriod = trips.groupBy { it.duration / 10 * 10 }
    return tripsByDurationPeriod.maxBy { it.value.size }?.key?.let { duration ->
        duration..(duration + 9)
    }
}










/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean {
    if (trips.isEmpty()) {
        return false
    }

    val totalIncome = trips.map { it.cost }.sum()
    val driversIncome = trips.groupBy { it.driver }
        .mapValues { (_, trips) -> trips.map { it.cost }.sum() }
    val sortedDriversIncome = driversIncome.values.sortedDescending()

    val top20PercentIncome = sortedDriversIncome.take((allDrivers.size * 0.2).toInt()).sum()
    return top20PercentIncome >= totalIncome * 0.8
}
