package com.levp.catsandmath.presentation.fragment

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.levp.catsandmath.R
import com.levp.catsandmath.core.toast
import com.levp.catsandmath.presentation.CatViewModel
import com.levp.catsandmath.presentation.core.UiStateCat
import kotlinx.android.synthetic.main.fragment_cat.*
import kotlinx.coroutines.*
import android.content.Intent
import android.graphics.Bitmap.createScaledBitmap
import android.net.Uri
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.levp.catsandmath.core.isOnline
import com.levp.catsandmath.core.noInternetToast
import kotlin.math.roundToInt


class CatFragment : Fragment() {
    companion object {
        fun newInstance(): Fragment =
            CatFragment()
    }

    val viewModel: CatViewModel by viewModels()
    var screenWidth: Int = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            screenWidth = it.getInt("screenWidth")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureView()
    }

    private fun configureView() {
        viewModel.uiState().observe(this.requireActivity(), { uiState ->
            if (uiState != null) {
                render(uiState)
            }
        })
        if (isOnline(this.requireContext())) {
            viewModel.getNormalCat()
        }
        else{
            
            
//            Glide.with(this)
//                .asGif()
//                .load(R.drawable.gif_loading)
//                .into(display_pic)

            noInternetToast(this.requireContext())
        }
        fab_cat.setOnClickListener {
            if (isOnline(this.requireContext())) {
                viewModel.getNormalCat()
            }
            else{
                noInternetToast(this.requireContext())
            }
        }
        fab_share.setOnClickListener {

            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, viewModel.currImageUrl)

            startActivity(Intent.createChooser(shareIntent, "Share cat with :3"))
        }
    }

    private fun render(uiState: UiStateCat) {
        when (uiState) {
            is UiStateCat.Loading -> {
                onLoad()
            }
            is UiStateCat.Success -> {
                onSuccess(uiState)

            }
            is UiStateCat.Error -> {
                onError(uiState)
            }
        }
    }

    private fun onLoad() {
        this.gif_loading.visibility = View.VISIBLE
    }

    private fun onSuccess(uiState: UiStateCat.Success) {

        val response = uiState.catResponse

        val catLink = response.body()?.file ?: "https://http.cat/404"
        viewModel.currImageUrl = catLink
        if (!catLink.contains(".gif")) {
            Glide.with(this)
                .asBitmap()
                .load(catLink)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                    ) {
                        this@CatFragment.gif_loading.visibility = View.GONE
                        viewModel.currImage = resource
                        this@CatFragment.display_pic.setImageBitmap(resource)
                    }

                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        super.onLoadFailed(errorDrawable)
                        this@CatFragment.display_pic.setImageResource(R.drawable.load_failed)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                    }
                })
        } else {

            Glide.with(this)
                .asGif()
                .load(catLink)
                .into(display_pic)
            Toast.makeText(this.requireContext(), "Loading gif...", Toast.LENGTH_SHORT).show()

            this@CatFragment.gif_loading.visibility = View.GONE

        }
    }

    private fun onError(uiState: UiStateCat.Error) {
        this.gif_loading.visibility = View.GONE
        this.requireContext().toast(uiState.message)
    }
}