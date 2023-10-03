package com.lianyi.paimonsnotebook.common.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.lianyi.paimonsnotebook.common.application.PaimonsNotebookApplication
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
        DailyNoteWidget::class
    ],
    autoMigrations = [
        AutoMigration(1, 2)
    ],
    version = 2,
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

    companion object {
        private const val DB_NAME = "paimonsnotebook_database.db"

        private val migration_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL()
            }
        }

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