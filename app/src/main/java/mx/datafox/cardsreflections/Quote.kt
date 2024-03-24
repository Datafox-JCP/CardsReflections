package mx.datafox.cardsreflections

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable

@Immutable // Use immutable data class to allow the compiler to optimize the code better, only if sure there will be no changes in the data. Could use @Stable instead.
data class Quote(
    val id: Int,
    val title: String,
    val text: String,
    val author: String,
    val book: String,
    @DrawableRes val cardImage: Int,
    @DrawableRes val authorImage: Int
)