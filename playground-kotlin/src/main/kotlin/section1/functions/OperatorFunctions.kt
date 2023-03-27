package section1.functions


fun main() {
    // 연산자 메서드는
    // 메인 메서드 밖에서 선언되지 않음
    operator fun Int.times(str: String) = str.repeat(this)
    println(2 * "Bye ")

    operator fun String.get(range: IntRange) = substring(range)
    val str = "Always forgive your enemies; nothing annoys them so much."
    println(str[0..14])
}
