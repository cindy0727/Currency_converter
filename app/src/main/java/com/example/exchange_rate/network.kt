package com.example.exchange_rate

import org.jsoup.Jsoup
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

//擷取資料
internal suspend fun getData(url: String): String {
    return try {
        withContext(Dispatchers.IO) {
            //使用Jsoup擷取將完整的html
            val document = Jsoup.connect(url).get()
            //回傳結果 所需匯率在第二個td項
            document.select("td")[1].text()
        }
    } catch (e: IOException) {
        "Error fetching website source: ${e.message}"
    }
}

