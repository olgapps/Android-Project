package olga.pietrzyk.androidteacher.tasks


import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import olga.pietrzyk.androidteacher.R
import olga.pietrzyk.androidteacher.databaseSqlite.TaskDatabase
import olga.pietrzyk.androidteacher.databinding.FragmentTaskBinding

/**
 * A simple [Fragment] subclass.
 */
class TaskFragment : Fragment() {
    companion object{
        lateinit var taskViewModel: TaskViewModel
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val binding: FragmentTaskBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_task, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = TaskDatabase.getInstance(application).taskDatabaseDao

        val viewModelFactory = TaskViewModelFactory(dataSource, application)

        taskViewModel = ViewModelProviders.of(this, viewModelFactory).get(TaskViewModel::class.java)



        binding.startButton.setOnClickListener {
            val taskTitle = binding.taskTitleFromUser.text.toString()
            val taskContent = binding.taskContentFromUser.text.toString()
            taskViewModel.addTaskTitle(taskTitle, taskContent)
        }


        binding.taskViewModel=taskViewModel

        binding.setLifecycleOwner (this)


        val manager =GridLayoutManager(activity, 3)
        binding.tasksList.layoutManager=manager




        val builder = AlertDialog.Builder(context)

        builder.setView(view)


        val adapter = TaskAdapter(TaskListener { task->

          val fm = activity!!.supportFragmentManager
          val f = TaskDialogFragment.newInstance(task.taskTitle,task.taskContent, task.taskId);

          f.show(fm, "TaskDialogFragment_tag")


/*
          builder.setTitle(" ${task.taskTitle}")
          builder.setMessage("${task.taskContent}")

          builder.setNegativeButton(getResources().getString(R.string.delete)){dialog,which ->
              Toast.makeText(context,getResources().getString(R.string.task_removed),Toast.LENGTH_SHORT).show()
              taskViewModel.deleteTask(task.taskId)
          }

          builder.setPositiveButton(getResources().getString(R.string.done)){dialog, which ->
              taskViewModel.updateById(task.taskId)
              Toast.makeText(context,getResources().getString(R.string.congratulations),Toast.LENGTH_SHORT).show()


          }

      val alert = builder.create()
      alert.show()
        */
  })
  binding.tasksList.adapter=adapter

  taskViewModel.tasks.observe(viewLifecycleOwner, Observer{
      it?.let{
          adapter.tasks=it
      }
  })


  return binding.root
}

override fun onActivityCreated(savedInstanceState: Bundle?) {
  super.onActivityCreated(savedInstanceState)
  val inn =getActivity()!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
  inn.hideSoftInputFromWindow(view!!.windowToken, 0)


}
}
