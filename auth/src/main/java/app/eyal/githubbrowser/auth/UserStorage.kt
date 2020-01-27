package app.eyal.githubbrowser.auth

import kotlinx.coroutines.flow.Flow

data class User(val accessToken: String, val usedType: UserType = UserType.Personal)

/**
 * Stores the logged in user.
 */
interface UserStorage {
    /**
     * [Flow] of the current [User]. null is returns when no [User] is logged in.
     */
    val user: Flow<User?>

    /**
     * Update the logged in [User].
     */
    suspend fun setUser(user: User)

    /**
     * Clears the logged in user.
     */
    suspend fun clearUser()
}

