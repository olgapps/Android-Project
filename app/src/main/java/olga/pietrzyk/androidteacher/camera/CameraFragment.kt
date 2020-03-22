package olga.pietrzyk.androidteacher.camera

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import olga.pietrzyk.androidteacher.R
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CameraFragment : Fragment() {
    private lateinit var imageView: ImageView
    private lateinit var buttonTakePicture: ImageView
    private lateinit var buttonUploadPicture: ImageView
    private var photoFile: File? = null
    private var photoUri: Uri? = null
    private val CAPTURE_IMAGE_REQUEST = 1
    private val REQUEST_CODE = 0
    private lateinit var mCurrentPhotoPath: String
    private val GALLERY = 2
    private val firstGrant = 0
    private val secondGrant = 1
    lateinit var currentPhotoUrl: Uri
    private lateinit var mStorageRef: StorageReference
    private lateinit var viewModel: CameraViewModel
    private lateinit var mDatabaseRef: DatabaseReference
    private lateinit var photosRecyclerView: RecyclerView
    private lateinit var photosList: MutableList<Photo>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_camera, container, false)
        viewModel = ViewModelProviders.of(this).get(CameraViewModel::class.java)

        buttonTakePicture = view.findViewById(R.id.camera_icon)
        buttonUploadPicture = view.findViewById(R.id.gallery_icon)
        imageView = view!!.findViewById(R.id.image_camera)

        mStorageRef = FirebaseStorage.getInstance().getReference("photos")
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("photos")

        photosRecyclerView = view.findViewById(R.id.photos_recycler_view)
        photosList = mutableListOf()

        readPhotosUriFromDatabase()
        observePhotoFile()
        observePhotoUri()

        buttonTakePicture.setOnClickListener {
            captureImage()
        }

        buttonUploadPicture.setOnClickListener {
            openGalleryForImage()
        }
        return view
    }

    private fun observePhotoFile() {
        viewModel.photoFile.observe(viewLifecycleOwner, Observer { viewModelPhotoFile ->
            photoFile = viewModelPhotoFile
            if (photoFile != null) {
                setImageByBitmap()
            }
        })
    }

    private fun setImageByBitmap() {
        val myBitmap = BitmapFactory.decodeFile(photoFile!!.absolutePath)
        imageView.setImageBitmap(myBitmap)
    }

    private fun observePhotoUri() {
        viewModel.photoUri.observe(viewLifecycleOwner, Observer { viewModelPhotoUri ->
            photoUri = viewModelPhotoUri
            if (photoUri != null) {
                Picasso.with(context).load(photoUri).into(imageView)
            }
        })
    }

    private fun readPhotosUriFromDatabase() {
        mDatabaseRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                photosList.clear()
                for (p in p0.children) {

                    val photo = p.getValue(Photo::class.java)
                    photosList.add(photo!!)
                }
                photosRecyclerView.apply {
                    layoutManager = LinearLayoutManager(activity)
                    adapter = PhotoAdapter(context!!, photosList)
                }
            }
        })
    }

    private fun captureImage() {
        if (ContextCompat.checkSelfPermission(
                context!!,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity!!,
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_CODE
            )
        } else {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(activity!!.packageManager) != null) {
                try {
                    photoFile = createImageFile()
                    if (photoFile != null) {
                        val currentPhotoFile = photoFile
                        currentPhotoFile!!.let {
                            val photoURI = FileProvider.getUriForFile(
                                context!!,
                                resources.getString(R.string.authority),
                                currentPhotoFile
                            )
                            photoFile = currentPhotoFile
                            viewModel.photoFile.value = photoFile
                            currentPhotoUrl = photoURI

                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, currentPhotoUrl)
                            startActivityForResult(takePictureIntent, CAPTURE_IMAGE_REQUEST)
                        }
                    }
                } catch (ex: Exception) {
                    informUser(resources.getString(R.string.image_bug) + ex.message.toString())
                }
            } else {
                informUser(resources.getString(R.string.null_value) + activity!!.baseContext)
            }
        }
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = resources.getString(R.string.image)
        startActivityForResult(
            Intent.createChooser(intent, resources.getString(R.string.gallery)),
            GALLERY
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAPTURE_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            imageView = view!!.findViewById(R.id.image_camera)
            viewModel.photoFile.observe(viewLifecycleOwner, Observer { photo ->
                photoFile = photo
            })
            val myBitmap = BitmapFactory.decodeFile(photoFile!!.absolutePath)
            imageView.setImageBitmap(myBitmap)
            storagePictureInFirebase(Uri.fromFile(photoFile))

        } else if (resultCode == Activity.RESULT_OK && requestCode == GALLERY) {

            val returnUri = data!!.data
            viewModel.photoUri.value = returnUri

            Picasso.with(context).load(returnUri).into(imageView)
            storagePictureInFirebase(returnUri)

        } else {
            informUser(resources.getString(R.string.request_error))
        }
    }

    private fun storagePictureInFirebase(returnUri: Uri?) {
        val id = mDatabaseRef.push().key
        mStorageRef.child("photos/${id}").putFile(returnUri!!).addOnCompleteListener {
            if (it.isSuccessful) {
                mStorageRef.child("photos/${id}").downloadUrl.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val storageUri = task.result
                        val photoUpload = Photo(id, storageUri!!.toString())
                        mDatabaseRef.child(id!!).setValue(photoUpload)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    informUser(resources.getString(R.string.sucesfull_db))
                                } else {
                                    informUser(resources.getString(R.string.not_sucesfull_db))
                                }
                            }.addOnFailureListener {
                                informUser(resources.getString(R.string.not_sucesfull_db))
                            }
                    } else {
                        informUser(resources.getString(R.string.not_sucesfull_db))
                    }
                }
            } else {
                informUser(resources.getString(R.string.not_sucesfull_db))
            }
        }.addOnFailureListener {
            informUser(resources.getString(R.string.not_sucesfull_db))
        }
    }

    private fun informUser(text: String) {
        Toast.makeText(
            context!!,
            text,
            Toast.LENGTH_LONG
        ).show()
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp =
            SimpleDateFormat(resources.getString(R.string.yyyyMMdd_HHmmss)).format(Date())
        val imageFileName = resources.getString(R.string.JPEG) + timeStamp + "_"
        val storageDir = activity!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName,
            resources.getString(R.string.jpg),
            storageDir
        )
        mCurrentPhotoPath = image.absolutePath
        return image
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[firstGrant] == PackageManager.PERMISSION_GRANTED
                && grantResults[secondGrant] == PackageManager.PERMISSION_GRANTED
            ) {
                captureImage()
            }
        } else {
            informUser(resources.getString(R.string.null_value) + activity!!.baseContext)
        }
    }
}

