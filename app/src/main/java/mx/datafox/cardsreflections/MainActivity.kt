package mx.datafox.cardsreflections

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.datafox.cardsreflections.ui.theme.CardsReflectionsTheme

data class Publisher(
    val author: String,
    @DrawableRes val image: Int,
    val book: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CardsReflectionsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.8f)),
                        contentAlignment = Alignment.Center
                    ) {
                        CustomCard(
                            modifier = Modifier.fillMaxWidth(0.8f),
                            image = R.drawable.imagen15,
                            title = "DECIDIR",
                            text = "- Desearía que no hubiese ocurrido en mi época -dijo Frodo.\n" +
                                    "- Y yo -respondió Gandalf-, y también todos los que han vivido para ver unos tiempos como estos. Pero no les toca a ellos decidir. Lo único que debemos decidir es qué hacer con el tiempo que nos es dado.",
                            publisher = Publisher(
                                author = "J.R.R. Tolkien",
                                book = "El Señor de los Anillos",
                                image = R.drawable.tolkien
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CustomCard(
    modifier: Modifier = Modifier,
    @DrawableRes image: Int,
    title: String,
    text: String,
    publisher: Publisher
) {
    var showFullText by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.animateContentSize(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Black
        )
    ) {
        Column {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                painter = painterResource(id = image),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.colorMatrix(
                    ColorMatrix().apply {
                        setToSaturation(0f)
                    }
                )
            )

            Column(
                modifier = Modifier
                    .padding(
                        vertical = 20.dp,
                        horizontal = 15.dp)
            ){
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    modifier = Modifier
                        .animateContentSize(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioLowBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        )
                        .clickable {
                            showFullText = !showFullText
                        },
                    text = text,
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily.Serif,
                    maxLines = if (showFullText) 10 else 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row {
                    Image(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape),
                        painter = painterResource(id = publisher.image),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    val annotatedString = buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                color = Color.White,
                                fontSize = 12.sp,
                                fontStyle = FontStyle.Italic
                            )
                        ) {
                            append(publisher.author)
                        }
                        append("\n")
                        withStyle(
                            SpanStyle(
                                color = Color.White.copy(alpha = 0.5f),
                                fontSize = 12.sp,
                            )
                        ) {
                            append(publisher.book)
                        }
                    }
                    Text(text = annotatedString)
                }

            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    CardsReflectionsTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.8f)),
            contentAlignment = Alignment.Center
        ) {
            CustomCard(
                modifier = Modifier.fillMaxWidth(0.8f),
                image = R.drawable.imagen15,
                title = "DECIDIR",
                text = "- Desearía que no hubiese ocurrido en mi época -dijo Frodo.\n" +
                        "- Y yo -respondió Gandalf-, y también todos los que han vivido para ver unos tiempos como estos. Pero no les toca a ellos decidir. Lo único que debemos decidir es qué hacer con el tiempo que nos es dado.",
                publisher = Publisher(
                    author = "J.R.R. Tolkien",
                    book = "El Señor de los Anillos",
                    image = R.drawable.tolkien
                )
            )
        }
    }
}