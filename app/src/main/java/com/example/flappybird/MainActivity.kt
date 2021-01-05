package com.example.flappybird

import android.graphics.PixelFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayoutStates
import com.example.flappybird.state.GameState
import com.example.flappybird.state.StateListener

class MainActivity : AppCompatActivity(){

    private lateinit var playBtn:ImageButton
    private lateinit var root: FrameLayout
    private lateinit var gameView: GameView
    private val mHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        root = findViewById(R.id.root_container)
        gameView = findViewById(R.id.game_view)
        val bg:ImageView = findViewById(R.id.background)
        playBtn = findViewById(R.id.play_btn)

        gameView.apply {
            setZOrderOnTop(true)
            holder.setFormat(PixelFormat.TRANSPARENT)
            setStateListener(object : StateListener {
                override fun onStateUpdate(state: GameState) {
                    if (state is GameState.Over){
                        mHandler.post {
                            playBtn.visibility = View.VISIBLE
                        }
                    }
                }
            })
        }

        playBtn.setOnClickListener {
            it.visibility = View.GONE
            gameView.startNewGame()
        }
    }


}