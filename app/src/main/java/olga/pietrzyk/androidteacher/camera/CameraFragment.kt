package olga.pietrzyk.androidteacher.camera


import android.Manifest
import android.app.Activity
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater!!.inflate(R.layout.fragment_camera, container, false)

        buttonTakePicture = view.findViewById(R.id.take_picture_button);
        buttonUploadPicture = view.findViewById(R.id.upload_picture_button);

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

            photoFile = createImageFile()

            if (photoFile != null) {
                val currentPhotoFile = photoFile
                currentPhotoFile!!.let {
                    var photoURI = FileProvider.getUriForFile(
                        context!!,
                        resources.getString(R.string.authority),
                        currentPhotoFile
                    )
                    photoFile = currentPhotoFile
                    currentPhotoUrl = photoURI

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, currentPhotoUrl)
                    startActivityForResult(takePictureIntent, CAPTURE_IMAGE_REQUEST)
                }
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
        if (requestCode == CAPTURE_IMAGE_REQUEST&& resultCode == Activity.RESULT_OK) {

            imageView = view!!.findViewById(R.id.img)
            val myBitmap = BitmapFactory.decodeFile(photoFile!!.getAbsolutePath())
            imageView.setImageBitmap(myBitmap)

        } else if (resultCode == Activity.RESULT_OK && requestCode == GALLERY) {
            val returnUri = data!!.getData();


            imageView = view!!.findViewById(R.id.img);
            returnUri!!.let {
                val s = activity!!.contentResolver.openInputStream(returnUri)
                val b = BitmapFactory.decodeStream(s)
                imageView.setImageBitmap(b)
            }
        }

        else {
                Log.i("tag", "Request cancelled or something went wrong.")
        }
    }

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
        }
    }
}

