package com.levp.catsandmath.presentation

import android.content.res.TypedArray

import android.widget.TimePicker

import android.R
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.preference.DialogPreference
import java.util.*


class TimePref(ctxt: Context?, attrs: AttributeSet?, defStyle: Int) :
    DialogPreference(ctxt, attrs, defStyle) {
    //private val calendar: Calendar?
    private var picker: TimePicker? = null

    constructor(ctxt: Context?) : this(ctxt, null) {}
    constructor(ctxt: Context?, attrs: AttributeSet?) : this(
        ctxt,
        attrs,
        R.attr.dialogPreferenceStyle
    ) {
    }

//    
//    protected fun onCreateDialogView(): View {
//        picker = TimePicker(getContext())
//        return picker
//    }
//
//    protected fun onBindDialogView(v: View?) {
//        super.onBindDialogView(v)
//        picker!!.currentHour = calendar.get(Calendar.HOUR_OF_DAY)
//        picker!!.currentMinute = calendar.get(Calendar.MINUTE)
//    }
//
//    protected fun onDialogClosed(positiveResult: Boolean) {
//        super.onDialogClosed(positiveResult)
//        if (positiveResult) {
//            calendar.set(Calendar.HOUR_OF_DAY, picker!!.currentHour)
//            calendar.set(Calendar.MINUTE, picker!!.currentMinute)
//            setSummary(summary)
//            if (callChangeListener(calendar.getTimeInMillis())) {
//                persistLong(calendar.getTimeInMillis())
//                notifyChanged()
//            }
//        }
//    }
//
//    protected fun onGetDefaultValue(a: TypedArray, index: Int): Any? {
//        return a.getString(index)
//    }
//
//    protected fun onSetInitialValue(restoreValue: Boolean, defaultValue: Any?) {
//        if (restoreValue) {
//            if (defaultValue == null) {
//                calendar.setTimeInMillis(getPersistedLong(System.currentTimeMillis()))
//            } else {
//                calendar.setTimeInMillis(getPersistedString(defaultValue as String?).toLong())
//            }
//        } else {
//            if (defaultValue == null) {
//                calendar.setTimeInMillis(System.currentTimeMillis())
//            } else {
//                calendar.setTimeInMillis(defaultValue as String?. toLong ())
//            }
//        }
//        setSummary(summary)
//    }
//
//    val summary: CharSequence?
//        get() = if (calendar == null) {
//            null
//        } else DateFormat.getTimeFormat(getContext()).format(Date(calendar.getTimeInMillis()))
//
//    init {
//        setPositiveButtonText(R.string.set)
//        setNegativeButtonText(R.string.cancel)
//        calendar = GregorianCalendar()
//    }
} 