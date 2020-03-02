package olga.pietrzyk.androidteacher.tasks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_task.*
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
        holder.taskTitle.text = item.taskTitle.toString()
        //holder.taskContent.text = item.taskContent.toString()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.list_item_task, parent, false)
        return ViewHolder(view)
    }

    class ViewHolder(taskView: View): RecyclerView.ViewHolder(taskView){
        val taskTitle: TextView = taskView.findViewById(R.id.item_task_title)
       // val taskContent: TextView = taskView.findViewById(R.id.item_task_content)
        val taskStatus: CheckBox = taskView.findViewById(R.id.task_status)
    }
}