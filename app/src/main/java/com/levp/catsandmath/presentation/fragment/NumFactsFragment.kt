package com.levp.catsandmath.presentation.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import com.levp.catsandmath.R
import com.levp.catsandmath.SettingsActivity
import com.levp.catsandmath.core.isOnline
import com.levp.catsandmath.core.noInternetToast
import com.levp.catsandmath.core.toast
import com.levp.catsandmath.presentation.NFViewModel
import com.levp.catsandmath.presentation.core.UiStateNF
import kotlinx.android.synthetic.main.fragment_num_facts.*
import java.util.*

class NumFactsFragment() : Fragment() {


    var currNum: Long? = null
    val NFViewModel: NFViewModel by viewModels()//= ViewModelProvider(requireActivity()).get(NumFactViewModel())

    private val types = arrayListOf<String>("trivia", "math", "year", "date")
    var type: String = types[Random().nextInt(3)]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_num_facts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureView(this)
    }

    private fun configureView(fragment: NumFactsFragment) {
        NFViewModel.uiState().observe(this.requireActivity()) { uiState ->
            if (uiState != null) {
                render(uiState)
            }
        }
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        var month = c.get(Calendar.MONTH)
        var day = c.get(Calendar.DAY_OF_MONTH)


        val spinner = fragment.numFacts_spinner
        val adapter: ArrayAdapter<*> = ArrayAdapter.createFromResource(
            fragment.requireContext(),
            R.array.num_facts_types,
            android.R.layout.simple_spinner_item
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SetTextI18n")
            override fun onItemSelected(
                parent: AdapterView<*>?,
                itemSelected: View?, selectedItemPosition: Int, selectedId: Long
            ) {
                val choose = resources.getStringArray(R.array.num_facts_types)
                type = choose[selectedItemPosition].lowercase(Locale.ROOT)

                fragment.num_facts_ET.isFocusable = type != "date"
                fragment.num_facts_ET.isFocusableInTouchMode = type != "date"

                if (type == "date") {
                    fragment.num_facts_ET.setText("$day.${month + 1}")
                } else {
                    fragment.num_facts_ET.setText(currNum?.toString() ?: "")
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        if (isOnline(this.requireContext())) {
            NFViewModel.getRandomFact(type)
        } else {
            noInternetToast(this.requireContext())
        }

        fragment.num_facts_ET.setOnClickListener {
            if (type == "date") {
                //Log.d("datepick", "date picker created")

                val dpd =
                    DatePickerDialog(fragment.requireContext(), { _, _, monthOfYear, dayOfMonth ->

                        num_facts_ET.setText("$dayOfMonth.${monthOfYear + 1}")
                        month = monthOfYear
                        day = dayOfMonth
                    }, year, month, day)

                dpd.show()
            }
        }

        fragment.next_numfact_btn.setOnClickListener {
            if (isOnline(this.requireContext())) {
                val curr = fragment.num_facts_ET.text.toString().toLongOrNull()
                currNum = curr

                if (currNum != null || type == "date") {
                    val date = fragment.num_facts_ET.text.toString()
                    NFViewModel.getFact(currNum ?: 0, type, date)
                } else {
                    NFViewModel.getRandomFact(type)
                }
            } else {
                noInternetToast(this.requireContext())
            }
        }
        fragment.settings_btn.setOnClickListener {

            val intent = Intent(this.requireContext(), SettingsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun render(uiState: UiStateNF) {
        when (uiState) {
            is UiStateNF.Loading -> {
                onLoad()
            }
            is UiStateNF.Success -> {
                onSuccess(uiState)

            }
            is UiStateNF.Error -> {
                onError(uiState)
            }
        }
    }

    private fun onLoad() {
        this.progressBar.visibility = View.VISIBLE
        this.next_numfact_btn.isEnabled = false
    }

    private fun onSuccess(uiState: UiStateNF.Success) {
        this.next_numfact_btn.isEnabled = true
        this.progressBar.visibility = View.GONE

        val response = uiState.numFactResponse
        val body = response.body()

        val fact = body?.text ?: "whoops..."
        this.num_fact_TextView.text = fact
    }

    private fun onError(uiState: UiStateNF.Error) {
        this.progressBar.visibility = View.GONE
        this.next_numfact_btn.isEnabled = true
        this.requireContext().toast(uiState.message)
    }

}