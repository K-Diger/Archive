package com.group.libraryapp.domain.user

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.loanhistory.UserLoanHistory
import javax.persistence.*

@Entity
class User(

    var name: String,
    val age: Int,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: String,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val userLoanHistories: MutableList<UserLoanHistory> = mutableListOf()
) {

    init {
        if (name.isBlank()) {
            throw IllegalArgumentException("이름은 비어있을 수 없습니다.")
        }
    }

    fun updateName(name: String) {
        this.name = name
    }

    fun loanBook(book: Book) {
        this.userLoanHistories.add(UserLoanHistory(this, book.name, false))
    }

    // 현재 유저가 가지고 있는 도서 대출 목록 첫 번째 책의 이름을 찾아서
    // 매개변수로 들어온 책 이름과 비교한다.
    fun returnBook(bookName: String) {
        this.userLoanHistories.first { history -> history.bookName == bookName} .doReturn()
    }
}