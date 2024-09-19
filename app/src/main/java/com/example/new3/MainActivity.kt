package com.example.new3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.new3.ui.theme.New3Theme
import kotlin.random.Random
import android.content.Context
import android.media.MediaPlayer
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            New3Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    mainScreen()
                }
            }
        }
    }
}


@Composable
fun mainScreen() {
    // Класс для хранения пар слов и переводов
    data class WordPair(val word: String, val translation: String)


// Исходная карта
    val wordMap = mapOf(
        "Although" to "Хотя",
        "provide" to "предоставлять",
        "flexibility" to "гибкость",
        "inevitably" to "неизбежно",
        "straightforward " to "простой",
        "make" to "сделать",
        "go" to "идти",
        "know" to "знать",
        "take" to "брать",
        "see" to "видеть",
        "come" to "приходить",
        "think" to "думать",
        "look" to "смотреть",
        "want" to "хотеть",
        "give" to "давать",
        "use" to "использовать",
        "find" to "находить",
        "tell" to "рассказать",
        "ask" to "спрашивать",
        "work" to "работать",
        "seem" to "казаться",
        "feel" to "чувствовать",
        "try" to "попробовать",
        "leave" to "уходить",
        "call" to "звонить",
        "good" to "хорошо",
        "help" to "помогать",
        "talk" to "говорить",
        "turn" to "поворачивать",
        "start" to "начинать",
        "show" to "показывать",
        "hear" to "слышать",
        "play" to "играть",
        "run" to "бежать"
    )
    var textContent by remember { mutableStateOf("") }
    var score by remember { mutableStateOf(0) }
    var successTry by remember { mutableStateOf(0) }
    var failier by remember { mutableStateOf(0) }
    var CountQuestion by remember { mutableStateOf(0) }
    var newMap by remember { mutableStateOf(mutableMapOf<String, String>()) }


    fun resetNewMap() {
        newMap = mutableMapOf()

    }

    // Функция для перемешивания значений с сохранением 50% на месте
    fun shuffleHalfMapValues(map: Map<String, String>): Map<String, String> {
        val entries = map.entries.toList()
        val totalEntries = entries.size
        val retainCount = totalEntries / 2 // Оставляем 50% значений на месте

        // Перемешиваем записи
        val shuffledEntries = entries.shuffled(Random.Default)

        // Выбираем те записи, которые останутся неизменными
        val retainedEntries = shuffledEntries.take(retainCount)
        // Оставшиеся записи для перемешивания
        val toShuffleEntries = shuffledEntries.drop(retainCount)

        // Перемешиваем значения в оставшихся записях
        val shuffledValues = toShuffleEntries.map { it.value }.shuffled(Random.Default)

        // Создаём новую Map
        val newMap = mutableMapOf<String, String>()

        // Добавляем неизмененные записи
        retainedEntries.forEach { newMap[it.key] = it.value }

        // Добавляем перемешанные записи
        toShuffleEntries.zip(shuffledValues).forEach { (entry, shuffledValue) ->
            newMap[entry.key] = shuffledValue
        }

        return newMap
    }

// Применяем функцию к исходной Map
    var shuffledMap = shuffleHalfMapValues(wordMap)

    // Переменная для хранения случайного элемента
    var randomEntry by remember { mutableStateOf(shuffledMap.entries.random()) }

    // Функция для обновления случайного элемента
    fun pickRandomEntry() {
        randomEntry = shuffledMap.entries.random()
        // Функция для перемешивания карты
        val keys = shuffledMap.keys.toList()
        val values = shuffledMap.values.toList().shuffled(Random)
        shuffledMap = keys.zip(values).toMap().toMutableMap()
        CountQuestion = (CountQuestion + 1)
    }

    // Инициализация MediaPlayer
    val context = LocalContext.current
    val mediaPlayer = remember { MediaPlayer.create(context, R.raw.click_sound) }
    val successSound = remember { MediaPlayer.create(context, R.raw.success) }
    val failSound = remember { MediaPlayer.create(context, R.raw.fail) }




    // Функция для добавления в новую Map
    fun addToNewMap() {
        newMap[randomEntry.key] = randomEntry.value
            // textContent = "Добавлено: ${randomEntry.key} -> ${randomEntry.value}"
    }


    fun createNewMap(newMap: Map<String, String>, wordMap: Map<String, String>): Map<String, String> {
        val resultMap = mutableMapOf<String, String>()

        for ((key, value) in newMap) {
            // Ищем значение в оригинальной wordMap по ключу из newMap
            val originalValue = wordMap[key]
            // Если значение найдено, добавляем в результат
            if (originalValue != null) {
                resultMap[key] = originalValue
            }
        }
        return resultMap
    }
    val thirdMap = createNewMap(newMap, wordMap)


    // Функция для сравнения элемента
    fun checkIfCorrect() {
        val originalValue = wordMap[randomEntry.key]
        if (originalValue == randomEntry.value) {
            textContent = "Угадал"
            score = (score + 1)
            successTry = (successTry + 1)
            successSound.start() // Воспроизведение звука
        } else {
            textContent = "Не угадал"
            score = (score - 1)
            failier = (failier + 1)
            addToNewMap()
            failSound.start() // // Воспроизведение звука
        }
    }

    fun checkifNotCorrect() {
        val originalValue = wordMap[randomEntry.key]
        if (originalValue !== randomEntry.value) {
            textContent = "Угадал"
            score = (score + 1)
            successTry = (successTry + 1)
            successSound.start() // Воспроизведение звука
        } else {
            failSound.start() // // Воспроизведение звука
            textContent = "Не угадал"
            score = (score - 1)
            failier = (failier + 1)
            addToNewMap()
        }
    }






    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Правильных ответов: $successTry",
            modifier = Modifier.padding(top = 16.dp),
            fontSize = 20.sp
        )
        Text(
            "Неправильных ответов: $failier",
            modifier = Modifier.padding(top = 16.dp),
            fontSize = 20.sp
        )
        Text("всего вопросов: $CountQuestion")
    }
    Column(
        modifier = Modifier
            .fillMaxSize()  // Занять всю доступную площадь экрана
            .padding(16.dp),  // Внутренние отступы
        verticalArrangement = Arrangement.Center,  // Центровка по вертикали
        horizontalAlignment = Alignment.CenterHorizontally  // Центровка по горизонтали
    ) {

        if (CountQuestion <= 4) {
            Text(

                text = "${randomEntry.key} -> ${randomEntry.value}",
                fontSize = 30.sp,  // Увеличенный размер текста
                modifier = Modifier.padding(top = 16.dp)

            )
            Row(Modifier.padding(20.dp)) {
                Button(
                    onClick = {
                        checkIfCorrect()
                        pickRandomEntry()
                        mediaPlayer.start() // Воспроизведение звука

                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                ) {
                    Text(text = "Правильно", fontSize = 20.sp)

                }

                Button(
                    onClick = {
                        checkifNotCorrect()
                        pickRandomEntry()
                        mediaPlayer.start() // Воспроизведение звука
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text(text = "Нет", fontSize = 20.sp)
                }
            }

            Text(text = textContent, modifier = Modifier.padding(top = 16.dp))
            Text("Баллы: $score", fontSize = 15.sp)
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp), // Добавьте отступы, если необходимо
                verticalArrangement = Arrangement.Center, // Центрируем по вертикали
                horizontalAlignment = Alignment.CenterHorizontally // Центрируем по горизонтали
            ) {
                Text("Баллы: $score", fontSize = 15.sp)
                Button(onClick = {
                    CountQuestion = 0
                    score = 0
                    successTry = 0
                    failier = 0
                    resetNewMap()
                }) {
                    Text("Следующий раунд")

                }
                if (thirdMap.isEmpty()) {
                    Text("Ошибок не было допущено")
                }
                else {
                    Text("Слова, в которых были допущены ошибки", fontSize = 25.sp,)
                    for ((item, value) in thirdMap)
                        Column {
                            Text("$item -> $value")
                        }
                }
            }
        }




    }


// Освобождение ресурсов MediaPlayer
    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer.release()
        }
    }

    // Освобождение ресурсов successSound
    DisposableEffect(Unit) {
        onDispose {
            successSound.release()
        }
    }
    // Освобождение ресурсов FailSound
    DisposableEffect(Unit) {
        onDispose {
            failSound.release()
        }
    }




}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    New3Theme {
        mainScreen()
    }
}