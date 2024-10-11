package org.craftedsw.tripservicekata.trip

import org.craftedsw.tripservicekata.exception.UserNotLoggedInException
import org.craftedsw.tripservicekata.user.User
import org.craftedsw.tripservicekata.user.UserSession

class TripService {

    fun getTripsByUser(user: User): List<Trip> {
        val loggedUser = UserSession.instance.loggedUser
            ?: throw UserNotLoggedInException()

        return if (user.friends.any { it == loggedUser }) TripDAO.findTripsByUser(user) else emptyList()
    }
}
