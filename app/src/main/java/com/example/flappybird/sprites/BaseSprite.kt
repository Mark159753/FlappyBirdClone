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
import kotlin.math.abs

class BaseSprite(
        context: Context,
        private val vWidth:Int,
        private val vHeight:Int,
        private val speed:Int,
        private val listener: StateListener
):Sprite {

    private val baseDrawable:Drawable
    private val baseWith = context.resources.getDimensionPixelSize(R.dimen.base_width)
    private val baseHeight = context.resources.getDimensionPixelSize(R.dimen.base_height)
    private val baseQueue:LinkedList<Drawable> = LinkedList<Drawable>()

    private var scroll:Int = 0

    init {
        val res = context.resources
        baseDrawable = res.getDrawable(R.drawable.base)
        initBaseQueue()
    }

    private fun initBaseQueue(){
        var with = 0
        while (with <= vWidth){
            baseQueue.add(baseDrawable)
            with += baseWith
        }
    }

    override fun onDraw(canvas: Canvas?) {
        var position = scroll
        if ((position + baseWith) <= 0) {
            baseQueue.removeFirst()
            position = 0
            scroll = 0
        }
        if (baseQueue.last.bounds.right <= vWidth) {
            baseQueue.add(baseDrawable)
        }
        baseQueue.forEach { drawable ->
            drawable.setBounds(position, vHeight - baseHeight, position + baseWith, vHeight)
            canvas?.let { drawable.draw(it) }
            position += baseWith
        }
        scroll -= speed
    }

    override fun isCollide(sprite: Sprite): Boolean {
        if (sprite is Bird){
            if (sprite.getRectOfBird().bottom >= (vHeight - baseHeight)){
                listener.onStateUpdate(GameState.Over)
                return true
            }
        }
        return false
    }
}