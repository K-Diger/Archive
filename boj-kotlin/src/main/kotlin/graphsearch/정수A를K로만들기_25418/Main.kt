package main.kotlin.graphsearch.정수A를K로만들기_25418

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.*

private val br: BufferedReader = BufferedReader(InputStreamReader(System.`in`))
private val bw: BufferedWriter = BufferedWriter(OutputStreamWriter(System.out))
private lateinit var graph: Array<Int>
private lateinit var visited: Array<Boolean>

private var count: Int = 0
private val operation: Array<Int> = arrayOf(1, 2)

fun main() {
    val (a: Int, k: Int) = br.readLine().split(" ").map { it.toInt() }
    graph = Array(k) { 0 }
    visited = Array(k) { false }
    bfs(a, k)
}

fun bfs(a: Int, k: Int) {
    val queue: Queue<IntArray> = LinkedList()
    queue.add(intArrayOf())

}