package olga.pietrzyk.androidteacher.tasks

import android.content.res.Resources
import android.os.Build
import android.text.Html
import android.text.Spanned
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import olga.pietrzyk.androidteacher.R
import olga.pietrzyk.androidteacher.databaseSqlite.Task

fun formatTasks(task: List<Task?>, resources: Resources): Spanned {
    val sb = StringBuilder()
    sb.apply {
        append(
        task.forEach {
            append(resources.getString(R.string.title))

            append("<br>")
            append(resources.getString(R.string.article_title))
            append("\t${it!!.taskId}<br>")
            append("\t${it!!.taskTitle}<br>")
        })
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        return Html.fromHtml(sb.toString(), Html.FROM_HTML_MODE_LEGACY)
    } else {
        return HtmlCompat.fromHtml(sb.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}

