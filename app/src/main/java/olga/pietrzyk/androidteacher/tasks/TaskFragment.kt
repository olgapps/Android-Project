package olga.pietrzyk.androidteacher.tasks


import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.tasks.Tasks.await
import olga.pietrzyk.androidteacher.R
import olga.pietrzyk.androidteacher.databaseSqlite.TaskDatabase
import olga.pietrzyk.androidteacher.databinding.FragmentTaskBinding

/**
 * A simple [Fragment] subclass.
 */
class TaskFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentTaskBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_task, container, false)

        // po co to
        val application = requireNotNull(this.activity).application

        //we need a reference to a database we are reference to a dao
        val dataSource = TaskDatabase.getInstance(application).taskDatabaseDao

        //instance of sleep tracker view factory and we pass it a data source
        val viewModelFactory = TaskViewModelFactory(dataSource, application)

        val taskViewModel = ViewModelProviders.of(this, viewModelFactory).get(TaskViewModel::class.java)



        binding.startButton.setOnClickListener {
            val taskTitle = binding.taskTitleFromUser.text.toString()
            val taskContent = binding.taskContentFromUser.text.toString()
            taskViewModel.addTaskTitle(taskTitle, taskContent)
        }



        //this is necessary so that the live data can observe live data updates
        binding.taskViewModel=taskViewModel

        binding.setLifecycleOwner (this)


        val manager =GridLayoutManager(activity, 3)
        binding.tasksList.layoutManager=manager

        //val alert =makeWindow(taskViewModel)
       // taskViewModel.task.observe(viewLifecycleOwner, Observer { newWord ->
        val builder = AlertDialog.Builder(context)
        //var a = "kupa"





        builder.setView(view)


        val adapter = TaskAdapter(TaskListener { task->

          taskViewModel.getTaskByID(task.taskId)


           // taskViewModel.currentTask.observe(viewLifecycleOwner, Observer{
               // newCurrentTask->

                Toast.makeText(context, "${task} and ", Toast.LENGTH_LONG).show()
                // alert.show()
                builder.setTitle(" ${task.taskTitle}")
                builder.setMessage("${task.taskContent}")
                //
                builder.setNegativeButton("DELETE"){dialog,which ->
                    Toast.makeText(context,"You are not agree.",Toast.LENGTH_SHORT).show()
                    taskViewModel.deleteTask(task.taskId)
                }

                builder.setPositiveButton("DONE"){dialog, which ->
                    taskViewModel.updateById(task.taskId)
                    Toast.makeText(context,"Ok, we change the app background.",Toast.LENGTH_SHORT).show()


                }
           // })

            val alert = builder.create()
            alert.show()

        })
        binding.tasksList.adapter=adapter




        taskViewModel.tasks.observe(viewLifecycleOwner, Observer{
            it?.let{
                adapter.tasks=it
            }
        })
   // })
        return binding.root
    }

    fun makeWindow(
        viewModel: TaskViewModel
    ):AlertDialog
    {
        val builder = AlertDialog.Builder(context)
        //var a = "kupa"
        //val taskTitle= taskViewModel.tasks[taskId.toInt()]
        viewModel.task.observe(viewLifecycleOwner, Observer { newWord ->
            newWord!!.taskTitle.toString()
            Log.i("AAAAAAAAAAAAA","${newWord!!.taskTitle}")

            Toast.makeText(context, "${newWord!!.taskTitle}", Toast.LENGTH_LONG).show()


        })

        builder.setTitle("Update Task")
        builder.setView(view)

        builder.setPositiveButton("DONE"){dialog, which ->
            // Do something when user press the positive button
            Toast.makeText(context,"Ok, we change the app background.",Toast.LENGTH_SHORT).show()


            // Change the app background color

        }


        // Display a negative button on alert dialog
        builder.setNegativeButton("DELETE"){dialog,which ->
            Toast.makeText(context,"You are not agree.",Toast.LENGTH_SHORT).show()
        }
        val alert = builder.create()
        return alert
        //alert.show()

    }

}
