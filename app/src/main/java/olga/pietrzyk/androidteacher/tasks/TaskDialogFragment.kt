package olga.pietrzyk.androidteacher.tasks

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import olga.pietrzyk.androidteacher.R
import olga.pietrzyk.androidteacher.databaseSqlite.TaskDatabase
import olga.pietrzyk.androidteacher.login.LoginViewModel

class TaskDialogFragment() : DialogFragment() {

    private lateinit var title: String
    private lateinit var content: String
    private var taskId: Long =-1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        content = arguments?.getString(CONTENT).toString()
        title = arguments?.getString(TITLE).toString()
        taskId = arguments?.getLong(ID)!!.toLong()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_fragment_task, container, false)
        content = arguments?.getString(CONTENT).toString()
        title = arguments?.getString(TITLE).toString()
        taskId = arguments?.getLong(ID)!!.toLong()

        val dialog_title = view!!.findViewById<TextView>(R.id.dialog_title)
        val dialog_content = view.findViewById<TextView>(R.id.dialog_content)

        dialog_title.setText(" ${title}")
        dialog_content.setText(" ${content}")

        return view
    }

    companion object {
        private const val CONTENT = "content"
        private const val TITLE = "title"
        private const val ID = "ID"

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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val inflater = requireActivity().layoutInflater;
        val taskViewModel = TaskFragment.taskViewModel

        return activity?.let {

            val builder = AlertDialog.Builder(it)
            val builderView = inflater.inflate(R.layout.dialog_fragment_task, null)
            builder.setView(builderView)

            val dialog_title = builderView!!.findViewById<TextView>(R.id.dialog_title)
            val dialog_content = builderView!!.findViewById<TextView>(R.id.dialog_content)

            dialog_title.setText(" ${title}")
            dialog_content.setText(" ${content}")

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
