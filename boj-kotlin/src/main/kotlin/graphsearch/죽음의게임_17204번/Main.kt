package main.kotlin.graphsearch.죽음의게임_17204번

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

private val br: BufferedReader = BufferedReader(InputStreamReader(System.`in`))
private val bw: BufferedWriter = BufferedWriter(OutputStreamWriter(System.out))

private lateinit var graph: Array<Array<Int>>
private lateinit var visited: Array<Boolean>

fun main() {
    val (n: Int, k: Int) = br.readLine().split(" ").map { it.toInt() }

    graph = Array(n + 1) { Array(n + 1) { 0 } }
    visited = Array(n + 1) { false }

    repeat(n) {

    }
}