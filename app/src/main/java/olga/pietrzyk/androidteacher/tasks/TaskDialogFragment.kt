package olga.pietrzyk.androidteacher.tasks

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import olga.pietrzyk.androidteacher.R

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

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_fragment_task, container, false)
        content = arguments?.getString(CONTENT).toString()
        title = arguments?.getString(TITLE).toString()
        taskId = arguments?.getLong(ID)!!.toLong()

        val dialogTitle = view!!.findViewById<TextView>(R.id.dialog_title)
        val dialogContent = view.findViewById<TextView>(R.id.dialog_content)

        dialogTitle.text = " $title"
        dialogContent.text = " $content"

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

    @SuppressLint("SetTextI18n")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val inflater = requireActivity().layoutInflater;
        val taskViewModel = TaskFragment.taskViewModel

        return activity?.let {

            val builder = AlertDialog.Builder(it)
            val builderView = inflater.inflate(R.layout.dialog_fragment_task, null)
            builder.setView(builderView)

            val dialogTitle = builderView!!.findViewById<TextView>(R.id.dialog_title)
            val dialogContent = builderView.findViewById<TextView>(R.id.dialog_content)

            dialogTitle.text = " $title"
            dialogContent.text = " $content"

            builder.setNegativeButton(resources.getString(R.string.delete)){ _, _ ->
                taskViewModel.deleteTask(taskId)
                Toast.makeText(context, resources.getString(R.string.task_removed), Toast.LENGTH_SHORT).show()
            }

            builder.setPositiveButton(resources.getString(R.string.done)){ _, _ ->
                taskViewModel.updateById(taskId)
                Toast.makeText(context, resources.getString(R.string.congratulations),
                    Toast.LENGTH_SHORT).show()

            }
            builder.create()

        } ?: throw IllegalStateException(resources.getString(R.string.cannot_be_null))
    }
}
