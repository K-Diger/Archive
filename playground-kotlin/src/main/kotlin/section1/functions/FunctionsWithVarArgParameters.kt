package section1.functions

fun printAll(vararg messages: String) {
    for (m in messages) println(m)
}

fun printAllWithPrefix(vararg messages: String, prefix: String) {
    for (m in messages) println(prefix + m)
}

fun log(vararg entries: String) {
    printAll(*entries)
}


fun main() {

    printAll("Hello", "Hallo", "Salut", "Hola", "ㅎㅇ")
    printAllWithPrefix(
        "Hello", "Hallo", "Salut", "Hola", "ㅎㅇ",
        prefix = "Greeting!! "
    )
    log("Hello", "Hallo", "Salut", "Hola")
}