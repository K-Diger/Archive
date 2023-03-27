package graphsearch.바닥장식_1388번

import java.io.*
import java.util.*

object Main {
    private val br = BufferedReader(InputStreamReader(System.`in`))
    private val bw = BufferedWriter(OutputStreamWriter(System.out))
    private lateinit var graph: Array<IntArray>
    private lateinit var visited: Array<BooleanArray>
    private val directionX = intArrayOf(0, 0, -1, 1, 1, -1, -1, 1)
    private val directionY = intArrayOf(-1, 1, 0, 0, 1, -1, 1, -1)
    private var queue: Queue<IntArray> = LinkedList()
    private var w = 0
    private var h = 0
    private var count = 0
    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        while (true) {
            var st = StringTokenizer(br.readLine())
            w = st.nextToken().toInt()
            h = st.nextToken().toInt()
            count = 0
            if (w == 0 && h == 0) {
                break
            }
            graph = Array(h) { IntArray(w) }
            visited = Array(h) { BooleanArray(w) }
            queue = LinkedList()
            for (i in 0 until h) {
                st = StringTokenizer(br.readLine())
                for (j in 0 until w) {
                    graph[i][j] = st.nextToken().toInt()
                }
            }
            for (i in 0 until h) {
                for (j in 0 until w) {
                    if (graph[i][j] == 1 && !visited[i][j]) {
                        bfs(i, j)
                        count++
                    }
                }
            }
            bw.write(
                """
    $count
    
    """.trimIndent()
            )
        }
        bw.close()
        br.close()
    }

    private fun bfs(x: Int, y: Int) {
        queue.add(intArrayOf(x, y))
        visited[x][y] = true
        while (!queue.isEmpty()) {
            val current = queue.poll()
            val currentX = current[0]
            val currentY = current[1]
            for (i in 0..7) {
                val nextX = currentX + directionX[i]
                val nextY = currentY + directionY[i]
                if (nextX >= 0 && nextY >= 0 && nextX < h && nextY < w) {
                    if (!visited[nextX][nextY] && graph[nextX][nextY] == 1) {
                        visited[nextX][nextY] = true
                        queue.add(intArrayOf(nextX, nextY))
                    }
                }
            }
        }
    }
}