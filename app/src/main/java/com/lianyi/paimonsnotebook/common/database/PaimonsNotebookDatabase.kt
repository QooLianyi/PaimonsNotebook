package com.lianyi.paimonsnotebook.common.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lianyi.paimonsnotebook.common.application.PaimonsNotebookApplication
import com.lianyi.paimonsnotebook.common.database.achievement.dao.AchievementsDao
import com.lianyi.paimonsnotebook.common.database.achievement.dao.AchievementUserDao
import com.lianyi.paimonsnotebook.common.database.achievement.entity.Achievements
import com.lianyi.paimonsnotebook.common.database.achievement.entity.AchievementUser
import com.lianyi.paimonsnotebook.common.database.app_widget_binding.dao.AppWidgetBindingDao
import com.lianyi.paimonsnotebook.common.database.app_widget_binding.entity.AppWidgetBinding
import com.lianyi.paimonsnotebook.common.database.daily_note.dao.DailyNoteDao
import com.lianyi.paimonsnotebook.common.database.daily_note.dao.DailyNoteWidgetDao
import com.lianyi.paimonsnotebook.common.database.daily_note.entity.DailyNote
import com.lianyi.paimonsnotebook.common.database.daily_note.entity.DailyNoteWidget
import com.lianyi.paimonsnotebook.common.database.disk_cache.dao.DiskCacheDao
import com.lianyi.paimonsnotebook.common.database.disk_cache.entity.DiskCache
import com.lianyi.paimonsnotebook.common.database.gacha.dao.GachaItemsDao
import com.lianyi.paimonsnotebook.common.database.gacha.entity.GachaItems
import com.lianyi.paimonsnotebook.common.database.user.dao.UserDao
import com.lianyi.paimonsnotebook.common.database.user.entity.User

@Database(
    entities = [
        GachaItems::class,
        DiskCache::class,
        AppWidgetBinding::class,
        User::class,
        DailyNote::class,
        DailyNoteWidget::class,
        AchievementUser::class,
        Achievements::class
    ],
    autoMigrations = [
        AutoMigration(1, 2),
        AutoMigration(2, 3)
    ],
    version = 3,
    exportSchema = true
)
abstract class PaimonsNotebookDatabase : RoomDatabase() {

    //祈愿记录
    abstract val gachaItemsDao: GachaItemsDao

    //硬盘缓存
    abstract val diskCacheDao: DiskCacheDao

    //桌面组件绑定
    abstract val appWidgetBindingDao: AppWidgetBindingDao

    //用户
    abstract val userDao: UserDao

    //实时便笺
    abstract val dailyNoteDao: DailyNoteDao

    //实时便笺桌面组件
    abstract val dailyNoteWidgetDao: DailyNoteWidgetDao

    //成就管理用户
    abstract val achievementUserDao: AchievementUserDao

    //成就管理数据表
    abstract val achievementsDao: AchievementsDao

    companion object {
        private const val DB_NAME = "paimonsnotebook_database.db"

//        private val migration_1_2 = object : Migration(1, 2) {
//            override fun migrate(db: SupportSQLiteDatabase) {
//                database.execSQL()
//            }
//        }

        val database by lazy {
            synchronized(this) {
                Room.databaseBuilder(
                    PaimonsNotebookApplication.context,
                    PaimonsNotebookDatabase::class.java,
                    DB_NAME
                )
//                    .addMigrations(migration_1_2)
                    .build()
            }
        }
    }

}