package olga.pietrzyk.androidteacher.articles

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import olga.pietrzyk.androidteacher.R
import olga.pietrzyk.androidteacher.database.Article

@BindingAdapter("setArticleTitle")
fun TextView.setArticleTitle(item: Article?){
    item?.let{
        text=item.aticleTitle.toString()
    }
}

@BindingAdapter("setArticleContent")
fun TextView.setArticleContent(item: Article?){
    item?.let{
        text=item.articleContent.toString()
    }
}

@BindingAdapter("setStarsImage")
fun ImageView.setStarsImage(item: Article){
    item?.let{
        setImageResource(when(item.articleRate){
            -1 -> R.drawable.ic_plain_yellow_star1
            0 -> R.drawable.ic_plain_yellow_star2
            1 -> R.drawable.ic_plain_yellow_star3
            else -> R.drawable.ic_plain_yellow_star3
        })
    }

}