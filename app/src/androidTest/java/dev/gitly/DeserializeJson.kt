package dev.gitly

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dev.gitly.model.data.User
import org.junit.Test
import org.junit.runner.RunWith
import java.io.InputStreamReader

@RunWith(AndroidJUnit4::class)
class DeserializeJson {

    @Test
    fun deserialize() {
        val context = InstrumentationRegistry.getInstrumentation().context
        with(Gson()) {
            val fromJson = this.fromJson(
                InputStreamReader(context.assets.open("sample_users.json")),
                object : TypeToken<List<User>>() {}.rawType
            )
            print(fromJson)
        }
    }

}