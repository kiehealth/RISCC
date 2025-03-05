package com.cronelab.riscc.support.data.database.configuration

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cronelab.riscc.support.data.database.table.DBAppAnalyticsType
import com.cronelab.riscc.support.data.database.dao.*
import com.cronelab.riscc.support.data.database.table.*

/**
 * Created by Rajesh Shrestha on 2020-10-19.
 */

@Database(
    entities = [DBUser::class,
        DBLinks::class,
        DBNotification::class,
        DBNote::class,
        DBAppVersion::class,
        DBAboutUs::class,
        DBResourceFile::class,
        DBAppAnalyticsType::class
        /* ,
         DBActiveQuestionnaireGroup::class,
         DBQuestionnaire::class,
         DBQuestions::class,
         QuestionType::class,
         DBQuestionOption::class*/
    ],
    version = DatabaseConfigs.databaseVersion
)

abstract class AppDatabase : RoomDatabase() {

    abstract fun getDBUserDao(): DBUserDao
    abstract fun getDBLinksDao(): DBLinksDao
    abstract fun getDBNotificationDao(): DBNotificationDao
    abstract fun getDBNoteDao(): DBNoteDao
    abstract fun getDBAppVersionDao(): DBAppVersionDao
    abstract fun getDBAboutUsDao(): DBAboutUsDao
    abstract fun getDBResourceFilesDao(): DBResourceFileDao
    abstract fun getDBAppAnalyticsTypeDao(): DBAppAnalyticsTypeDao

    /*abstract fun getDBGroupQuestionnaireDao(): DBActiveQuestionnaireDao
    abstract fun getDBQuestionnaireDao(): DBQuestionnaireDao
    abstract fun getDBQuestionDao(): DBQuestionDao
    abstract fun getDBQuestionTypeDao(): DBQuestionTypeDao
    abstract fun getDBQuestionOptionDao(): DBQuestionOptionDao*/

    companion object {

        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                DatabaseConfigs.databaseName
            ).build()

        fun clearDatabase(): Unit? {
            return instance?.clearAllTables()
        }
    }
}