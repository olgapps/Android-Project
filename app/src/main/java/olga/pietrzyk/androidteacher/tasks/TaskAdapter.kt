package olga.pietrzyk.androidteacher.tasks


import android.view.LayoutInflater
import android.view.View
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
       return tasks.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item =tasks[position]

        val res = holder.itemView.context.resources

        holder.bind(item, clickListener)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder(val binding: ListItemTaskBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(
        item: Task, clickListener: TaskListener
        ) {
            binding.task=item

            binding.clickListener=clickListener
            binding.itemTaskTitle.text = item.taskTitle.toString()
            binding.executePendingBindings()
            if(item.taskStatus==true){
                binding.taskStatus.visibility= View.GONE
                binding.taskStatusChecked.visibility= View.VISIBLE
            }else{
                binding.taskStatusChecked.visibility= View.GONE
                binding.taskStatus.visibility= View.VISIBLE
                }
            }


        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemTaskBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class TaskListener(val clickListener: (taskId: Task)-> Unit){
    fun onClick(task:Task)=clickListener(task)
}