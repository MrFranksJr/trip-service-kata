package org.craftedsw.tripservicekata.user

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class UserTest {
    private val guido = User.Builder().build()
    private val sander = User.Builder().build()

    @Test
    fun `should inform me that users are or are not friends`() {
        val user = User.Builder()
            .friendsWith(guido)
            .build()

        assertFalse(user.isFriendsWith(sander))
        assertTrue(user.isFriendsWith(guido))
    }

}
