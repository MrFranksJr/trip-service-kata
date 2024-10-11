package org.craftedsw.tripservicekata.user

import org.craftedsw.tripservicekata.trip.Trip

class User private constructor(
    private val _friends: MutableList<User> = mutableListOf(),
    private val _trips: MutableList<Trip> = mutableListOf()
) {
    val friends: List<User> get() = _friends.toList()
    val trips: List<Trip> get() = _trips.toList()

    fun addFriend(user: User) = _friends.add(user)
    fun addTrip(trip: Trip) = _trips.add(trip)

    class Builder {
        private val friends: MutableList<User> = mutableListOf()
        private val trips: MutableList<Trip> = mutableListOf()

        fun friendsWith(vararg friends: User) = apply { this.friends.addAll(friends) }
        fun withTrips(vararg withTrips: Trip) = apply { this.trips.addAll(withTrips) }
        fun build(): User = User(friends, trips)
    }
}
