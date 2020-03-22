package olga.pietrzyk.androidteacher.camera


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.*
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import olga.pietrzyk.androidteacher.R
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CameraFragment : Fragment() {
    private lateinit var imageView: ImageView
    private lateinit var buttonTakePicture: Button
    private lateinit var buttonUploadPicture: Button
    private var photoFile: File? = null
    private val CAPTURE_IMAGE_REQUEST = 1
    private val REQUEST_CODE = 0
    private lateinit var mCurrentPhotoPath: String
    private val GALLERY = 2
    private val firstGrant= 0
    private val secondGrant = 1
    lateinit var currentPhotoUrl: Uri
    private lateinit var  mStorageRef: StorageReference
    private lateinit var viewModel: CameraViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_camera, container, false)
        viewModel = ViewModelProviders.of(this).get(CameraViewModel::class.java)

        buttonTakePicture = view.findViewById(R.id.take_picture_button)
        buttonUploadPicture = view.findViewById(R.id.upload_picture_button)
        imageView = view!!.findViewById(R.id.image_camera)
        mStorageRef = FirebaseStorage.getInstance().reference

        viewModel.photoFile.observe(viewLifecycleOwner, Observer{ viewModelPhotoFile ->
            photoFile = viewModelPhotoFile
            if(photoFile!=null){
                val myBitmap = BitmapFactory.decodeFile(photoFile!!.absolutePath)
                imageView.setImageBitmap(myBitmap)
            }
        })

        buttonTakePicture.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                captureImage()
            }
        }

        buttonUploadPicture.setOnClickListener {
            openGalleryForImage()
        }
        return view
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

                            if ( photoFile != null) {
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
                        }catch (ex: Exception) {
                      Toast.makeText(context!!,resources.getString(R.string.image_bug)  + ex.message.toString(), Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(context!!,resources.getString(R.string.null_value) + activity!!.baseContext, Toast.LENGTH_LONG).show()
                }
            }
        }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = resources.getString(R.string.image)
        startActivityForResult(Intent.createChooser(intent, resources.getString(R.string.gallery)), GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAPTURE_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {

            imageView = view!!.findViewById(R.id.image_camera)
            viewModel.photoFile.observe(viewLifecycleOwner, Observer{ p ->
                Log.i("TAL", "${p} and ${photoFile}")
                photoFile=p
            })

            val myBitmap = BitmapFactory.decodeFile(photoFile!!.absolutePath)
            imageView.setImageBitmap(myBitmap)
        } else if (resultCode == Activity.RESULT_OK && requestCode == GALLERY) {

            val returnUri = data!!.data
            imageView = view!!.findViewById(R.id.image_camera);
            returnUri!!.let {
                val s = activity!!.contentResolver.openInputStream(returnUri)
                val b = BitmapFactory.decodeStream(s)
                imageView.setImageBitmap(b)

                val progressDialog = ProgressDialog(context!!)
                progressDialog.setTitle(resources.getString(R.string.upload_info))
                progressDialog.show()

                val uri = returnUri.toString()
                val filePath = mStorageRef.child("photos/${uri}")
                filePath.putFile(returnUri).addOnSuccessListener{
                    progressDialog.dismiss()
                    Toast.makeText(context!!, resources.getString(R.string.sucesful_upload), Toast.LENGTH_LONG).show()
                }.addOnFailureListener{
                    progressDialog.dismiss()
                    Toast.makeText(context!!, resources.getString(R.string.fail_upload), Toast.LENGTH_LONG).show()
                }.addOnProgressListener{
                    Toast.makeText(context!!, resources.getString(R.string.upload_progress), Toast.LENGTH_LONG).show()
                }
            }
        } else {
            Toast.makeText(context!!,resources.getString(R.string.request_error), Toast.LENGTH_LONG).show()
        }
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat(resources.getString(R.string.yyyyMMdd_HHmmss)).format(Date())
        val imageFileName = resources.getString(R.string.JPEG)+ timeStamp + "_"
        val storageDir = getActivity()!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
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
            Toast.makeText(context!!,resources.getString(R.string.null_value) +getActivity()!!.getBaseContext(), Toast.LENGTH_LONG).show()
        }
    }


}

