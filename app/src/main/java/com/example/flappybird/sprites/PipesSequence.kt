package com.example.flappybird.sprites

import android.content.Context
import android.graphics.Canvas
import com.example.flappybird.R
import com.example.flappybird.state.GameState
import com.example.flappybird.state.StateListener
import java.util.*

class PipesSequence(
        private val context: Context,
        private val vWidth:Int,
        private val vHeight:Int,
        private val speed:Int,
        private val listener: StateListener
):Sprite {

    private val pipes = LinkedList<PipeSprite>()
    private val space = context.resources.getDimensionPixelSize(R.dimen.pipe_space)

    init {
        pipes.add(PipeSprite(context, vWidth, vHeight, speed, listener))
    }

    override fun onDraw(canvas: Canvas?) {
            addPipeIfNeed()
            removePipeIfNeed()
            pipes.forEach {
                it.onDraw(canvas)
        }
    }

    private fun addPipeIfNeed(){
        if (pipes.last.xRightPosition <= (vWidth - space)){
            pipes.add(PipeSprite(context, vWidth, vHeight, speed, listener))
        }
    }

    private fun removePipeIfNeed(){
        if (pipes.first.xRightPosition <= 0){
            pipes.removeFirst()
        }
    }

    override fun isCollide(sprite: Sprite): Boolean {
        pipes.forEach {
            if (it.isCollide(sprite)){
                listener.onStateUpdate(GameState.Over)
                return true
            }
        }
        return false
    }
}