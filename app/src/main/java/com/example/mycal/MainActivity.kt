package com.example.mycal

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mycal.ui.theme.MycalTheme
import com.example.mycal.ui.theme.Rose
import com.example.mycal.ui.theme.Russian_Violete

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MycalTheme {
                val context = LocalContext.current

                fun simpleCalculate(expr: String): Double {
                    val expression = expr.replace(" ", "")
                    val numbers = mutableListOf<Double>()
                    val operators = mutableListOf<Char>()
                    var currentNumber = ""

                    for (ch in expression) {
                        if (ch.isDigit() || ch == '.') {
                            currentNumber += ch
                        } else if (ch in listOf('+', '-', '*', '/')) {
                            if (currentNumber.isNotEmpty()) {
                                numbers.add(currentNumber.toDouble())
                                currentNumber = ""
                            }
                            operators.add(ch)
                        }
                    }
                    if (currentNumber.isNotEmpty()) {
                        numbers.add(currentNumber.toDouble())
                    }

                    var result = numbers[0]
                    for (i in operators.indices) {
                        val op = operators[i]
                        val nextNumber = numbers[i + 1]
                        result = when (op) {
                            '+' -> result + nextNumber
                            '-' -> result - nextNumber
                            '*' -> result * nextNumber
                            '/' -> result / nextNumber
                            else -> result
                        }
                    }
                    return result
                }

                var expression by remember { mutableStateOf("") }
                var expressionS by remember { mutableStateOf(listOf<String>()) }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Russian_Violete
                ) {
                    Column(
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(35.dp)
                    ) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                label = { Text("Enter Expression", color = Rose) },
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    containerColor = Russian_Violete,
                                    focusedBorderColor = Rose,
                                    unfocusedBorderColor = Rose
                                ),
                                value = expression,
                                onValueChange = { input ->
                                    val allowedChars = "0123456789+-*/."
                                    expression = input.filter { it in allowedChars }
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Button(
                                onClick = {
                                    if (expression.isNotBlank()) {
                                        try {
                                            val result = simpleCalculate(expression)
                                            expressionS = expressionS + "$expression = $result"
                                            expression = ""
                                        } catch (e: Exception) {
                                            expressionS = expressionS + "Ошибка в выражении"
                                        }
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(Rose),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                                    .height(56.dp)
                            ) {
                                Text("Calculate Expression", color = Color.White, fontSize = 15.sp)
                            }
                        }
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Button(
                                onClick = {
                                    context.startActivity(Intent(context, MusicPlayer::class.java))
                                },
                                colors = ButtonDefaults.buttonColors(Rose),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                                    .height(56.dp)
                            ) {
                                Text("Go to Music Player", color = Color.White, fontSize = 15.sp)
                            }
                        }
                        LazyColumn {
                            items(expressionS.reversed()) { currentExp ->
                                Text(
                                    text = currentExp,
                                    color = Color.White,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                )
                                Divider(color = Rose)
                            }
                        }
                    }
                }
            }
        }
    }
}
