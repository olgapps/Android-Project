package olga.pietrzyk.androidteacher.tasks

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import olga.pietrzyk.androidteacher.R
import olga.pietrzyk.androidteacher.databaseSqlite.TaskDatabase
import olga.pietrzyk.androidteacher.login.LoginViewModel

class TaskDialogFragment() : DialogFragment() {

    //
    private lateinit var title: String
    private lateinit var content: String
    private var taskId: Long =-1
    //private  var taskViewModel=taskViewModel


   override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        content = arguments?.getString(CONTENT).toString()
        title = arguments?.getString(TITLE).toString()
        taskId = arguments?.getLong(ID)!!.toLong()
    }


    companion object {
        private const val CONTENT = "content"
        private const val TITLE = "title"
        private const val ID= "ID"

        fun newInstance(
            title: String?, content: String?, taskId: Long
        ): TaskDialogFragment = TaskDialogFragment().apply {
            arguments = Bundle().apply {
                putLong(ID, taskId)
                putString(TITLE, title)
                putString(CONTENT, content)
            }
        }
    }





    //lateinit var task: Task
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {


        val taskViewModel = TaskFragment.taskViewModel
       // val taskViewModel = ViewModelProviders.of(this, viewModelFactory).get(TaskViewModel::class.java)


        return activity?.let {

            val builder = AlertDialog.Builder(it)
            builder.setTitle(" ${title}")
            builder.setMessage("${content}")



            builder.setNegativeButton(getResources().getString(R.string.delete)){ dialog, which ->
                taskViewModel.deleteTask(taskId)
                Toast.makeText(context,getResources().getString(R.string.task_removed), Toast.LENGTH_SHORT).show()

            }

            builder.setPositiveButton(getResources().getString(R.string.done)){ dialog, which ->
                taskViewModel.updateById(taskId)
                Toast.makeText(context,getResources().getString(R.string.congratulations),
                    Toast.LENGTH_SHORT).show()

            }


            builder.create()



        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
