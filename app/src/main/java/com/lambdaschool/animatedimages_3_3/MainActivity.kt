package com.lambdaschool.animatedimages_3_3

import android.annotation.SuppressLint
import android.graphics.ImageDecoder
import android.graphics.drawable.Animatable
import android.graphics.drawable.AnimatedImageDrawable
import android.graphics.drawable.AnimationDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val drawableIds = listOf(
        R.drawable.mk_finish /* animated gif */,
        R.drawable.sosad_animated /* animation drawable */
    )
    val pauseDrawableIds = listOf(
        R.drawable.mk_finish,
        R.drawable.sosad_frame_00
    )
    var pointer = 0
    var playPause: Boolean = false // false = animation is paused

    fun incrementPointer() {
        pointer++
        if (pointer >= drawableIds.size) {
            pointer = 0
        }
    }

    fun decrementPointer() {
        pointer--
        if (pointer < 0) {
            pointer = drawableIds.size - 1
        }
    }

    @SuppressLint("WrongThread")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set initial images
        animated_image.setImageDrawable(
            ContextCompat.getDrawable(this, drawableIds[pointer]))
        button_play.setImageDrawable(
            ContextCompat.getDrawable(this, R.drawable.vd_play_icon)
        )
        button_next.setImageDrawable(
            ContextCompat.getDrawable(this, R.drawable.vd_next_icon)
        )
        button_previous.setImageDrawable(
            ContextCompat.getDrawable(this, R.drawable.vd_previous_icon)
        )

        button_next.setOnClickListener {
            incrementPointer()
            animated_image.setImageDrawable(
                ContextCompat.getDrawable(this, drawableIds[pointer])
            )
        }

        button_previous.setOnClickListener {
            decrementPointer()
            animated_image.setImageDrawable(
                ContextCompat.getDrawable(this, drawableIds[pointer])
            )
        }

        button_play.setOnClickListener { view ->
            when (pointer) {
                0 -> animateGif(drawableIds[pointer], animated_image)
                1 -> animateAnimationDrawable(drawableIds[pointer], animated_image)
            }
            when (playPause) {
                true -> {
                    // play the gif
                    animateVectorDrawable(R.drawable.avd_playtopause, view as ImageView)
                    playPause = false
                }
                false -> {
                    // pause the gif
                    animateVectorDrawable(R.drawable.avd_pausetoplay, view as ImageView)
                    playPause = true
                    animated_image.setImageDrawable(
                        ContextCompat.getDrawable(
                            this,
                            pauseDrawableIds[pointer]
                        )
                    )
                }
            }
        }

    }

    private fun animateVectorDrawable(id: Int, view: ImageView) {
        val animatedVectorDrawable = ContextCompat.getDrawable(this, id)
        view.setImageDrawable(animatedVectorDrawable)
        (animatedVectorDrawable as Animatable).start()
    }

    private fun animateAnimationDrawable(id: Int, view: ImageView) {
        val frameDrawable = ContextCompat.getDrawable(this, id)
        view.setImageDrawable(frameDrawable)
        (frameDrawable as AnimationDrawable).start()
    }

    private fun animateGif(id: Int, view: ImageView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val gifDrawable = ImageDecoder.decodeDrawable(ImageDecoder.createSource(resources, id))
            view.setImageDrawable(gifDrawable)
            (gifDrawable as AnimatedImageDrawable).start()
        }
    }
}

