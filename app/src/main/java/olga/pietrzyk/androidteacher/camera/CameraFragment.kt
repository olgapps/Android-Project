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
    private lateinit var button: Button
    private lateinit var button_upload: Button
    private var photoFile: File? = null
    private val CAPTURE_IMAGE_REQUEST = 1
    private lateinit var mCurrentPhotoPath: String
    private val GALLERY = 2
    lateinit var u: Uri


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        /* val binding = DataBindingUtil.inflate<FragmentCameraBinding>(
            inflater,
            R.layout.fragment_camera, container, false
        )*/
        val view = inflater!!.inflate(R.layout.fragment_camera, container, false)
        //View rootView = inflater.inflate( R.layout.fragment_camera, container, false);
        //imageView = view.findViewById(R.id.camera_image);
        button = view.findViewById(R.id.take_picture_button);
        button_upload = view.findViewById(R.id.upload_picture_button);



        button.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                captureImage()
            }
        }

        button_upload.setOnClickListener {
            openGalleryForImage()
        }
        return view
    }


    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(getActivity()!!.getPackageManager())?.also {
                startActivityForResult(takePictureIntent, CAPTURE_IMAGE_REQUEST)
            }
        }
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
                0
            )
        } else {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            photoFile = createImageFile()

            if (photoFile != null) {
                val pF = photoFile
                pF!!.let {
                    var photoURI = FileProvider.getUriForFile(
                        context!!,
                        "olga.pietrzyk.androidteacher.fileprovider",
                        pF
                    )
                    photoFile=pF
                    u=photoURI


                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,u)

                    startActivityForResult(takePictureIntent, CAPTURE_IMAGE_REQUEST)
                }
            }
        }
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent, "p"), GALLERY)
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
                Log.i("Olgabs", "${s}")
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
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = getActivity()!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName, /* prefix */
            ".jpg", /* suffix */
            storageDir      /* directory */
        )

        mCurrentPhotoPath = image.absolutePath

        return image
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == 0) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED
            ) {
                captureImage()
            }
        }
    }
}

