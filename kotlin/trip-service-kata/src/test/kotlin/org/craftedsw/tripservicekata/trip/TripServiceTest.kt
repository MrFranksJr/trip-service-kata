package org.craftedsw.tripservicekata.trip

import io.mockk.*
import org.craftedsw.tripservicekata.exception.UserNotLoggedInException
import org.craftedsw.tripservicekata.user.User
import org.craftedsw.tripservicekata.user.UserSession
import kotlin.test.Test
import kotlin.test.assertFailsWith

class TripServiceTest {

    @Test
    fun `should throw UserNotLoggedInException when user is not logged in`() {
        val tripService = TripService()
        val user = mockk<User>()
        mockkObject(UserSession)

        every { UserSession.instance.loggedUser } returns null

        assertFailsWith<UserNotLoggedInException> {
            tripService.getTripsByUser(user)
        }
    }
}