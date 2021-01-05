package com.example.flappybird

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.flappybird.sprites.*
import com.example.flappybird.state.GameState
import com.example.flappybird.state.StateListener

class GameView @JvmOverloads constructor(
        context: Context,
        attrs:AttributeSet? = null
):SurfaceView(context, attrs), SurfaceHolder.Callback, StateListener{

    private var drawThread:DrawThread? = null
    private var birdSprite:Bird? = null
    private var scoreSprite:ScoreSprite? = null
    private var listener:StateListener? = null

    fun setStateListener(l:StateListener){
        listener = l
    }

    fun startNewGame(){
        holder.addCallback(this)
        surfaceCreated(holder)
    }


    override fun surfaceCreated(holder: SurfaceHolder) {
        val screen = getScreenSize()
        birdSprite = Bird(context, screen[0], screen[1], this)
        scoreSprite = ScoreSprite(context, screen[0], screen[1], this)
        drawThread = DrawThread(holder).also {
            it.setSprites(listOf(BaseSprite(context, screen[0], screen[1], SPEED, this),
                    PipesSequence(context, screen[0], screen[1], SPEED, this),
                    birdSprite!!,
                    scoreSprite!!
                ))
            it.setRunning(true)
            it.start()
        }
    }


    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        var retry = true
        drawThread?.setRunning(false)
        while (retry){
            try {
                drawThread?.join(10)
                retry = false
                birdSprite = null
                scoreSprite = null
            }catch (e:InterruptedException){}
        }
    }

    private fun getScreenSize(): IntArray {
        val size = IntArray(2)
        size[0] = Resources.getSystem().displayMetrics.widthPixels
        size[1] = Resources.getSystem().displayMetrics.heightPixels
        return size
    }

    override fun onStateUpdate(state: GameState) {
        when (state) {
            is GameState.Over -> {
                surfaceDestroyed(holder)
                holder.removeCallback(this)
            }
            is GameState.ScoreUp -> {
                scoreSprite?.increaseScore()
            }
        }
        listener?.onStateUpdate(state)
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.actionMasked == MotionEvent.ACTION_DOWN){
            birdSprite?.onTap()
        }
        return super.onTouchEvent(event)
    }

    class DrawThread(
            private val surfaceHolder: SurfaceHolder
    ):Thread(){

        private var isRunning = false
        private var sprites:List<Sprite>? = null
        private var bird:Bird? = null


        fun setSprites(list: List<Sprite>){
            this.sprites = list
            list.forEach {
                if (it is Bird){
                    bird = it
                }
            }
        }

        fun setRunning(b:Boolean){
            isRunning = b
        }


        override fun run() {
            var canvas: Canvas? = null
            while (isRunning) {
                val startTime = System.currentTimeMillis()
                canvas = surfaceHolder.lockCanvas()
                try {
                    synchronized(surfaceHolder) {
                        cleanCanvas(canvas)
                        sprites?.forEach {
                            it.onDraw(canvas)
                            if (it !is Bird && bird != null) {
                                val collide = it.isCollide(bird!!)
                                if (collide)
                                    bird?.setIsCollide(true)
                            }
                        }
                    }
                } finally {
                    if (canvas != null) surfaceHolder.unlockCanvasAndPost(canvas)
                }
                val endTime = System.currentTimeMillis()
                val sleepTime = RENDER_TIME - (endTime - startTime)
                if (sleepTime > 0)
                    Thread.sleep(sleepTime)
            }
            clean()
        }


        private fun clean(){
            val canvas = surfaceHolder.lockCanvas()
            try {
                cleanCanvas(canvas)
            }finally {
                if (canvas != null) surfaceHolder.unlockCanvasAndPost(canvas)
            }
        }

        private fun cleanCanvas(canvas: Canvas?){
            canvas?.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        }
    }

    companion object{
        const val RENDER_TIME:Long = 30
        private const val SPEED = 3
    }
}