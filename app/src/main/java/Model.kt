
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Model(
    var users: List<User> = emptyList()
)