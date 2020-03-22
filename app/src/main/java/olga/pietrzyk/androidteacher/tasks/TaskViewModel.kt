package olga.pietrzyk.androidteacher.tasks

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import olga.pietrzyk.androidteacher.databaseSqlite.Task
import olga.pietrzyk.androidteacher.databaseSqlite.TaskDatabaseDao

class TaskViewModel(val database: TaskDatabaseDao, val applicaton: Application) : AndroidViewModel(applicaton){
 private var viewModelJob= Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    var task = MutableLiveData<Task?>()
    val tasks = database.getAllTasks()
    var taskTitle = ""
    private var taskContnet = ""
    var currentTask= MutableLiveData<Task?>()

    init{
        initializeTask()
    }

    fun addTaskTitle(title: String,content: String){
        taskTitle = title
        taskContnet = content
        onCreateTask()
    }

    private fun initializeTask(){
        uiScope.launch{
            task.value = getTaskFromDatabase()
        }
    }

    fun deleteTask(taskId : Long){
        uiScope.launch{
            deleteTaskById(taskId)
        }
    }

    private fun onCreateTask(){
        uiScope.launch{
            val aTask = Task(taskTitle,taskContnet, false)
            insert(aTask)
            task.value = getTaskFromDatabase()
        }
    }

    fun deleteAllTasks() {

        uiScope.launch {
            clear()
            task.value = null
        }
    }

    fun updateById(Id: Long) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                val task = database.get(Id) ?: return@withContext
                task.taskStatus = true
                database.update(task)
            }
        }
    }

    private suspend fun getTaskFromDatabase(): Task?{
        return withContext(Dispatchers.IO){
            var task = database.getTask()
            task
        }
    }

    private suspend fun getTaskByIdFromDatabase(id : Long): Task?{
        return withContext(Dispatchers.IO){
            var taskId = async { database.get(id) }
            taskId.await()
        }
    }

    private suspend fun insert(task: Task){
        withContext(Dispatchers.IO){
            database.insert(task)
        }
    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.clear()
        }
    }

    private suspend fun deleteTaskById(taskId: Long) {
        withContext(Dispatchers.IO) {
            database.deleteByTaskId(taskId)
        }
    }
}

