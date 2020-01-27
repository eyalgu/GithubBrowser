package app.eyal.githubbrowser.auth

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.runner.RunWith
import kotlin.test.BeforeTest
import kotlin.test.Test

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class UserStorageTest {

    companion object {
        val User1 = UserEntity("user1", UserType.Personal)
        val User2 = UserEntity("user2", UserType.Business)
    }


    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val userStorage = createUserStore(
        Room.inMemoryDatabaseBuilder(context, UserDatabase::class.java)
            .setTransactionExecutor(testDispatcher.asExecutor())
            .setQueryExecutor(testDispatcher.asExecutor())
    ) as UserDao

    @BeforeTest
    fun setup() = testScope.runBlockingTest {
        userStorage.clearUser()
    }

    @Test
    fun GIVEN_no_user_WHEN_new_users_THEN_users_are_emitted() = testScope.runBlockingTest {
        userStorage.setUserEntities(listOf(User1, User2))
        val users = async {
            userStorage.users()
        }

        assertThat(users.await()).containsExactly(listOf<UserEntity>(), listOf(User1, User2))
    }
}
