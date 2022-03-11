package com.sid.app.traintracks.util

import kotlin.random.Random

fun Random.nextInt(from: Int, until: Int, excluding: Int): Int {
    return Random.nextInt(from, until - 1).let { it + if (it >= excluding) 1 else 0 }
}