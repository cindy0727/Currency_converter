package com.example.exchange_rate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.exchange_rate.ui.theme.Exchange_rateTheme
import java.text.NumberFormat
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.unit.sp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Exchange_rateTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //APP執行程式
                    ExchangeRateApp()
                }
            }
        }
    }
}

//預覽畫面 呼叫function與MainActivity相同
@Preview(showBackground = true)
@Composable
fun ExchangeRateApp(){
    ExchangeRate()
}

@Composable
fun ExchangeRate (){
    //顯示和紀錄使用者輸入金額
    var amountInput by remember { mutableStateOf("") }
    val amount = amountInput.toDoubleOrNull() ?: 0.0
    //匯率轉換結果
    var result by remember { mutableStateOf("$00.00") }
    //網頁擷取到的當前匯率
    var rate by remember { mutableStateOf("00.00") }
    val url = "https://rate.bot.com.tw/xrt?Lang=zh-TW"

    //UI介面為直向排列
    Column(
        modifier = Modifier.padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        //APP名稱
        Text(
            text = stringResource(R.string.calculate_exchange_rate),
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(alignment = Alignment.Start)
        )
        //輸入金額的欄位
        EditNumberField(
            value = amountInput,
            onValueChange = { amountInput = it },
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth()
        )
        //呼叫爬蟲函式進行資料擷取，並將結果儲存在rate
        LaunchedEffect(url) {
            rate = getData(url)
        }
        //按鈕 按下會進行匯率轉換 並將結果儲存在result
        Button(
            onClick = { result = calculator(amount, rate) }
        ){
            Text(stringResource(R.string.Button_name))
        }
        Spacer(modifier = Modifier.height(16.dp))
        //印出當前匯率
        Text(
            text = stringResource(R.string.Spot_rate) + " " + rate,
            fontSize = 25.sp
        )
        Spacer(modifier = Modifier.height(10.dp))
        //印出匯率轉換結果
        Text(
            text = stringResource(R.string.after_exchange) + " " + result,
            style = MaterialTheme.typography.displaySmall,
            fontSize = 30.sp
        )
        Spacer(modifier = Modifier.height(150.dp))
    }
}

//輸入要轉換的價格
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNumberField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        label = { Text(stringResource(R.string.input_amount)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = modifier
    )
}

//計算匯率
private fun calculator(amount: Double, spot_rate: String): String {
    val result = amount / spot_rate.toDouble()
    return NumberFormat.getCurrencyInstance().format(result)
}




