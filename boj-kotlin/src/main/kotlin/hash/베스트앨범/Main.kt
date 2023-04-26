package main.kotlin.hash.베스트앨범

/*
["classic", "pop", "classic", "classic", "pop"] 	[500, 600, 150, 800, 2500]
 */

private val genres = arrayOf("classic", "pop", "classic", "classic", "pop")
private val plays = arrayOf(500, 600, 150, 800, 2500)

private lateinit var genresRanking: Array<String>

fun main() {

    val result = HashMap<String, Int>()
    repeat(genres.size) { index ->
        if (result[genres[index]] == null) {
            result[genres[index]] = 0
        }
        result[genres[index]] = (plays[index] + result[genres[index]]!!)
    }

    for (mutableEntry in result) {
        println("mutableEntry = ${mutableEntry}")
    }
}