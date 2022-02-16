package com.levp.catsandmath.core

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Data
import com.google.gson.GsonBuilder
import com.levp.catsandmath.data.remote.CatApi
import com.levp.catsandmath.data.remote.NumFactApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.work.OneTimeWorkRequest
import com.levp.catsandmath.R
import android.app.PendingIntent

import android.content.Intent
import com.levp.catsandmath.MainActivity


const val hourInMillis = 3600000L


fun buildNumFactApi(BASE_URL_NUM_FACT: String): NumFactApi =
    Retrofit.Builder()
        .baseUrl(BASE_URL_NUM_FACT)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        .build()
        .create(NumFactApi::class.java)


fun buildCatApi(BASE_URL_CAT: String): CatApi =
    Retrofit.Builder()
        .baseUrl(BASE_URL_CAT)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CatApi::class.java)


fun Context?.toast(text: CharSequence, duration: Int = Toast.LENGTH_LONG) =
    this?.let { Toast.makeText(it, text, duration).show() }


fun noInternetToast(context: Context) {
    Toast.makeText(context, "No internet :(", Toast.LENGTH_SHORT).show()
}

fun isOnline(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities =
        connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    if (capabilities != null) {
        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
            return true
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
            return true
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
            Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
            return true
        }
    }
    return false
}

fun getNotification(
    message: String,
    applicationContext: Context,
    notificationManager: NotificationManager,
    CHANNEL_ID: String = "channelID",
    NOTIFICATION_ID: Int = 101
) {
    val name: CharSequence = "quotes_channel"
    val description = "This is my channel"
    val importance = NotificationManager.IMPORTANCE_HIGH
    val mChannel = NotificationChannel(CHANNEL_ID, "kek", importance)
    notificationManager.createNotificationChannel(mChannel)
    mChannel.description = description
    mChannel.enableLights(true)
    mChannel.lightColor = Color.RED
    mChannel.setShowBadge(false)

    val contentIntent = PendingIntent.getActivity(
        applicationContext, 0,
        Intent(applicationContext, MainActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT
    )

    val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
        .setSmallIcon(R.drawable.catdrawable)
        .setContentTitle("Today's fact:")
        .setStyle(NotificationCompat.BigTextStyle().bigText(message))
        .setContentText(message)
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setContentIntent(contentIntent)

    with(NotificationManagerCompat.from(applicationContext)) {
        notificationManager.notify(NOTIFICATION_ID, builder.build()) // посылаем уведомление
    }
}


fun longToStringTime(notificationTime: Long): String {
    val hour: Int = (notificationTime / hourInMillis).toInt()
    val minutes: Int = (notificationTime % hourInMillis / 60000).toInt()
    val sHour =  if (hour>=10) hour.toString() else "0$hour"
    val sMin =  if (minutes>=10) minutes.toString() else "0$minutes"
    return "$sHour:$sMin"
}
fun millisToHours(millis:Long):Int{
    return (millis / hourInMillis).toInt()
}
fun millisToMinutes(millis:Long):Int{
    return (millis % hourInMillis / (60000)).toInt()
}
fun triggerNotification() {

}