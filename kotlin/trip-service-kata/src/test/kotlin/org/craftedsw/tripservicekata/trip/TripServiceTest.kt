package org.craftedsw.tripservicekata.trip

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockkObject
import org.craftedsw.tripservicekata.exception.UserNotLoggedInException
import org.craftedsw.tripservicekata.user.User
import org.craftedsw.tripservicekata.user.UserSession
import org.junit.jupiter.api.AfterEach
import kotlin.test.Test
import kotlin.test.assertFailsWith

class TripServiceTest {
    private val guestUser: User = User()
    private val loggedInUser: User = User()
    private val anotherUser: User = User()

    @AfterEach
    fun cleanUp() {
        clearAllMocks()
    }

    @Test
    fun `should throw UserNotLoggedInException when user is not logged in`() {
        val tripService = TripService()
        mockkObject(UserSession)

        every { UserSession.instance.loggedUser } returns null

        assertFailsWith<UserNotLoggedInException> {
            tripService.getTripsByUser(guestUser)
        }
    }

    @Test
    fun `should not return any trips when users are not friends`() {
        val tripService = TripService()
        mockkObject(UserSession)
        mockkObject(TripDAO)
        every { UserSession.instance.loggedUser } returns loggedInUser
        every { TripDAO.findTripsByUser(any()) } returns emptyList()

        val friend = User()
        friend.addFriend(anotherUser)
        friend.addTrip(Trip())

        val friendTrips: List<Trip> = tripService.getTripsByUser(friend)

        assert(friendTrips.isEmpty())
    }
}