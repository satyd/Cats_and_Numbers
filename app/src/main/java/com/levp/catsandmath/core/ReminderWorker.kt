package com.levp.catsandmath.core

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.work.*
import androidx.work.CoroutineWorker
import com.levp.catsandmath.R
import com.levp.catsandmath.data.remote.NumFactApi
import com.levp.catsandmath.presentation.BASE_URL_NUMFACT
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.currentCoroutineContext
import java.net.SocketException
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.coroutines.coroutineContext


class ReminderWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    companion object {
        private const val NOTIFICATION_ID = 102
        private const val CHANNEL_ID = "channel07"

        private const val REMINDER_WORK_NAME = "reminder"
        private const val PARAM_NAME = "name" // optional - send parameter to worker
        // private const val RESULT_ID = "id"
    }
    
    fun cancel() {
        val workManager = WorkManager.getInstance(applicationContext)
        workManager.cancelUniqueWork(REMINDER_WORK_NAME)
    }

    override suspend fun doWork(): Result = coroutineScope {
        val c = Calendar.getInstance()
        var month = c.get(Calendar.MONTH)
        var day = c.get(Calendar.DAY_OF_MONTH)
        val api: NumFactApi = buildNumFactApi(BASE_URL_NUMFACT)
        val date: String = "$day.${month + 1}"
        
        val worker = this@ReminderWorker
        val context = applicationContext

        val name = inputData.getString(PARAM_NAME)

        var isScheduleNext = true
        try {
            //Log.d("kek", "workmanager, yaay");
            val txt = date.split(".")
            
            val response = api.getByDate(txt[1].toLong(), txt[0].toLong())
            val body = response.body()
            val fact = body?.text ?: "whoops..."
            
            getNotification(
                fact,
                applicationContext,
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            )

            Result.success()
        } catch (e: Exception) {
            // only retry 3 times
            if (runAttemptCount > 3) {
                return@coroutineScope Result.success()
            }

            // retry if network failure, else considered failed
            when (e.cause) {
                is SocketException -> {
                    isScheduleNext = false
                    Result.retry()
                }
                else -> {

                    Result.failure()
                }
            }
        } finally {
            // only schedule next day if not retry, else it will overwrite the retry attempt
            // - because we use uniqueName with ExistingWorkPolicy.REPLACE
            if (isScheduleNext) {
                val currentDate = Calendar.getInstance()
                val dueDate = Calendar.getInstance()
                
                dueDate.add(Calendar.HOUR_OF_DAY, 24)
                
                val timeDiff = dueDate.timeInMillis - currentDate.timeInMillis
                val dailyWorkRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
                    .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
                    .build()
                
                WorkManager.getInstance(applicationContext)
                    .enqueue(dailyWorkRequest)

            }
        }
    }
}