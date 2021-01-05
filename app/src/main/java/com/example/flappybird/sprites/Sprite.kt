package com.example.flappybird.sprites

import android.graphics.Canvas
import com.example.flappybird.state.GameState

interface Sprite {

    fun onDraw(canvas: Canvas?)

    fun isCollide(sprite: Sprite):Boolean
}