package com.example.frankcamera.ui.main

import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import com.example.frankcamera.R
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var cb: Button
    private lateinit var im: ImageView
    private lateinit var imageBitmap: Bitmap
    val REQUEST_IMAGE_CAPTURE = 1

    /*
     onCreateView() - Instantiate the view hierarchy
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var myroot = inflater.inflate(R.layout.main_fragment, container, false)
        cb = myroot!!.findViewById(R.id.takePicture_button)
        im = myroot!!.findViewById(R.id.camera_imageview)
        return myroot
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        
        cb.setOnClickListener { 
            Log.i("Frank", "Inside onClick()")
            dispatchTakePictureIntent()
        }
    }
    
    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            Log.i("Frank", "*****ERROR.  Cannot start Camera app")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            imageBitmap = data!!.extras!!.get("data") as Bitmap
            Log.i("Frank", "Setting Image...")
            im.setImageBitmap(imageBitmap)
            imageLabeling(imageBitmap)
        }
    }
    
    private fun imageLabeling(mybitmap: Bitmap) {
        val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
        val image = InputImage.fromBitmap(mybitmap, 0)
        var outputText = ""

        labeler.process(image).addOnSuccessListener { labels ->
            for (label in labels) {
                val text = label.text
                val confidence = label.confidence
                outputText += "$text : $confidence\n"
                Log.i("Frank", "LOOP>     [$text]:$confidence")
            }
            Log.i("Frank", "\n***** Labels\n")
            Log.i("Frank", outputText)
            
        }.addOnFailureListener { e ->
            Log.e("Frank", "**Failure when processing image")
        }
    }

}