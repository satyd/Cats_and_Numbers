package com.levp.catsandmath

import android.app.TimePickerDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.work.*
import com.levp.catsandmath.R
import com.levp.catsandmath.core.ReminderWorker
import com.levp.catsandmath.core.longToStringTime
import com.levp.catsandmath.core.millisToHours
import com.levp.catsandmath.core.millisToMinutes
import kotlinx.android.synthetic.main.settings_activity.*
import java.util.*
import java.util.concurrent.TimeUnit

const val hourInMillis = 3600000L

class SettingsActivity : AppCompatActivity() {


    var notificationsEnabled: Boolean = false
    var notificationTime: Long = 36000000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.setting_frag_container, SettingsFragment())
                .commit()
        }
        val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        notificationsEnabled = prefs.getBoolean("areNotificationsEnabled", false)
        notificationTime = prefs.getString("notificationTime", "36000000")?.toLong() ?: 36000000

    }

    class SettingsFragment : PreferenceFragmentCompat() {

        companion object {
            const val NOTIFICATION_WORK = "appName_notification_work"
        }

        var notificationsEnabled: Boolean = false
        var notificationTime: Long = 36000000
        lateinit var request: WorkRequest

        var cHour = 10
        var cMins = 0


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            val prefs: SharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(requireContext())
            notificationsEnabled = prefs.getBoolean("areNotificationsEnabled", false)
            notificationTime =
                prefs.getString("notificationTime", "36000000")?.toLong() ?: hourInMillis * 10

            cHour = millisToHours(notificationTime)
            cMins = millisToMinutes(notificationTime)

            val pref = preferenceScreen.findPreference<Preference>("notificationTime")!!
            pref.summary = longToStringTime(notificationTime)


        }

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.pref_screen, rootKey)

            request = PeriodicWorkRequestBuilder<ReminderWorker>(1, TimeUnit.DAYS)
                .build()
        }

        override fun onPreferenceTreeClick(preference: Preference?): Boolean {
            val prefs: SharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(requireContext())

            if (preference == preferenceScreen.findPreference("areNotificationsEnabled")) {
                notificationsEnabled = prefs.getBoolean("areNotificationsEnabled", false)
                if (notificationsEnabled) {
                    setManager()
                } else {
                    WorkManager.getInstance(this.requireContext()).cancelAllWork()
                    Toast.makeText(
                        this.requireContext(),
                        "Notifications cancelled",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else if (preference == preferenceScreen.findPreference("notificationTime")) {
                val cldr: Calendar = Calendar.getInstance()
                val hour: Int = millisToHours(notificationTime)
                val minutes: Int = millisToMinutes(notificationTime)
                val pref = preference as Preference


                val picker = TimePickerDialog(
                    this.requireActivity(), android.R.style.Theme_DeviceDefault_Dialog_NoActionBar,
                    { tp, sHour, sMinute ->

                        notificationTime =
                            sHour.toLong() * hourInMillis + sMinute.toLong() * 60 * 1000
                        prefs.edit().putString("notificationTime", notificationTime.toString())
                            .apply()
                        cHour = sHour
                        cMins = sMinute
                        pref.summary = ("$sHour:$sMinute")
                        if (notificationsEnabled) {
                            WorkManager.getInstance(this.requireContext()).cancelAllWork()
                            setManager()
                        }
                    }, hour, minutes, true
                )

                picker.show()

            }
            return super.onPreferenceTreeClick(preference)
        }

        private fun setManager() {
//            WorkManager
//                .getInstance(requireContext().applicationContext)
//                .enqueue(request)
            val currentDate = Calendar.getInstance()
            val dueDate = Calendar.getInstance()


            dueDate.set(Calendar.HOUR_OF_DAY, cHour)
            dueDate.set(Calendar.MINUTE, cMins)

            if (dueDate.before(currentDate)) {
                dueDate.add(Calendar.HOUR_OF_DAY, 24)
            }
            val constraints = Constraints.Builder()
                .setRequiresCharging(true)
                .build()

            val timeDiff = dueDate.timeInMillis - currentDate.timeInMillis
            val dailyWorkRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
                .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
                .build()
            Toast.makeText(
                this.requireContext(),
                "Notifications set for $cHour:$cMins",
                Toast.LENGTH_SHORT
            ).show()
            WorkManager.getInstance(this.requireContext()).enqueue(dailyWorkRequest)
        }

        private fun scheduleNotification(delay: Long) {
            val notificationWork = OneTimeWorkRequest.Builder(ReminderWorker::class.java)
                .setInitialDelay(delay, TimeUnit.MILLISECONDS).build()

            val instanceWorkManager = WorkManager.getInstance(this.requireContext())
            instanceWorkManager.beginUniqueWork(
                NOTIFICATION_WORK,
                ExistingWorkPolicy.REPLACE,
                notificationWork
            ).enqueue()
        }

    }

}

