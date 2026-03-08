package fi.monami.finnish_parliament_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import fi.monami.finnish_parliament_app.ui.theme.InventoryTheme



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {

            InventoryTheme {
                ParliamentApp()
            }
        }
    }
}