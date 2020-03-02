package olga.pietrzyk.androidteacher.tasks


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
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

        val adapter = TaskAdapter()
        val manager =GridLayoutManager(activity, 4)
        binding.tasksList.layoutManager=manager

        binding.tasksList.adapter=adapter

        taskViewModel.tasks.observe(viewLifecycleOwner, Observer{
            it?.let{
                adapter.tasks=it
            }
        })

        return binding.root
    }


}
