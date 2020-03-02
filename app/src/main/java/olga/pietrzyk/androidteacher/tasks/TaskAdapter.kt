package olga.pietrzyk.androidteacher.tasks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import olga.pietrzyk.androidteacher.R
import olga.pietrzyk.androidteacher.databaseSqlite.Task

class TaskAdapter : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

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
        holder.bind(item)
        //holder.

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder (taskView: View): RecyclerView.ViewHolder(taskView){
        val taskTitle: TextView = taskView.findViewById(R.id.item_task_title)
        //val taskContent: TextView = taskView.findViewById(R.id.item_task_content)
        val taskStatus: CheckBox = taskView.findViewById(R.id.task_status)

        fun bind(
        item: Task
        ) {
            taskTitle.text = item.taskTitle.toString()
            // taskContent.text = item.taskContent.toString()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.list_item_task, parent, false)
                return ViewHolder(view)
            }
        }
    }


}