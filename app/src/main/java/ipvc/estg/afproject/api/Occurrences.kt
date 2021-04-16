package ipvc.estg.afproject.api

data class Occurrences (
    val id: Int,
    val titulo: String,
    val descricao: String,
    val imagem: String,
    val latitude: Double,
    val longitude: Double,
    val user_id: Int
)

data class Users (
    val id: Int,
    val name: String,
    val email: String,
    val password: String
)