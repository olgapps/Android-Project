package olga.pietrzyk.androidteacher.camera


import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.*
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory

import android.net.Uri

import android.os.Build
import android.os.Build.VERSION.SDK_INT
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
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.FileProvider


import olga.pietrzyk.androidteacher.R

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URI
import java.text.SimpleDateFormat
import java.util.*


class CameraFragment : Fragment() {

    private lateinit var imageView: ImageView
    private lateinit var button: Button
    private lateinit var button_upload: Button
    private var photoFile: File? = null
    private val CAPTURE_IMAGE_REQUEST = 1
    private lateinit var mCurrentPhotoPath: String
    private val IMAGE_DIRECTORY_NAME = "VLEMONN"
    private val GALLERY = 2
    lateinit var currentPhotoPath: String
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
        imageView = view.findViewById(R.id.camera_image);
        button = view.findViewById(R.id.take_picture_button);
        button_upload = view.findViewById(R.id.upload_picture_button);



        button.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                captureImage()
                // dispatchTakePictureIntent1()
            }
        }

        button_upload.setOnClickListener {
            openGalleryForImage()
        }
        return view

        // return binding.root
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
            /*if (takePictureIntent.resolveActivity(packageManager) != null) {
                // Create the File where the photo should go
                try {*/

            photoFile = createImageFile()
            //displayMessage(baseContext, photoFile!!.getAbsolutePath())
            Log.i("Olgaa", photoFile!!.getAbsolutePath())
            // val fileProvider = FileProvider.getUriForFile(context!!, "olga.pietrzyk.androidteacher.fileprovider", photoFile);
            // Continue only if the File was successfully created
            if (photoFile != null) {
                val pF = photoFile
                pF!!.let {
                    var photoURI = FileProvider.getUriForFile(
                        context!!,
                        "olga.pietrzyk.androidteacher.fileprovider",
                        pF
                    )
                    photoFile=pF
                    Log.i("Olgab", photoFile!!.getAbsolutePath())
                    u=photoURI

//                    val file = File(u!!.getPath());//create path from uri
//                    Log.i("Olgaurid", "${file}")
//                    var split = file.getPath().split(":");//split the path.
//                    val filePath = split[1];//assign it to a string(your choice).

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,u)
                    // Intent(MediaStore.ACTION_IMAGE_CAPTURE)

                    startActivityForResult(takePictureIntent, CAPTURE_IMAGE_REQUEST)
                }
            }
            /* } catch (ex: Exception) {
                    // Error occurred while creating the File
                    Log.i("Olga","Capture Image Bug: "  + ex.message.toString())
                }


            } else {
                Log.i("Olga", "Nullll")
            }*/
        }


    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent, "p"), GALLERY)
    }

    fun choosePhotoFromGallary() {
        val galleryIntent = Intent(Intent.ACTION_PICK)
        //MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryIntent.setType("image/*")

        startActivityForResult(galleryIntent, GALLERY)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        /* if (requestCode == CAPTURE_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
           //val myBitmap = BitmapFactory.decodeFile( )
            var bmp = data!!.extras!!.get("data") as Bitmap
            //imageView.setImageBitmap(bmp)

           imageView.setImageBitmap(bmp)

            //imageView.setImageBitmap(myBitmap)

*/
        if (requestCode == CAPTURE_IMAGE_REQUEST&& resultCode == Activity.RESULT_OK) {

            imageView = view!!.findViewById(R.id.img)
            val myBitmap = BitmapFactory.decodeFile(photoFile!!.getAbsolutePath())
            imageView.setImageBitmap(myBitmap)
            //val imageBitmap = data!!.extras!!.get("data") as Bitmap
            //val returnUri=data!!.getData()
            //val st = Environment.getExternalStorageDirectory().toString() + "/" + PHOTO_DIR  + "/test1.jpg";

            //setPic()
//           Log.i("olgagaga", "${data!!.getData()}")
            // imgUri!!.let {
            //val myBitmap =
            //  BitmapFactory.decodeStream(context!!.getContentResolver().openInputStream(Uri.parse(mCurrentPhotoPath)))




            // }
            //val a = activity!!.contentResolver.openInputStream(returnUri!!)
//val bitmap = BitmapFactory.decodeStream(a)

            //imageView.setImageBitmap(bitmap)
            //galleryAddPic()


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
            Log.i("Olgare", "Request cancelled or something went wrong.")
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {


        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = getActivity()!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        Log.i("Olgaext", "${getActivity()!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)}")
        Log.i("Olgabs", "${getActivity()!!.getFilesDir()!!.absolutePath}")
        val image = File.createTempFile(
            imageFileName, /* prefix */
            ".jpg", /* suffix */
            storageDir      /* directory */
        )

        Log.i("Olgaapth", "${image.absolutePath}")
        Log.i("Olgapth", "${image.path}")

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.absolutePath

        return image
        /* val mediaStorageDir = File( getActivity()!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "ola")

    // Create the storage directory if it does not exist

        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"

    // Return the file target for the photo based on filename
    val file = File(mediaStorageDir.getPath() + File.separator + imageFileName);

    return file;*/
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

/* override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
     super.onActivityResult(requestCode, resultCode, data)
     if(requestCode==123){
         var bmp = data!!.extras!!.get("data") as Bitmap
         camera_image.setImageBitmap(bmp)
     }
 }*/

