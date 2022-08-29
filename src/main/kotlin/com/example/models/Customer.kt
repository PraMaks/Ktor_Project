import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id

@Serializable
data class Customer(
    @BsonId
    val id: Id<String>? = null,
    val firstName: String,
    val lastName: String,
    val email: String
    )

val customerStorage = mutableListOf<Customer>()