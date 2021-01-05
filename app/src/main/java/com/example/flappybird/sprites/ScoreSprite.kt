package com.example.flappybird.sprites

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import com.example.flappybird.R
import com.example.flappybird.state.GameState
import com.example.flappybird.state.StateListener
import java.util.*

class ScoreSprite(
    private val context: Context,
    private val vWidth:Int,
    private val vHeight:Int,
    private val listener: StateListener
):Sprite {

    private val digitsMap = hashMapOf<String, Int>(
        "0" to R.drawable.zero,
        "1" to R.drawable.one,
        "2" to R.drawable.two,
        "3" to R.drawable.thre,
        "4" to R.drawable.four,
        "5" to R.drawable.five,
        "6" to R.drawable.six,
        "7" to R.drawable.seven,
        "8" to R.drawable.eight,
        "9" to R.drawable.nine
    )

    private val digitMarginTop = context.resources.getDimensionPixelSize(R.dimen.digit_margin_top)

    private val drawableScore:MutableList<Drawable> = LinkedList<Drawable>()
    private var scoreDrawable:LayerDrawable? = null
    private var score:Int = 0

    init {
        score.toString().forEach {
            drawableScore.add(context.resources.getDrawable(digitsMap[it.toString()]!!))
        }
        updateScoreDrawable()
    }

    fun increaseScore(){
        score++
        drawableScore.clear()
        score.toString().forEach {
            drawableScore.add(context.resources.getDrawable(digitsMap[it.toString()]!!))
        }
        updateScoreDrawable()
    }

    private fun updateScoreDrawable(){
        scoreDrawable = LayerDrawable(drawableScore.toTypedArray())
        var left = 0
        for (i:Int in 0 until drawableScore.size){
            val d = drawableScore[i]
            val w = d.intrinsicWidth
            scoreDrawable?.setLayerInsetLeft(i, left)
            scoreDrawable?.setLayerWidth(i, w)
            left += w
        }
    }

    override fun onDraw(canvas: Canvas?) {
        scoreDrawable?.let { score ->
            score.setBounds(vWidth / 2 - score.intrinsicWidth /2, digitMarginTop, vWidth / 2 + score.intrinsicWidth /2, score.intrinsicHeight + digitMarginTop)
            canvas?.let {
                score.draw(it)
            }
        }
    }

    override fun isCollide(sprite: Sprite): Boolean {
        return false
    }
}