package mx.datafox.cardsreflections

import android.os.Bundle
import android.view.WindowManager
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        installSplashScreen()
        setContent {
            CardsReflectionsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    RandomQuoteCard()
                }
            }
        }
    }
}

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
                    color = Color.White,
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
                    style = MaterialTheme.typography.bodyLarge,
                    fontFamily = FontFamily.Serif,
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
                                fontSize = MaterialTheme.typography.bodySmall.fontSize,
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
            "EXCELENCIA",
            "Lidiar con la frustración pasajera de no lograr ningún progreso forma parte del camino hacia la excelencia.",
            "Tim Ferris",
            "Armas de titanes",
            R.drawable.imagen3,
            R.drawable.ferris
        ),
        Quote(
            "ENFOQUE",
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
            "Un éxito nunca es definitivo, un fracaso nunca es fatídico, lo que cuenta es el coraje.",
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
            "ATENCIÓN",
            "Los demás no piensan tanto en nosotros como creemos, ni siquiera cuando somos el centro de atención.",
            "Amy Cuddy",
            "El poder de la presencia",
            R.drawable.imagen13,
            R.drawable.cuddy
        ),
        Quote(
            "SEGURIDAD",
            "Un individuo seguro -que conoce su identidad y cree en ella, lleva consigo herramientas en lugar de armas. No necesita quedar por encima de los demás. Cuando crees de verdadd -en ti, en tus ideas-, no te sientes amenzado, sino seguro.",
            "Amy Cuddy",
            "El poder de la presencia",
            R.drawable.imagen13,
            R.drawable.cuddy
        ),
        Quote(
            "SER",
            "No lo finjas hasta conseguirlo, fíngelo hasta serlo.",
            "Amy Cuddy",
            "El poder de la presencia",
            R.drawable.imagen14,
            R.drawable.cuddy
        ),
        Quote(
            "HASTA EL ANOCHECER",
            "Cualquiera puede llevar una carga, por pesada que sea, hasta el anochecer. Cualquiera puede hacer su trabajo, por duro que sea, durante el día. Cualquiera puede llever una vida dulce, paciente, amorosa y pura hasta la puesta del sol. Y esto es todo cuanto la vida significa realmente.",
            "Robert Louis Stevenson",
            "",
            R.drawable.imagen15,
            R.drawable.stevenson
        ),
        Quote(
            "NO RENDIRSE",
            "Estar a la altura de la situación no tenía nada que ver con el talento. Lo más importate para no tirar la toalla era la actitud de <<no rendirse nunca>>.",
            "Amy Cuddy",
            "GRIT",
            R.drawable.imagen16,
            R.drawable.cuddy
        ),
        Quote(
            "PERSEVERANCIA",
            "Aquello que alcanzamos en la vida depende más de nuestra pasión y persverancia que de nuestro talento natural.",
            "Angela Duckworth",
            "GRIT",
            R.drawable.imagen17,
            R.drawable.duckworth
        ),
        Quote(
            "OPORTUNIDADES",
            "Aprovecha los errores y problemas como oportunidades para mejorar y no como razones para desistir.",
            "Angela Duckworth",
            "GRIT",
            R.drawable.imagen18,
            R.drawable.duckworth
        ),
        Quote(
            "SUFRIMIENTO",
            "No es el sufrimiento lo que lleva a la desesperanza, sino el sufrimiento que creemos no poder controlar.",
            "Angela Duckworth",
            "GRIT",
            R.drawable.imagen19,
            R.drawable.duckworth
        ),
        Quote(
            "CUALIDADES",
            "Apunta hacia arriba. Presta atención. Arregla lo que puedas arreglar. No seas arrogante. Esfuérzate por ser humilde. No mientas por nada nunca y sobre todo no te mientas a ti.",
            "Jordan B. Peterson",
            "12 Reglas para vivir",
            R.drawable.imagen20,
            R.drawable.peterson
        ),
        Quote(
            "CARIÑO",
            "Cuando quieres a alguien, no es a pesar de sus limitaciones, sino a causa de ellas.",
            "Jordan B. Peterson",
            "12 Reglas para vivir",
            R.drawable.imagen21,
            R.drawable.peterson
        ),
        Quote(
            "VIVIR",
            "Aquel que tiene un porqué por el cual vivir, puede soportar cualquier cosa.",
            "Friedrich Nietzche",
            "",
            R.drawable.imagen22,
            R.drawable.nietzsche
        ),
        Quote(
            "RESPONSABILIDAD",
            "No eres responsable de la mano de cartas que te han repartido. Eres responsable de sacar el máximo de lo que te ha tocado.",
            "Tim Ferris",
            "Armas de titanes",
            R.drawable.imagen23,
            R.drawable.ferris
        ),
        Quote(
            "ESTRATEGÍA",
            "La esperanza no es una estrategía. La suerte no es un factor. El miedo no es una opción.",
            "Tim Ferris",
            "Armas de titanes",
            R.drawable.imagen24,
            R.drawable.ferris
        ),
        Quote(
            "INTENTAR",
            "¡No lo intentes! Hazlo o no lo hagas. No vale intentar.",
            "Yoda",
            "Star Wars (Episode V)",
            R.drawable.imagen25,
            R.drawable.yoda
        ),
        Quote(
            "PERDER",
            "Está bien perder con el oponente. No perder ante el miedo.",
            "Pat Morita (Señor Miyagi)",
            "The Karate Kid III",
            R.drawable.imagen26,
            R.drawable.morita
        ),
        Quote(
            "HÁBITOS",
            "Todas las cosas importantes provienen de comienzos modestos. La semilla de cada hábito es una pequeña decisión.",
            "James Clear",
            "Hábitos atómicos",
            R.drawable.imagen27,
            R.drawable.clear
        ),
        Quote(
            "ESPERANZA",
            "Al principio la esperanza es todo lo que tienes.",
            "James Clear",
            "Hábitos atómicos",
            R.drawable.imagen28,
            R.drawable.clear
        ),
        Quote(
            "CLARIDAD",
            "Muchas veces, lo que parece resistencia es falta de claridad.",
            "Chip Heath & Dan Heath",
            "Cambia el chip",
            R.drawable.imagen29,
            R.drawable.chip
        ),
        Quote(
            "PERDER",
            "Los grandes cambios proceden de una sucesión de pequeños cambios",
            "Chip Heath & Dan Heath",
            "Cambia el chip",
            R.drawable.imagen30,
            R.drawable.chip
        ),
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