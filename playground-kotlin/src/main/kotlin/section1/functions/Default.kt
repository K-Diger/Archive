package section1.functions

fun main() {
    printMessage("diger")
    printMessageWithPrefix("diger")
    println(sum(15, 20))
    println(multiply(15, 20))
}

fun printMessage(message: String): Unit {
    println(message)
}

fun printMessageWithPrefix(message: String, prefix: String = "Info") {
    println("[$prefix] $message")
}

fun sum(x: Int, y: Int): Int {
    return x + y
}

fun multiply(x: Int, y: Int) = x * y
