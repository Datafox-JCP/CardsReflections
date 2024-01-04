package mx.datafox.cardsreflections

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.skydoves.orbital.Orbital
import com.skydoves.orbital.animateBounds
import com.skydoves.orbital.rememberMovableContentOf

@Composable
fun QuotesListScreen() {
    Orbital {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
        ) {
            items(quotes, { it.id } ) {
                var expanded by rememberSaveable { mutableStateOf(false) }

                AnimatedVisibility(
                    remember { MutableTransitionState(false) }
                        .apply { targetState = true },
                    enter = fadeIn(tween(300)),
                    exit = fadeOut(tween(300))
                ) {
                    Orbital(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickableWithoutRipple(
                                interactionSource = MutableInteractionSource(),
                                onClick = { expanded = !expanded }
                            )
                    ) {
                        val text = rememberMovableContentOf {
                            Column(
                                modifier = Modifier
                                    .padding(vertical = 10.dp)
                                    .padding(horizontal = if (expanded) 20.dp else 10.dp)
                                    .animateBounds(
                                        sizeAnimationSpec = tween(300),
                                        positionAnimationSpec = tween(300)
                                    )
                            ) {
                                Text(
                                    text = it.title,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                                    fontWeight = FontWeight.Medium,
                                    maxLines = if (expanded) 2 else 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = it.text,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontFamily = FontFamily.Serif,
                                    maxLines = if (expanded) 14 else 2,
                                    overflow = TextOverflow.Clip
                                )
                            }
                        }
                        val image = rememberMovableContentOf {
                            AsyncImage(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .animateBounds(
                                        modifier = if (expanded) {
                                            Modifier.fillMaxWidth()
                                        } else {
                                            Modifier.size(100.dp)
                                        },
                                        sizeAnimationSpec = tween(300),
                                        positionAnimationSpec = tween(300)
                                    )
                                    .clip(RoundedCornerShape(10.dp)),
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(it.cardImage)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = it.title,
                                contentScale = ContentScale.Crop
                            )
                        }

                        if (expanded) {
                            Column(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)),
                            ) {
                                image()
                                text()
                            }
                        } else {
                            Row {
                                image()
                                text()
                            }
                        }
                    }
                }
            }
        }
    }
}

@SuppressLint("UnnecessaryComposedModifier")
fun Modifier.clickableWithoutRipple(
    interactionSource: MutableInteractionSource,
    onClick: () -> Unit
) = composed(
    factory = {
        this.then(
            Modifier.clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = { onClick() }
            )
        )
    }
)