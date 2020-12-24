
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
class User{
    @SerializedName("Description")
    var description: String = ""
    @SerializedName("Topic")
    var topic: String= ""
    @SerializedName("url")
    var url: String= ""

    //Add this
    constructor()

}

