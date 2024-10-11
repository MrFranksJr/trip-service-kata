package org.craftedsw.tripservicekata.trip

import io.mockk.every
import io.mockk.mockkObject
import org.craftedsw.tripservicekata.exception.UserNotLoggedInException
import org.craftedsw.tripservicekata.user.User
import org.craftedsw.tripservicekata.user.UserSession
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertFailsWith

class TripServiceTest() {
    private lateinit var tripService: TripService
    private val guestUser: User = User()
    private var friendUser: User = User()
    private val loggedInUser: User = User()
    private val anotherUser: User = User()
    private val tripToGreece = Trip()
    private val tripToJapan = Trip()
    private val tripList: List<Trip> = listOf(tripToGreece, tripToJapan)

    @Before
    fun init() {
        tripService = TripService()
    }

    @Test
    fun `should throw UserNotLoggedInException when user is not logged in`() {
        mockkObject(UserSession)

        every { UserSession.instance.loggedUser } returns null

        assertFailsWith<UserNotLoggedInException> {
            tripService.getTripsByUser(guestUser)
        }
    }

    @Test
    fun `should not return any trips when users are not friends`() {
        mockkObject(UserSession)
        mockkObject(TripDAO)
        every { UserSession.instance.loggedUser } returns loggedInUser
        every { TripDAO.findTripsByUser(any()) } returns emptyList()

        friendUser.addFriend(anotherUser)
        friendUser.addTrip(Trip())

        assert(tripService.getTripsByUser(friendUser).isEmpty())
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

        assert(tripService.getTripsByUser(friendUser).size == tripList.size)
        assert(tripService.getTripsByUser(friendUser).containsAll(tripList))
    }
}