package olga.pietrzyk.androidteacher.tasks

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.core.content.res.TypedArrayUtils.getString

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import kotlinx.android.synthetic.main.fragment_task.view.*
import kotlinx.coroutines.*
import olga.pietrzyk.androidteacher.R
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
    var taskTitle = "nic nie przeczytałem"
    var taskContnet = "nic nie przeczytałem"

    var currentTask= MutableLiveData<Task?>()



    val taskStringFormat = Transformations.map(tasks) { newTask ->
        formatTasks(newTask, applicaton.resources)}

    init{
        initializeTask()
    }

    fun addTaskTitle(title: String,content: String){
        taskTitle = title
        taskContnet = content
        onCreateTask()
    }



    fun initializeTask(){
        uiScope.launch{
            task.value = getTaskFromDatabase()

        }
    }

    fun deleteTask(taskId : Long){
        uiScope.launch{
            deleteTaskById(taskId)
        }
    }

    fun getTaskByID(id: Long){
        uiScope.launch{
            currentTask.value = getTaskByIdFromDatabase(id)
            Log.i("aaaaaaaaaaaaa", "${currentTask.value!!.taskTitle}")

        }
    }



    fun onCreateTask(){
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

    /*fun getChosenTask(Id: Long): Task{
        uiScope.launch{
            withContext(Dispatchers.IO){
                val task = database.get(Id) ?: return@withContext
                task
            }
        }

    }*/

    fun updateById(Id: Long) {
        uiScope.launch {
            // IO is a thread pool for running operations that access the disk, such as
            // our Room database.
            withContext(Dispatchers.IO) {
                val task = database.get(Id) ?: return@withContext
                //currentTask=task
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

