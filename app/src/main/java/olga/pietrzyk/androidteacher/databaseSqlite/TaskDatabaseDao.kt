package olga.pietrzyk.androidteacher.databaseSqlite

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskDatabaseDao {

    @Insert
    fun insert(task: Task)

    @Update
    fun update(task: Task)

    @Query("SELECT * from tasks_table WHERE taskId = :key")
    fun get(key: Long): Task?

    @Query("SELECT * from tasks_table WHERE taskId=:key")
    fun getTaskWithId(key:Long): LiveData<Task>

    @Query("DELETE FROM tasks_table")
    fun clear()

    @Query("SELECT * FROM tasks_table ORDER BY taskId DESC")
    fun getAllTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM tasks_table ORDER BY taskId DESC LIMIT 1")
    fun getTask(): Task?

    @Query("DELETE FROM tasks_table WHERE taskId=:key")
    fun deleteByTaskId(key: Long)
}