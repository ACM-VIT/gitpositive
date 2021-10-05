package org.acmvit.gitpositive

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.acmvit.gitpositive.local.UserDao
import org.acmvit.gitpositive.local.UserDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesBaseUrl(@ApplicationContext context: Context): UserDatabase {
        return Room.databaseBuilder(
            context,
            UserDatabase::class.java, "database-name"
        ).build()
    }

    @Provides
    @Singleton
    fun providesUserDao(userDatabase: UserDatabase): UserDao {
        return userDatabase.userDao()
    }

}