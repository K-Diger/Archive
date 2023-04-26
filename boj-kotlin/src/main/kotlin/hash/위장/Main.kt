package main.kotlin.hash.위장

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

private val br: BufferedReader = BufferedReader(InputStreamReader(System.`in`))
private val bw: BufferedWriter = BufferedWriter(OutputStreamWriter(System.out))

// [오늘]
// 동그란 안경
// 긴 코트
// 파란색 티셔츠

// [내일]
// 청바지
// 혹은 동그란 안경 대신 검정 선글라스

private val example: Array<Array<String>> =
    arrayOf(
        arrayOf("yellow_hat", "headgear"),
        arrayOf("blue_sunglasses", "eyewear"),
        arrayOf("green_turban", "headgear")
    )

class Solution {
    fun main(clothes: Array<Array<String>>) = clothes
        .groupBy { it[1] }.values   // group by type of clothes, keep only names of clothes
        .map { it.size + 1 }        // number of things to wear in a group (including wearing nothing)
        .reduce(Int::times)         // combine
        .minus(1)

    fun playground() {
        val words = listOf("a", "abc", "ab", "def", "abcd")

        val byLength = words.groupBy { it.length }

        val byLengthAd = words.groupBy { it.length }
            .values.map { it.size }
            .reduce(Int::times)
            .minus(1)

        println(byLengthAd)

        println(byLength.keys) // [1, 3, 2, 4]
        println(byLength.values) // [[a], [abc, def], [ab], [abcd]]

        val mutableByLength: MutableMap<Int, MutableList<String>> =
            words.groupByTo(mutableMapOf()) { it.length }
        // same content as in byLength map, but the map is mutable
        println("mutableByLength == byLength is ${mutableByLength == byLength}") // true
    }
}