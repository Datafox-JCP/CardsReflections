package mx.datafox.cardsreflections

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun RandomQuoteCard() {
    val gradientColorList = listOf(
        Color(0xFF020A5E),
        Color(0xFF311B92),
        Color(0xFF4527A0),
        Color(0xFF010E70),
        Color(0xFF020A5E)
    )

    val state = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    var quote by remember { mutableStateOf(getRandomQuote()) }

    Box(
        modifier = Modifier
            .fillMaxSize()
//            .background(Color.Black.copy(alpha = 0.8f)),
            .background(
                brush = gradientBackgroundBrush(
                    isVerticalGradient = true,
                    colors = gradientColorList
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(state),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearOutSlowInEasing
                    )
                ),
                exit = fadeOut(animationSpec = tween(durationMillis = 300))
            ) {
                Crossfade(
                    targetState = quote,
                    label = "ChangeQuote"
                ) { currentQuote ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                coroutineScope.launch(Dispatchers.Default) {
                                    quote = getRandomQuote()
                                }
                            }
                            .animateContentSize(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Black.copy(alpha = 0.6f)
                        )
                    ) {
                        QuoteCardContent(quote = currentQuote)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QuoteCardContent(
    modifier: Modifier = Modifier,
    quote: Quote
) {

    val context = LocalContext.current
    val quoteShare = "\"${quote.text}\" - ${quote.author}"
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, quoteShare)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)

    var showFullText by remember { mutableStateOf(false) }
    var isFavorite by remember { mutableStateOf(FavoritesManager.isFavorite(context, quote.id)) }

    Column {
        Image(
            modifier = modifier
                .fillMaxWidth(),
            painter = painterResource(id = quote.cardImage),
            contentDescription = quote.title,
            contentScale = ContentScale.Fit
        )

        Column(
            modifier = modifier
                .padding(
                    vertical = 20.dp,
                    horizontal = 8.dp
                )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        isFavorite = !isFavorite
                        if (isFavorite) {
                            FavoritesManager.markAsFavorite(context, quote.id)
                        } else {
                            FavoritesManager.removeAsFavorite(context, quote.id)
                        }
                    },
                    modifier = Modifier
                        .size(24.dp)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = if (isFavorite) stringResource(R.string.remove_from_favorites) else stringResource(
                            R.string.add_to_favorites
                        ),
                        tint = Color.Red.copy(0.5f)
                    )
                }

                Text(
                    text = quote.title,
                    maxLines = 1,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                        .basicMarquee(),
                    color = Color.White,
                    fontFamily = FontFamily.Serif,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        shadow = Shadow(
                            color = Color.White,
                            offset = Offset(2f, 2f),
                            blurRadius = 8f
                        )
                    )
                )
            }

            Spacer(modifier = modifier.height(8.dp))

            Text(
                modifier = modifier
                    .animateContentSize(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    )
                    .clickable { showFullText = !showFullText },
                text = quote.text,
                color = Color.White.copy(alpha = 0.8f),
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = FontFamily.SansSerif,
                maxLines = if (showFullText) 20 else 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = modifier.height(20.dp))

            Row {
                Image(
                    modifier = modifier
                        .size(60.dp)
                        .clip(CircleShape),
                    painter = painterResource(id = quote.authorImage),
                    contentDescription = stringResource(R.string.author_picture),
                    contentScale = ContentScale.Crop,
                    colorFilter = ColorFilter.colorMatrix(
                        ColorMatrix().apply {
                            setToSaturation(0f)
                        }
                    )
                )

                Spacer(modifier = modifier.width(10.dp))

                val annotatedString = buildAnnotatedString {
                    withStyle(
                        SpanStyle(
                            color = Color.White,
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                            fontStyle = FontStyle.Italic
                        )
                    ) {
                        append(quote.author)
                    }
                    append("\n")
                    withStyle(
                        SpanStyle(
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = MaterialTheme.typography.bodySmall.fontSize
                        )
                    ) {
                        append(quote.book)
                    }
                }
                Text(text = annotatedString)

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.End
                ){
                    IconButton(onClick = { context.startActivity(shareIntent) }) {
                        Icon(
                            imageVector = Icons.Filled.Share,
                            contentDescription = "Share",
                            modifier = Modifier
                                .size(25.dp),
                            tint = Color.LightGray
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun gradientBackgroundBrush(
    isVerticalGradient: Boolean,
    colors: List<Color>
): Brush {

    val endOffset = if (isVerticalGradient) {
        Offset(x = 0F, Float.POSITIVE_INFINITY)
    } else {
        Offset(Float.POSITIVE_INFINITY, y = 0F)
    }

    return Brush.linearGradient(
        colors = colors,
        start = Offset.Zero,
        end = endOffset
    )
}

@Preview(showSystemUi = true)
@Composable
fun QuoteCardPreview() {
    RandomQuoteCard()
}