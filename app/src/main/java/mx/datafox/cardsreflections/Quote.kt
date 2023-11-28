package mx.datafox.cardsreflections

import androidx.annotation.DrawableRes

data class Quote(
    val title: String,
    val text: String,
    val author: String,
    val book: String,
    @DrawableRes val cardImage: Int,
    @DrawableRes val authorImage: Int
)