package com.example.nzhang.proto_festival

import android.app.Activity
import android.util.Log
import com.example.nzhang.proto_festival.model.Categories
import com.example.nzhang.proto_festival.model.Events
import com.example.nzhang.proto_festival.model.Places
import com.squareup.moshi.Moshi
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.LinkedHashMap
import kotlin.reflect.jvm.internal.impl.javax.inject.Singleton

@Singleton
class DataController(activity: Activity) {

    // Load and parse JSON
    private val eventResponse = parseLoadJson(activity, Events::class.java, "events.json") as Events

    private val placeResponse = parseLoadJson(activity, Places::class.java, "places.json") as Places
    val places: List<Places.Place> = placeResponse.places
    val placesText = getPlacesBuilder()

    private val categorieResponse = parseLoadJson(activity, Categories::class.java, "categories.json") as Categories
    val categories: List<Categories.Category> = categorieResponse.categories

    // Order by date and by name
    private val orderedEvents = eventResponse.events.sortedWith(compareBy({it.getStartingDate().time}, {it.name}))

    private val publicEvents: List<Events.Event> = orderedEvents.filter({it.pro == 0})
    private val proEvents: List<Events.Event> = orderedEvents.filter({it.pro == 1})
    private val proDataClass = DataClass(proEvents)
    private val publicDataClass = DataClass(publicEvents)

    fun get(param: String): DataClass {
        return if (param == "pro") proDataClass else publicDataClass
    }

    fun getPlacesBuilder(): StringBuilder {
        val places = places.map{it.name}.sortedBy{it}
        val placesText = StringBuilder()
        places.forEach{ if(places.indexOf(it) == places.size - 1) placesText.append(it) else placesText.append(it + " / ")}
        return placesText
    }

    private fun loadJsonFromAssets(filename: String, activity: Activity) : String {
        val bytesStream: InputStream
        try {
            bytesStream = activity.baseContext.assets.open(filename)
            if (bytesStream != null) {
                val streamSize: Int = bytesStream.available() // Returns an estimate of the number of bytes that can be read
                val buffer: ByteArray = kotlin.ByteArray(streamSize)
                bytesStream.read(buffer)
                bytesStream.close()
                return String(buffer)
            }
        } catch (e: Throwable) {
            Log.e("error",  e.toString())
        }
        return ""
    }

    private fun <T> parseLoadJson(activity: Activity, dataClass: Class<T>, filename: String): T? {
        val eventsJson = loadJsonFromAssets(filename, activity)
        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter(dataClass)
        return adapter.fromJson(eventsJson)
    }
}
