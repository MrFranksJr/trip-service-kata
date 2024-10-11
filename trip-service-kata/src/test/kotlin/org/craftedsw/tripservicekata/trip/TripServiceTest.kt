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
    private val loggedInUser: User = User.Builder().build()
    private val notLoggedInUser: User = User.Builder().build()
    private val tripToGreece = Trip()
    private val tripToJapan = Trip()

    @BeforeEach
    fun init() {
        tripService = TripService()
        mockkObject(UserSession)
        mockkObject(TripDAO)
    }

    @Test
    fun `should throw UserNotLoggedInException when user is not logged in`() {
        every { UserSession.instance.loggedUser } returns null

        assertThrows<UserNotLoggedInException> { tripService.getTripsByUser(notLoggedInUser) }
    }

    @Test
    fun `should not return any trips when users are not friends`() {
        every { UserSession.instance.loggedUser } returns loggedInUser
        every { TripDAO.findTripsByUser(any()) } returns emptyList()

        val friendUser = User.Builder()
            .withTrips(tripToJapan)
            .build()

        assertTrue { tripService.getTripsByUser(friendUser).isEmpty() }
    }

    @Test
    fun `should return all trips when users are friends`() {
        every { UserSession.instance.loggedUser } returns loggedInUser
        every { TripDAO.findTripsByUser(any()) } returns listOf(tripToGreece, tripToJapan)

        val friendUser = User.Builder()
            .friendsWith(loggedInUser)
            .withTrips(tripToJapan, tripToGreece)
            .build()

        assertEquals(2, tripService.getTripsByUser(friendUser).size)
        assertTrue { tripService.getTripsByUser(friendUser).containsAll(listOf(tripToGreece, tripToJapan)) }
    }
}