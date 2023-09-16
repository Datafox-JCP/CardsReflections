package mx.datafox.cardsreflections

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
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
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mx.datafox.cardsreflections.ui.theme.CardsReflectionsTheme

data class Quote(
    val title: String,
    val text: String,
    val author: String,
    val book: String,
    @DrawableRes val cardImage: Int,
    @DrawableRes val authorImage: Int
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            CardsReflectionsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.8f)),
                        contentAlignment = Alignment.Center
                    ) {
                        RandomQuoteCard()
                    }
                }
            }
        }
    }
}

@Composable
fun RandomQuoteCard() {
    var quote by remember { mutableStateOf(getRandomQuote()) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedVisibility(
            visible = true,
            enter = fadeIn(animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)),
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
                        .animateContentSize()
                ) {
                    QuoteCardContent(quote = currentQuote)
                }
                
            }

        }
    }
}

@Composable
fun QuoteCardContent(
    modifier: Modifier = Modifier,
    quote: Quote
) {

    var showFullText by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.animateContentSize(),
        colors = CardDefaults.cardColors(
            containerColor = Color.Black
        )
    ) {
        Column {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                painter = painterResource(id = quote.cardImage),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
//                colorFilter = ColorFilter.colorMatrix(
//                    ColorMatrix().apply {
//                        setToSaturation(0f)
//                    }
//                )
            )

            Column(
                modifier = Modifier
                    .padding(
                        vertical = 20.dp,
                        horizontal = 15.dp)
            ){
                Text(
                    text = quote.title,
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
                    text = quote.text,
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
                        painter = painterResource(id = quote.authorImage),
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
                            append(quote.author)
                        }
                        append("\n")
                        withStyle(
                            SpanStyle(
                                color = Color.White.copy(alpha = 0.5f),
                                fontSize = 12.sp,
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
}

fun getRandomQuote(): Quote {
    val quotes = arrayOf(
        Quote(
            "DECIDIR",
            "- Desearía que no hubiese ocurrido en mi época -dijo Frodo. - Y yo -respondió Gandalf-, y también todos los que han vivido para ver unos tiempos como estos. Pero no les toca a ellos decidir. Lo único que debemos decidir es qué hacer con el tiempo que nos es dado.",
            "J.R.R. Tolkien",
            "El Señor de los Anillos",
            R.drawable.imagen1,
            R.drawable.tolkien
        ),
        Quote(
            "PRESENTE",
            "Si estás deprimido, vives en el pasado. Si estás inquieto, vives en el futuro. Si estás en paz, vives en el presente.",
            "Lao Tse",
            "",
            R.drawable.imagen2,
            R.drawable.lao_tse
        ),
        Quote(
            "FRUSTRACION",
            "Lidiar con la frustración pasajera de no lograr ningún progreso forma parte del camino hacia la excelencia.",
            "Tim Ferris",
            "Armas de titanes",
            R.drawable.imagen3,
            R.drawable.ferris
        ),
        Quote(
            "CAMBIO",
            "Si quieres hacer un cambio permanente, deja de enfocarte en el tamaño de tus problemas y enfócate en el tamaño de tu ser.",
            "T. Harv Eker",
            "Secretos de la Mente Millonaria",
            R.drawable.imagen4,
            R.drawable.eker
        ),
        Quote(
            "DICHA",
            "Así como no es una golondrina o un buen día lo que hace la primavera, tampoco es un buen día o un corto período de tiempo lo que hace a un hombre bendecido y dichoso",
            "Aristoteles",
            "",
            R.drawable.imagen5,
            R.drawable.aristoteles
        ),
        Quote(
            "ADVERSIDAD",
            "Algunas ocasiones tus mayores fortalezas son tus mayores debilidades. Algunas ocasiones de las grandes adversidades, es de donde aprendes mas.",
            "Steve Jobs",
            "Make Something Wonderful",
            R.drawable.imagen6,
            R.drawable.jobs
        ),
        Quote(
            "GENIAL",
            "Para ser bueno, debes aprender a ser tú mismo. Y para ser genial, debes sumar habilidades a tus fortalezas, no reemplazarlas.",
            "Chris Voss",
            "Rompe la barrera del no",
            R.drawable.imagen7,
            R.drawable.voss
        ),
        Quote(
            "SUCESOS",
            "No nos afecta lo que nos sucede, sino lo que nos decimos acerca de lo que nos sucede.",
            "Epicteto",
            "",
            R.drawable.imagen8,
            R.drawable.epicteto
        ),
        Quote(
            "PROPOSITO",
            "La vida nunca es insoportable por las circunstancias, sino por la falta de significado y propósito.",
            "Victor Frankl",
            "El sentido de la vida",
            R.drawable.imagen9,
            R.drawable.frankl
        ),
        Quote(
            "NUNCA",
            "Nunca te rindas. Nunca!, nunca!, nunca!. Frente a nada, sea grande o pequeño, enorme o diminuto. Nunca te rindas excepto frente a las convicciones del honor y el buen sentido. Nunca te sometas frente a la fuerza. Nunca te sometas al aparentemente abrumador poder del enemigo.",
            "Winston Churchill",
            "",
            R.drawable.imagen10,
            R.drawable.churchill
        ),
        Quote(
            "CORAJE",
            "El éxito no es final; fracasar no es fatal: Es el coraje para continuar lo que cuenta.",
            "Winston Churchill",
            "",
            R.drawable.imagen11,
            R.drawable.churchill
        ),
        Quote(
            "CONFIANZA",
            "La falta de confianza mata más sueños que la falta de habilidad.",
            "James Clear",
            "Hábitos Atómicos",
            R.drawable.imagen12,
            R.drawable.clear
        ),
        Quote(
            "CONFIANZA",
            "La falta de confianza mata más sueños que la falta de habilidad.",
            "James Clear",
            "Hábitos Atómicos",
            R.drawable.imagen12,
            R.drawable.clear
        )
    )

    return quotes.random()
}

@Preview(showBackground = true, showSystemUi = true, backgroundColor = 0xFF000000)
@Composable
fun GreetingPreview() {
    CardsReflectionsTheme {
        RandomQuoteCard()
    }
}