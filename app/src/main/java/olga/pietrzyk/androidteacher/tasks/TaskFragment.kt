package olga.pietrzyk.androidteacher.tasks


import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import olga.pietrzyk.androidteacher.R
import olga.pietrzyk.androidteacher.databaseSqlite.TaskDatabase
import olga.pietrzyk.androidteacher.databinding.FragmentTaskBinding

class TaskFragment : Fragment() {
    companion object {
        lateinit var taskViewModel: TaskViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentTaskBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_task, container, false)
        val application = requireNotNull(this.activity).application
        val dataSource = TaskDatabase.getInstance(application).taskDatabaseDao
        val viewModelFactory = TaskViewModelFactory(dataSource, application)
        val gridNumber = 3

        taskViewModel = ViewModelProviders.of(this, viewModelFactory).get(TaskViewModel::class.java)

        binding.startButton.setOnClickListener {
            val taskTitle = binding.taskTitle.text.toString()
            val taskContent = binding.taskDescription.text.toString()
            taskViewModel.addTaskTitle(taskTitle, taskContent)
        }

        binding.taskViewModel = taskViewModel
        binding.lifecycleOwner = this

        val manager = GridLayoutManager(activity, gridNumber)
        binding.tasksList.layoutManager = manager

        val builder = AlertDialog.Builder(context)
        builder.setView(view)

        val adapter = TaskAdapter(TaskListener { task ->
            val fragmentManager = activity!!.supportFragmentManager
            val taskDialogFragment =
                TaskDialogFragment.newInstance(task.taskTitle, task.taskContent, task.taskId)
            taskDialogFragment.show(fragmentManager, "TaskDialogFragment_tag")
        })

        binding.tasksList.adapter = adapter
        taskViewModel.tasks.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.tasks = it
            }
        })
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val inn = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inn.hideSoftInputFromWindow(view!!.windowToken, 0)
    }
}
