package mx.datafox.cardsreflections

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.play.core.review.ReviewManagerFactory
import mx.datafox.cardsreflections.ui.theme.CardsReflectionsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        installSplashScreen()
        showFeedbackDialog()
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

    private fun showFeedbackDialog() {
        val reviewManager = ReviewManagerFactory.create(applicationContext)

        reviewManager.requestReviewFlow().addOnCompleteListener {
            if (it.isSuccessful) {
                reviewManager.launchReviewFlow(this, it.result)
            }
        }
    }
}



@Preview(showBackground = true, showSystemUi = true, backgroundColor = 0xFF000000)
@Composable
fun GreetingPreview() {
    CardsReflectionsTheme {
        RandomQuoteCard()
    }
}
