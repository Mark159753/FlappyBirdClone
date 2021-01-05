package com.example.flappybird.sprites

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import com.example.flappybird.R
import com.example.flappybird.state.GameState
import com.example.flappybird.state.StateListener
import java.util.*

class Bird(
        context: Context,
        private val vWidth: Int,
        private val vHeight: Int,
        private val listener: StateListener
):Sprite {

    private val random = Random()
    private val birds:Array<Drawable>
    private var count = 0
    private var position = 0
    private var birdXPosition = 0
    private var birdYPosition = 0
    private var velocityY = 0
    private val birdWidth = context.resources.getDimensionPixelSize(R.dimen.bird_with)
    private val birdHeight = context.resources.getDimensionPixelSize(R.dimen.bird_height)
    private val onTapVelocity = context.resources.getDimensionPixelSize(R.dimen.on_tap_velocity)
    private var isCollided = false

    init {
        val res = context.resources
        val currentBird = random.nextInt(DRAWABLE_BIRD.size)
        birds = Array(3){res.getDrawable(DRAWABLE_BIRD[currentBird][it]) }
        initStartPosition()
    }

    private fun initStartPosition(){
        birdXPosition = vWidth / 2
        birdYPosition = (vHeight * 0.45).toInt()
    }

    override fun onDraw(canvas: Canvas?) {
        var bird:Drawable? = null
        if (count >= FLY_COUNT) {
            count = 0
            if (position >= birds.size - 1) {
                position = 0
            } else
                position++
        } else
            count++
        bird = birds[position]


        updateVelocityY()
        bird?.setBounds(birdXPosition - birdWidth / 2, birdYPosition - birdHeight / 2, birdXPosition + birdWidth / 2, birdYPosition + birdHeight / 2)
        canvas?.let { bird.draw(it) }
    }

    private fun updateVelocityY(){
        if (birdYPosition + velocityY <= 0){
            velocityY = 0
        }
        if (!isCollided) {
            birdYPosition += velocityY
            velocityY += 1
        }else{
            velocityY = 0
        }
    }

    fun getRectOfBird():Rect{
        return birds[position].bounds
    }

    override fun isCollide(sprite: Sprite): Boolean {
        return false
    }

    fun setIsCollide(state: Boolean){
        isCollided = state
    }

    fun onTap(){
        velocityY = onTapVelocity
    }


    companion object{
        @JvmStatic
        private val DRAWABLE_BIRD = arrayOf(
                intArrayOf(R.drawable.bluebird_upflap, R.drawable.bluebird_midflap, R.drawable.bluebird_downflap),
                intArrayOf(R.drawable.redbird_upflap, R.drawable.redbird_midflap, R.drawable.redbird_downflap),
                intArrayOf(R.drawable.yellowbird_upflap, R.drawable.yellowbird_midflap, R.drawable.yellowbird_downflap)
        )
        @JvmStatic
        private val FLY_COUNT = 3
    }
}