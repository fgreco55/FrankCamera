package com.example.frankcamera.utility

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.URL

class UtilityViewModel : ViewModel() {
    private var _networkBitmap = MutableLiveData<Bitmap>()
    val networkBitmap: LiveData<Bitmap> = _networkBitmap

    /*
     imageURLStringToBitmap() - Given an image URL and a context (from frag/activity), return a Bitmap using Coil library
     */
    fun imageURLStringToBitmap(imageURL: String, context: Context) {
        viewModelScope
            .launch {
                val loader = ImageLoader(context)
                val request = ImageRequest.Builder(context)
                    .data(imageURL)
                    .build()

                    val result = loader.execute(request).drawable   // val result = (loader.execute(request) as SuccessResult).drawable
                _networkBitmap.value = (result as BitmapDrawable).bitmap!!
            }
    }
}