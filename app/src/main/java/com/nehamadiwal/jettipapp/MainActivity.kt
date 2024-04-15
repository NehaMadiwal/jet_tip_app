package com.nehamadiwal.jettipapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableDoubleState
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nehamadiwal.jettipapp.components.InputField
import com.nehamadiwal.jettipapp.ui.theme.JetTipAppTheme
import com.nehamadiwal.jettipapp.util.calculateTotalPerPerson
import com.nehamadiwal.jettipapp.util.calculateTotalTip
import com.nehamadiwal.jettipapp.widgets.RoundIconButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                Column {

                    MainContent()
                }
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
fun TopHeader(totalPerPerson: Double = 134.0) {
    Surface(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .fillMaxWidth()
            .height(150.dp)
            .clip(shape = CircleShape.copy(all = CornerSize(12.dp))),
        //.clip(shape = RoundedCornerShape(corner = CornerSize(12.dp))),
        color = Color(0xFFF7D7E9)

    ) {

        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val total = "%.2f".format(totalPerPerson)
            Text(
                text = "Total per person",
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = "$$total",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@Preview
@Composable
fun MainContent() {
    val splitByState = remember {
        mutableIntStateOf(1)
    }

    val range = IntRange(start = 1, endInclusive = 100)

    val tipAmountState = remember {
        mutableDoubleStateOf(0.0)
    }

    val totalPerPersonState = remember {
        mutableDoubleStateOf(0.0)
    }

    Column(modifier = Modifier.padding(all = 16.dp)) {
        BillForm(
            range = range,
            splitByState = splitByState,
            tipAmountState = tipAmountState,
            totalPerPersonState = totalPerPersonState
        ) {}
    }
}


@Composable
fun BillForm(
    modifier: Modifier = Modifier,
    range: IntRange = 1..100,
    splitByState: MutableIntState,
    tipAmountState: MutableDoubleState,
    totalPerPersonState: MutableDoubleState,
    onValChange: (String) -> Unit = {}
) {
    val totalBillState = remember {
        mutableStateOf("")
    }

    val validState = remember(totalBillState.value) {
        totalBillState.value.trim().isNotEmpty()
    }

    val sliderPositionState = remember {
        mutableStateOf(0f)
    }

    //val tipPercentage = (sliderPositionState.value * 100).toInt()

    val keyBoardController = LocalSoftwareKeyboardController.current

    TopHeader(totalPerPerson = totalPerPersonState.value)
    Surface(
        modifier = Modifier
            //.padding(16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(corner = CornerSize(8.dp)),
        border = BorderStroke(width = 1.dp, color = Color.LightGray)
    ) {
        Column(
            modifier = Modifier.padding(6.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            InputField(
                valueState = totalBillState,
                labelId = "Enter bill",
                enabled = true,
                isSingleLine = true,
                onAction = KeyboardActions {
                    if (!validState) return@KeyboardActions
                    onValChange(totalBillState.value.trim())
                    keyBoardController?.hide()
                    tipAmountState.value = calculateTotalTip(
                        totalBill = totalBillState.value.toDouble(),
                        tipPercentage = sliderPositionState.value.toInt()
                    )
                    totalPerPersonState.value = calculateTotalPerPerson(
                        totalBill = totalBillState.value.toDouble(),
                        splitBy = splitByState.value,
                        tipPercentage = sliderPositionState.value.toInt()
                    )
                }
            )

            if (validState) {
                Row(
                    modifier = modifier.padding(start = 12.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "Split",
                        modifier = Modifier
                            .align(
                                alignment = Alignment.CenterVertically
                            )
                            .width(40.dp)
                    )

                    Spacer(modifier = Modifier.width(64.dp))

                    Row(
                        modifier = Modifier.padding(horizontal = 3.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        RoundIconButton(
                            imageVector = Icons.Default.Remove,
                            onClick = {
                                splitByState.value = if (splitByState.value > 1) {
                                    splitByState.value - 1
                                } else {
                                    1
                                }
                                totalPerPersonState.value = calculateTotalPerPerson(
                                    totalBill = totalBillState.value.toDouble(),
                                    splitBy = splitByState.value,
                                    tipPercentage = sliderPositionState.value.toInt()
                                )

                            }
                        )
                        Text(
                            text = "${splitByState.value}",
                            modifier = modifier
                                .align(Alignment.CenterVertically)
                                .padding(start = 9.dp, end = 9.dp)
                        )
                        RoundIconButton(
                            imageVector = Icons.Default.Add,
                            onClick = {
                                if (splitByState.value < range.last) {
                                    splitByState.value += 1
                                }
                                totalPerPersonState.value = calculateTotalPerPerson(
                                    totalBill = totalBillState.value.toDouble(),
                                    splitBy = splitByState.value,
                                    tipPercentage = sliderPositionState.value.toInt()
                                )

                            }
                        )
                    }
                }
                //Tip row
                Row(
                    modifier = modifier.padding(12.dp)
                ) {
                    Text(
                        text = "Tip",
                        modifier = Modifier
                            .align(alignment = Alignment.CenterVertically)
                            .width(40.dp)
                    )
                    Spacer(modifier = Modifier.width(64.dp))
                    Text(
                        text = "$ ${tipAmountState.value}",
                        modifier = Modifier
                            .align(alignment = Alignment.CenterVertically)
                            .padding(3.dp)
                            .padding(start = 8.dp)
                    )
                }

                //Slider column
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "${sliderPositionState.value.toInt()}%",
                        style = MaterialTheme.typography.titleMedium
                    )
                    //Slider
                    Slider(
                        value = sliderPositionState.value,
                        onValueChange = { newVal ->
                            sliderPositionState.value = newVal
                            Log.d("Slider", "BillForm: onValueChange $newVal")
                            tipAmountState.value = calculateTotalTip(
                                totalBill = totalBillState.value.toDouble(),
                                tipPercentage = sliderPositionState.value.toInt()
                            )
                            totalPerPersonState.value = calculateTotalPerPerson(
                                totalBill = totalBillState.value.toDouble(),
                                splitBy = splitByState.value,
                                tipPercentage = sliderPositionState.value.toInt()
                            )
                        },
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                        valueRange = 0f..100f,
                        //steps = 9
                    )

                }
            } else {
                Box {}
            }
        }
    }

}


