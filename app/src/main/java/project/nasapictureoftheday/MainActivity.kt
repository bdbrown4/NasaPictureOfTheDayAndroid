package project.nasapictureoftheday

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.method.ScrollingMovementMethod
import android.widget.Button
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.concurrent.Executors


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var date: DatePicker
    private lateinit var textView: TextView
    private lateinit var button: Button
    private lateinit var image: Bitmap
    private val mainViewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        button.setOnClickListener {
            lifecycleScope.launch {
                var pictureOfTheDay = mainViewModel.processRequest(Constants.API_KEY, convertDatePickerSelectedDateToString())
                processResponse(pictureOfTheDay)
            }
        }
    }

    private fun initViews() {
        imageView = findViewById(R.id.imageView)
        textView = findViewById(R.id.textView)
        date = findViewById(R.id.editTextDate)
        button = findViewById(R.id.button)
    }

    private fun convertDatePickerSelectedDateToString(): String {
        val year = date.year
        val month = padIntLessThan10(date.month + 1)
        val day = padIntLessThan10(date.dayOfMonth)
        return "${year}-${month}-${day}"
    }

    private fun padIntLessThan10(number: Int): String = if (number >= 10) number.toString() else "0${number}"

    private fun processResponse(pictureOfTheDay: PictureOfTheDay) {
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute {
            val imageURL = pictureOfTheDay.url
            try {
                val stream = java.net.URL(imageURL).openStream()
                image = BitmapFactory.decodeStream(stream)
                handler.post {
                    imageView.setImageBitmap(image)
                    textView.text = pictureOfTheDay.explanation
                    textView.movementMethod = ScrollingMovementMethod()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}