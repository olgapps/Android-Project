package olga.pietrzyk.androidteacher.databaseSqlite

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="tasks_table")
class Task(
    @ColumnInfo(name = "title")
    val taskTitle: String?,

    @ColumnInfo(name = "content")
    val taskContent: String?,

    @ColumnInfo(name = "status")
    var taskStatus: Boolean,

    @PrimaryKey(autoGenerate = true)
    var taskId: Long=0L
)