package com.example.frankcamera.ui.main

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.frankcamera.R
import com.example.frankcamera.utility.ImagesCollection
import com.example.frankcamera.utility.UtilityViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel       // AS created this one... I don't use it
    private lateinit var uviewModel: UtilityViewModel   // My VM to handle my Utility methods

    private lateinit var cb: Button
    private lateinit var im: ImageView
    private lateinit var rt: TextView

    private lateinit var imageBitmap: Bitmap
    private lateinit var ic: ImagesCollection           // ImagesCollection has random images

    /*
     onCreateView() - Instantiate the view hierarchy
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val myroot = inflater.inflate(R.layout.main_fragment, container, false)
        cb = myroot!!.findViewById(R.id.takePicture_button)
        im = myroot.findViewById(R.id.camera_imageview)
        rt = myroot.findViewById(R.id.results_textview)

        ic = ImagesCollection()     // My random images list
        return myroot
    }

    /*
     onActivityCreated() - Called when Activity is created and the Views are created.
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        uviewModel = ViewModelProvider(this).get(UtilityViewModel::class.java)

        /*
         Get Image button click handler
         */
        cb.setOnClickListener {
            Log.i("Frank", "Inside onClick()")
            handleImage()           // Either call the camera or pick an internal image
        }
        /*
         observe callback - notices when the LiveData bitmap is changed and then does the work.
         */
        uviewModel.networkBitmap.observe(viewLifecycleOwner, {
            Log.i("Frank", "++++++++++ OBSERVER CALLED...")
            im.setImageBitmap(it)
            imageLabeling(it)
        })
    }

    /*
     handleImage() - calls either the camera or gets a random image... the UI should really allow the user to pick
     */
    private fun handleImage() {
        //dispatchTakePictureIntent()       // Calls the camera
        dispatchRandomImage()               // Gets a random image from internal list... simulates random-image service
    }

    /*
     dispatchTakePictureIntent() - calls Coil lib to get Bitmap using a coroutine.  Bitmap returned to ViewModel.
         The observe callback in onActivityCreated notices the bitmap and sets the ImageView and calls ML Kit
     */
    private fun dispatchRandomImage() {
        val s = ic.pickImageURLString()
        Log.i("Frank", "URL string: $s")

        uviewModel.imageURLStringToBitmap(s, context!!)
    }

    /*
     Called when Intent returns.  New way vs onActivityResult()
     */
    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
    if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            imageBitmap = data!!.extras!!.get("data") as Bitmap
            Log.i("Frank", "Setting Image...")

            im.setImageBitmap(imageBitmap)
            imageLabeling(imageBitmap)
        }
    }

    /*
      dispatchTakePictureIntent() - my routine that creates an Intent to get an image from a camera app
     */
    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        resultLauncher.launch(takePictureIntent)
    }

    /*
     imageLabeling() - my routine that calls ML Kit ImageLabeling to get recognized objects (labels) with their associated accuracy.

     Accuracy is how confident ML Kit feels about a recognized object in the image.
     */
    private fun imageLabeling(mybitmap: Bitmap?) {
        val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
        val image = InputImage.fromBitmap(mybitmap, 0)
        var outputText = ""

        labeler.process(image).addOnSuccessListener { labels ->
            for (label in labels) {
                val text = label.text                   // retrieve the label text
                val confidence = label.confidence       // retrieve the confidence
                outputText += "$text : $confidence\n"   // build a string for output
                //Log.i("Frank", "LOOP>     [$text]:$confidence")
            }
            Log.i("Frank", "\n\n***** Labels\n $outputText")
            rt.text = outputText


        }.addOnFailureListener { e ->
            Log.e("Frank", "**Failure when processing image")
        }
    }

}