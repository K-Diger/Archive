package main.kotlin.hash.전화번호목록

//private val phoneBook = arrayOf("119", "97674223", "1195524421")
//private val phoneBook = arrayOf("123","456","789")
private val phoneBook = arrayOf("12", "123", "1235", "567", "88")

fun main() {
    for (i: Int in 0..phoneBook.size - 2) {
        for (j: Int in i + 1..phoneBook.size - 1) {
            if (phoneBook.get(j).startsWith(phoneBook.get(i))) {
                println(false)
                return
            }
        }
    }
    println(true)
    return
}