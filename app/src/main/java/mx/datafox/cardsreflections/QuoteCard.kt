package mx.datafox.cardsreflections

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun RandomQuoteCard() {
    val state = rememberScrollState()
    var quote by remember { mutableStateOf(getRandomQuote()) }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.7f)),
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
                Crossfade(targetState = quote, label = "ChangeQuote") { currentQuote ->
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
                            containerColor = Color.Black
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

    var showFullText by remember { mutableStateOf(false) }

    Column {
        Image(
            modifier = modifier
                .fillMaxWidth()
                .height(200.dp),
            painter = painterResource(id = quote.cardImage),
            contentDescription = quote.title,
            contentScale = ContentScale.Crop,
//                colorFilter = ColorFilter.colorMatrix(
//                    ColorMatrix().apply {
//                        setToSaturation(0f)
//                    }
//                )
        )

        Column(
            modifier = modifier
                .padding(
                    vertical = 20.dp,
                    horizontal = 15.dp)
        ){
            Text(
                text = quote.title,
                maxLines = 1,
                modifier = Modifier
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

            Spacer(modifier = modifier.height(8.dp))

            Text(
                modifier = modifier
                    .animateContentSize(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    )
                    .clickable {
                        showFullText = !showFullText
                    },
                text = quote.text,
                color = Color.White.copy(alpha = 0.8f),
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = FontFamily.SansSerif,
                maxLines = if (showFullText) 14 else 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = modifier.height(20.dp))

            Row {
                Image(
                    modifier = modifier
                        .size(60.dp)
                        .clip(CircleShape),
                    painter = painterResource(id = quote.authorImage),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
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
            }
        }
    }
}