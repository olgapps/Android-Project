package olga.pietrzyk.androidteacher

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import olga.pietrzyk.androidteacher.databaseSqlite.Task
import olga.pietrzyk.androidteacher.databaseSqlite.TaskDatabase
import olga.pietrzyk.androidteacher.databaseSqlite.TaskDatabaseDao
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import java.io.IOException

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class AndroidDatabaseTest {

    private lateinit var articleDao: TaskDatabaseDao
    private lateinit var db: TaskDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, TaskDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        articleDao = db.taskDatabaseDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetNight() {
        val task = Task(1, "a","b",true)
        articleDao.insert(task)
        val tonight = articleDao.getTask()
        assertEquals(tonight?.taskStatus, true)
    }
}
