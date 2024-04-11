package com.nehamadiwal.jettipapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nehamadiwal.jettipapp.ui.theme.JetTipAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                Text(text = "Hello")
            }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    JetTipAppTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier,
            color = MaterialTheme.colorScheme.background
        ) {
            content()
        }
    }
}

@Preview
@Composable
fun TopHeader() {
    Surface(modifier = Modifier
        .fillMaxWidth()
        .height(150.dp)
        .clip(shape = CircleShape.copy(all = CornerSize(12.dp)))
        //.clip(shape = RoundedCornerShape(corner = CornerSize(12.dp))),

    ) {

        Column {

        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JetTipAppTheme {
        MyApp {
            Text(text = "Hello")
        }
    }
}