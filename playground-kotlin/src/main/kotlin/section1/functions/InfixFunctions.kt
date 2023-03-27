package section1.functions

infix fun Int.times(str: String) = str.repeat(this)
infix fun String.onto(other: String) = Pair(this, other)


fun main() {
    println(2 times "Bye ")

    // to 라는 키워드는 코틀린 표준 라이브러리에서 제공하는
    // pair 타입을 만들어주는 infix 함수이다.
    val pair = "Ferrari" to "Katrina"
    println(pair)

    val myPair = "McLaren" onto "Lucas"
    println(myPair)

    val sophia = Person("Sopia")
    val claudia = Person("Claudia")
    sophia likes claudia
    println(sophia.likedPeople.get(0).name)
    // println(claudia.likedPeople.get(0).name)
}

class Person(val name: String) {
    val likedPeople = mutableListOf<Person>()
    infix fun likes(other: Person) = likedPeople.add(other)
}

