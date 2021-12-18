package com.example.frankcamera.utility

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import java.io.IOException
import kotlin.random.Random
import kotlin.random.nextInt
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import kotlinx.coroutines.*
import java.net.URL


class ImagesCollection() {
    // Image URLs *MUST BE* legit image URLS ending in jpg/jpeg/png with no embedded HTML/JSON... or else they will fail miserably...
    //  Try a URL in a browser... you should just see the image with no other html/json/text/etc.
    var list = listOf(
        "https://www.suhr.com/wp-content/uploads/SKU/01-SIG-0030.jpg",
        "https://images.reverb.com/image/upload/s--tBhZamnS--/f_auto,t_large/v1601578200/fovidx8lsqfm4fix0mvn.jpg",
        "https://www.suhr.com/wp-content/uploads/SKU/01-STP-0041.jpg",
        "https://magazine-resources.tidal.com/uploads/2021/09/miles_davis_resized.jpg",
        "https://ichef.bbci.co.uk/news/976/cpsprodpb/9926/production/_119360293_71a2a560-8ca1-4450-a322-4eaae522fea3.jpg",
        "https://storage.googleapis.com/petbacker/images/blog/2017/dog-and-cat-cover.jpg",
        "https://img.kytary.com/eshop_ie/velky_v2/na/636824679358500000/d61badb1/64621731/prs-dgt-birds-dark-cherry-sunburst.jpg",
        "https://www.suhr.com/wp-content/uploads/SKU/01-ALT-0022.jpg",
        "https://americansongwriter.com/wp-content/uploads/2012/09/PRS-Bass-Orchestra.jpg",   // I took this pic!
        "https://media.guitarcenter.com/is/image/MMGS7/J07969000003000-00-720x720.jpg",
        "https://sc1.musik-produktiv.com/img/guitargallery/thumbs/010123740/010123740_01_1280x1920.jpg",
        "https://media.rainpos.com/8400/prs_dgt_david_grissom_tremolo_electric_guitar_gold_top_3.jpg",
        "https://friedmanamplification.com/images/guitars/Vintage_t/FRIEDMAN-VINTAGE-T--AMVB90_-1600-x-1200-front-min.jpg",
        "https://upload.wikimedia.org/wikipedia/commons/a/a6/Marion_Cty%2C_Iowa_Farmer_w_mule_drawn_wagon%2C_1920s.jpg",
        "https://upload.wikimedia.org/wikipedia/commons/e/e1/FullMoon2010.jpg",
        "https://upload.wikimedia.org/wikipedia/commons/thumb/2/2c/Earth%2C_Wind_%26_Fire_%282%29.jpg/2880px-Earth%2C_Wind_%26_Fire_%282%29.jpg",
        "https://upload.wikimedia.org/wikipedia/commons/7/79/Himalayas.jpg"
    )

        /* In case we ever want to use another arraylist instead of the default list above */
    constructor(urllist: ArrayList<String>) : this() {
        list = urllist
    }

    /* pick one of the URL strings in the list */
    fun pickImageURLString(): String {
        var mypick = list.random()              // Kotlin has useful random() method to get random element
        Log.i("Frank", "mypick is $mypick")

        return mypick
    }
}