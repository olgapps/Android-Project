package olga.pietrzyk.androidteacher.camera

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.File

class CameraViewModel: ViewModel(){
    var photoFile= MutableLiveData <File>()
}