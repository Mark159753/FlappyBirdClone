package com.example.flappybird.state

sealed class GameState{
    object Play:GameState()
    object Over:GameState()
    object ScoreUp:GameState()
}

interface StateListener{
    fun onStateUpdate(state: GameState)
}