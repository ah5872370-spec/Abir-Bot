package com.example.quotexsignalai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

private val Navy = Color(0xFF081426)
private val Panel = Color(0xFF12243B)
private val Blue = Color(0xFF4DA3FF)
private val Green = Color(0xFF36C98F)
private val Red = Color(0xFFFF6B75)
private val Muted = Color(0xFF9DB0C8)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { SignalDemoApp() }
    }
}

@Composable
fun SignalDemoApp() {
    var market by remember { mutableStateOf("EUR/USD (Sample)") }
    var signal by remember { mutableStateOf("WAIT") }
    var note by remember { mutableStateOf("Generate a practice analysis to see a sample result.") }
    var rsi by remember { mutableIntStateOf(50) }
    var macd by remember { mutableStateOf("Neutral") }
    var history by remember { mutableStateOf(listOf<String>()) }

    MaterialTheme(colorScheme = darkColorScheme(
        background = Navy, surface = Panel, primary = Blue,
        onBackground = Color.White, onSurface = Color.White
    )) {
        Column(
            Modifier.fillMaxSize().background(Navy).verticalScroll(rememberScrollState()).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text("QUOTEX SIGNAL AI", fontSize = 23.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text("ANDROID • OFFLINE PRACTICE DEMO", color = Blue, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF352B16)), shape = RoundedCornerShape(12.dp)) {
                Text(
                    "DEMO ONLY — কাল্পনিক ডেটা। এটি লাইভ মার্কেট সিগন্যাল নয় এবং ট্রেড নেওয়ার পরামর্শ নয়।",
                    color = Color(0xFFFFD78A), modifier = Modifier.padding(12.dp), fontSize = 13.sp
                )
            }

            Text("Practice market", color = Muted)
            var expanded by remember { mutableStateOf(false) }
            Box {
                OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
                    Text(market, modifier = Modifier.weight(1f))
                    Text("▼")
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    listOf("EUR/USD (Sample)", "BTC/USD (Sample)", "GBP/USD (Sample)", "Gold (Sample)", "Random demo").forEach {
                        DropdownMenuItem(text = { Text(it) }, onClick = { market = it; expanded = false })
                    }
                }
            }

            Card(colors = CardDefaults.cardColors(containerColor = Panel), shape = RoundedCornerShape(18.dp)) {
                Column(Modifier.fillMaxWidth().padding(18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("PRACTICE RESULT", color = Muted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    Text(signal, color = when(signal) { "BUY" -> Green; "SELL" -> Red; else -> Blue },
                        fontSize = 38.sp, fontWeight = FontWeight.ExtraBold)
                    Text("1-minute learning timeframe • simulated", color = Muted, fontSize = 12.sp)
                    Spacer(Modifier.height(8.dp))
                    Text(note, color = Color.White, fontSize = 13.sp)
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                MetricCard("RSI (sample)", rsi.toString(), Modifier.weight(1f))
                MetricCard("MACD (sample)", macd, Modifier.weight(1f))
            }

            Button(
                onClick = {
                    rsi = Random.nextInt(25, 76)
                    macd = listOf("Positive", "Negative", "Neutral").random()
                    signal = when {
                        rsi < 35 && macd == "Positive" -> "BUY"
                        rsi > 65 && macd == "Negative" -> "SELL"
                        else -> "WAIT"
                    }
                    note = when(signal) {
                        "BUY" -> "Sample conditions align upward. Educational example only."
                        "SELL" -> "Sample conditions align downward. Educational example only."
                        else -> "Sample indicators do not align clearly. Waiting is part of risk awareness."
                    }
                    history = (listOf("$market • $signal • RSI $rsi • $macd") + history).take(8)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Blue)
            ) { Text("GENERATE PRACTICE ANALYSIS", color = Navy, fontWeight = FontWeight.Bold) }

            OutlinedButton(
                onClick = { signal = "WAIT"; rsi = 50; macd = "Neutral"; note = "Reset complete. Generate a new practice analysis."; history = emptyList() },
                modifier = Modifier.fillMaxWidth()
            ) { Text("RESET DEMO") }

            Text("Practice history", fontWeight = FontWeight.Bold, fontSize = 17.sp)
            if (history.isEmpty()) Text("No practice results yet.", color = Muted)
            history.forEach { item ->
                Card(colors = CardDefaults.cardColors(containerColor = Panel)) {
                    Text(item, modifier = Modifier.fillMaxWidth().padding(12.dp), fontSize = 12.sp)
                }
            }
            Text(
                "এই অ্যাপ ইন্টারনেট ব্যবহার করে না, Quotex-এর সঙ্গে সংযুক্ত নয়, এবং কোনো অর্ডার দেয় না। BUY/SELL ফলাফলগুলো র‍্যান্ডম ডেমো ইনপুটের শিক্ষামূলক উদাহরণ।",
                color = Muted, fontSize = 11.sp
            )
        }
    }
}

@Composable
fun MetricCard(title: String, value: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = Panel), shape = RoundedCornerShape(14.dp)) {
        Column(Modifier.padding(14.dp)) {
            Text(title, color = Muted, fontSize = 11.sp)
            Spacer(Modifier.height(5.dp))
            Text(value, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 19.sp)
        }
    }
}
