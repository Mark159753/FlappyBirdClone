package com.example.flappybird.sprites

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.Log
import com.example.flappybird.R
import com.example.flappybird.state.GameState
import com.example.flappybird.state.StateListener
import java.util.*

class PipeSprite(
        context: Context,
        vWidth:Int,
        vHeight:Int,
        private val speed:Int,
        private val listener: StateListener
):Sprite {

    private val baseHeight = context.resources.getDimensionPixelSize(R.dimen.base_height)
    private val bottom = vHeight - baseHeight
    private val bottomPipe = context.resources.getDrawable(R.drawable.pipe_green)
    private val topPipe:Drawable = context.resources.getDrawable(R.drawable.pipe_green_top)
    private val pipeWidth = context.resources.getDimensionPixelSize(R.dimen.pipe_width)
    private val opening = context.resources.getDimensionPixelSize(R.dimen.pipe_opening)
    private var middleOpening:Int
    private var isPassed = false

    var xLeftPosition = vWidth
        private set
    var xRightPosition = xLeftPosition + pipeWidth
        private set

    init {
        middleOpening = getRandomPosition(opening, bottom - opening)
    }

    override fun onDraw(canvas: Canvas?) {
        bottomPipe.setBounds(xLeftPosition, middleOpening + opening /2, xRightPosition, bottom)
        topPipe.setBounds(xLeftPosition, 0, xRightPosition, middleOpening - opening /2)
        canvas?.let {
            bottomPipe.draw(it)
            topPipe.draw(it)
        }
        xLeftPosition -= speed
        xRightPosition -= speed
    }

    private fun getRandomPosition(min: Int, max: Int): Int {
        val random = Random()
        return random.nextInt(max - min) + min
    }

    override fun isCollide(sprite: Sprite): Boolean {
        if (sprite is Bird){
            val bounds = sprite.getRectOfBird()
            if (bounds.left >= xRightPosition && !isPassed) {
                listener.onStateUpdate(GameState.ScoreUp)
                isPassed = true
            }
            return Rect.intersects(bounds, bottomPipe.bounds) || Rect.intersects(bounds, topPipe.bounds)
        }
        return false
    }
}