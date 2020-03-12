package com.allarma.hammington.activities

interface SwipeHandler {
    fun removeAt( position: Int )
    fun move( from: Int, to: Int )
}