package org.craftedsw.tripservicekata.trip

import io.mockk.every
import io.mockk.mockkObject
import org.craftedsw.tripservicekata.exception.UserNotLoggedInException
import org.craftedsw.tripservicekata.user.User
import org.craftedsw.tripservicekata.user.UserSession
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class TripServiceTest {
    private lateinit var tripService: TripService
    private val guestUser: User = User()
    private var friendUser: User = User()
    private val loggedInUser: User = User()
    private val anotherUser: User = User()
    private val tripToGreece = Trip()
    private val tripToJapan = Trip()
    private val tripList: List<Trip> = listOf(tripToGreece, tripToJapan)

    @BeforeEach
    fun init() {
        tripService = TripService()
    }

    @Test
    fun `should throw UserNotLoggedInException when user is not logged in`() {
        mockkObject(UserSession)

        every { UserSession.instance.loggedUser } returns null

        assertThrows<UserNotLoggedInException> { tripService.getTripsByUser(guestUser) }
    }

    @Test
    fun `should not return any trips when users are not friends`() {
        mockkObject(UserSession)
        mockkObject(TripDAO)
        every { UserSession.instance.loggedUser } returns loggedInUser
        every { TripDAO.findTripsByUser(any()) } returns emptyList()

        friendUser.addFriend(anotherUser)
        friendUser.addTrip(Trip())

        assertTrue { tripService.getTripsByUser(friendUser).isEmpty() }
    }

    @Test
    fun `should return all trips when users are friends`() {
        mockkObject(UserSession)
        mockkObject(TripDAO)
        every { UserSession.instance.loggedUser } returns loggedInUser
        every { TripDAO.findTripsByUser(any()) } returns tripList

        friendUser.addFriend(loggedInUser)
        friendUser.addTrip(tripToJapan)
        friendUser.addTrip(tripToGreece)

        assertEquals(2,tripService.getTripsByUser(friendUser).size)
        assertTrue { tripService.getTripsByUser(friendUser).containsAll(tripList) }
    }
}