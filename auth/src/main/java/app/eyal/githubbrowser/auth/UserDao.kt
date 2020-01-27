package app.eyal.githubbrowser.auth

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun createUserStore(context: Context): UserStorage =
    createUserStore(
        Room.databaseBuilder(
            context,
            UserDatabase::class.java,
            "auth"
        )
    )

internal fun createUserStore(builder: RoomDatabase.Builder<UserDatabase>): UserStorage =
    builder
        .build()
        .userDao()

enum class UserType {
    Personal,
    Business
}

@Entity
data class UserEntity(val accessToken: String, @PrimaryKey val userType: UserType)

@Dao
internal abstract class UserDao : UserStorage {

    override val user: Flow<User?>
        get() = userEntity.map { it?.let { User(it.accessToken, it.userType) }}

    @get:Query("SELECT * FROM UserEntity LIMIT 1")
    abstract val userEntity: Flow<UserEntity?>

    @get:Query("SELECT * FROM UserEntity")
    abstract val users: Flow<List<UserEntity>>

    override suspend fun setUser(user: User) = setUserEntities(listOf(
            UserEntity(
                user.accessToken,
                user.usedType
            )
        ))


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun setUserEntities(userEntities: List<UserEntity>)

    @Query("DELETE FROM UserEntity WHERE userType='Personal'")
    abstract override suspend fun clearUser()

}

internal class UserTypeConverters {
    @TypeConverter
    fun userTypeToString(userType: UserType?) = userType?.name

    @TypeConverter
    fun stringToUserType(name: String?) = name?.let { UserType.valueOf(it) }

    // @TypeConverter
    // fun userToUserEntity(user: User?) = user?.let { UserEntity(it.accessToken, it.usedType) }
    //
    // @TypeConverter
    // fun userEntityToUser(userEntity: UserEntity?) = userEntity?.let { User(it.accessToken, it.userType) }
}

@Database(
    version = 1,
    exportSchema = false,
    entities = [UserEntity::class]
)
@TypeConverters(UserTypeConverters::class)
internal abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}