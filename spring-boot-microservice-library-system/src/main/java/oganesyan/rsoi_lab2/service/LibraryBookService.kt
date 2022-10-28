package oganesyan.rsoi_lab2.service

import oganesyan.rsoi_lab2.model.library_book.CreateLibraryBookRequest
import oganesyan.rsoi_lab2.model.library_book.LibraryBookInfoResponse
import org.springframework.transaction.annotation.Transactional

interface LibraryBookService {
    @Transactional(readOnly = true)
    fun getBooksIdByLibraryId(library_id: Int?): LibraryBookInfoResponse

    fun putLibraryBook(createLibraryBookRequest: CreateLibraryBookRequest)
}