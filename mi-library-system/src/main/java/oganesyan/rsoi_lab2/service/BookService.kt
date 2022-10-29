package oganesyan.rsoi_lab2.service

import oganesyan.rsoi_lab2.model.book.BookRequest
import oganesyan.rsoi_lab2.model.book.BookResponse
import oganesyan.rsoi_lab2.model.book.CreateBookRequest
import oganesyan.rsoi_lab2.model.library_book.BookIdUidResponse
import org.springframework.transaction.annotation.Transactional

interface BookService {
    @Transactional(readOnly = true)
    fun getBooksByLibrary(bookRequest: BookRequest): BookResponse

    @Transactional(readOnly = true)
    fun getBookIdByUid(book_uid: String?): BookIdUidResponse

    fun putBooks(createBookRequest: CreateBookRequest)
}