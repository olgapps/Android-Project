package olga.pietrzyk.androidteacher.camera

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import olga.pietrzyk.androidteacher.R

class PhotoAdapter(val adapterContext: Context, var photos: MutableList<Photo>) :
    RecyclerView.Adapter<PhotoAdapter.PhotosViewHolder>() {

    var tasks = listOf<Photo>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_photo, parent, false)
        return PhotosViewHolder(view)
    }

    override fun getItemCount(): Int {
        return photos.size
    }

    override fun onBindViewHolder(holder: PhotosViewHolder, position: Int) {
        val uploadCurrent = photos[position]
        Picasso.with(adapterContext).load(uploadCurrent.uri).into(holder.imageView)
    }

    class PhotosViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView? = itemView.findViewById(R.id.image_view_upload)
    }
}