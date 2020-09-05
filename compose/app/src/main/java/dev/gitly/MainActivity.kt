package dev.gitly

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Text
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.stringResource
import androidx.ui.tooling.preview.Preview
import dev.gitly.theme.GitlyTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GitlyTheme {
                // A surface container using the 'background' color from the theme
                Greeting(stringResource(id = R.string.app_name))
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GitlyTheme {
        Greeting(stringResource(id = R.string.app_name))
    }
}