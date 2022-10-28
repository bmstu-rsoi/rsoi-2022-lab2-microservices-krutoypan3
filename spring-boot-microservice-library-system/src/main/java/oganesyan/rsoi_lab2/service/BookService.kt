package oganesyan.rsoi_lab2.service

import oganesyan.rsoi_lab2.model.book.BookRequest
import oganesyan.rsoi_lab2.model.book.BookResponse
import oganesyan.rsoi_lab2.model.book.CreateBookRequest
import org.springframework.transaction.annotation.Transactional

interface BookService {
    @Transactional(readOnly = true)
    fun getBooksByLibrary(bookRequest: BookRequest): BookResponse

    fun putBooks(createBookRequest: CreateBookRequest)
}