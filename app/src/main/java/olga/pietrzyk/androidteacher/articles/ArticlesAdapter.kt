package olga.pietrzyk.androidteacher.articles

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import olga.pietrzyk.androidteacher.R
import olga.pietrzyk.androidteacher.TextItemViewHolder
import olga.pietrzyk.androidteacher.database.Article

class ArticlesAdapter: RecyclerView.Adapter<ArticlesAdapter.ViewHolder>(){
    var data = listOf<Article>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int {
       return data.size//To change body of created functions use File | Settings | File Templates.
    }
    //tell Recycler view how to actually draw an item
    //holder is an TextItemViewHolder which is a
    /*generic type we specified on the Recycler View Adapter
     and positio n is just a position in the list we are supposed to be binding*/
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]

        val res = holder.itemView.context.resources
       // holder.articleGrade.text =
        //holder.textView.text=item.aticleTitle.toString()

        holder.articleTitle.text=item.aticleTitle.toString()
        holder.gradeImage.setImageResource(when (item.articleRate){
            -1 -> R.drawable.ic_plain_yellow_star1
            0-> R.drawable.ic_plain_yellow_star2
            1-> R.drawable.ic_plain_yellow_star3
            else -> R.drawable.ic_plain_yellow_star3
        })

    }

    //tell recycler view how to create a new view holder
    /**recycler does not really cares about views it does everything in terms of VIEW HOLDERS
    we can think of view holders mostly the same thing as the view it holds
    the name is on create view holder


     it takes a parent which is a view group as well as view type which is a hint

     code when recycler view needs a new ViewHolder of the given typ to represent an item
     whenever recycler view needs a new view holder it will ask for one
     our job is to give it one whenever it asks

     it might need a new view holder if it jus start up and its displaying a the first item
     or if the number of views on screen increased



        PARENT -> a view group into which the new view will be added after it is bound to adapter position
                    this view will be added to some viewGroup before it gets dispalyed to the screen
    A ViewGroup is a type of view that holds a group of views-> in reality this will always be a Recycler View
     Recycler view hands us the parent because we will need that to actually create a view holder
     the viewType is used when there are multiple different views in the same Recycler View


     */

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        /*to make a view holder you neet to make a view for it to hold
        *to inflate a layout form xml you use a layout inflator
        *from parent.context -> you will create a layout inflator based on the parent view
        *  */
        val layoutInflater= LayoutInflater.from(parent.context)
        /*we just use the layout infaltor object to inflate text item view
        * you need to pass a parent that recycyler view gave us
        * the layou inflator will make sure it sets the view up correctly for the parrent  passed
        * false - > this parameter is attached to root, root just means parent
        *   since recycycler view will add this item for us when it is time it will thow an exception if we added iit now
        *
       */
        val view =layoutInflater.inflate(R.layout.list_item_article, parent, false)
        return ViewHolder(view)

        //
    }
    //we need to let the recycler view know when the data changes
    // recycyler view does not really care about what data it is displying
    // it just uses the adapter to bind items that should be on the screen right now
    //you wiil use a custom setter to the data variable



    /**we will make a class inside a Adapter called View Holder */
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        //every time you bind view holder you need to acces the image and both TextViews
        //this way when we are binding this view holder we we do not need to look up at id...length every time
        //we can retreive the view by the findView by id
        //firt we will pull out sleep lenth from the item view
        //later we will update this using data binding
        //but we want to get working view holder as much as possible
        var articleTitle: TextView = itemView.findViewById(R.id.txt_article_title)
        val gradeImage: ImageView=itemView.findViewById(R.id.img_stars)
        //when we are binding this view holders we can just use these properties to get directly to the views that we need to update

    }
}