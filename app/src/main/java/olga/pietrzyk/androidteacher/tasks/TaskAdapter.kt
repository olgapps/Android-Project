package olga.pietrzyk.androidteacher.tasks


import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import olga.pietrzyk.androidteacher.databaseSqlite.Task
import olga.pietrzyk.androidteacher.databinding.ListItemTaskBinding

class TaskAdapter(val clickListener: TaskListener) : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {



    var tasks = listOf<Task>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int {
       return tasks.size //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item =tasks[position]

        val res = holder.itemView.context.resources
        //holder.bind(item)
        holder.bind(item, clickListener)

        //holder.

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder(val binding: ListItemTaskBinding): RecyclerView.ViewHolder(binding.root){
        //val taskContent: TextView = taskView.findViewById(R.id.item_task_content)
        //val taskStatus: CheckBox = binding.taskStatus

        fun bind(
        item: Task, clickListener: TaskListener
        ) {
            binding.task=item
            binding.itemTaskTitle.text = item.taskStatus.toString()
            binding.clickListener=clickListener
            binding.executePendingBindings()
            // binding.taskContent.text = item.taskContent.toString()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                //val view = layoutInflater.inflate(R.layout.list_item_task, parent, false)
                val binding = ListItemTaskBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class TaskListener(val clickListener: (taskId: Long)-> Unit){
    fun onClick(task:Task)=clickListener(task.taskId)
}