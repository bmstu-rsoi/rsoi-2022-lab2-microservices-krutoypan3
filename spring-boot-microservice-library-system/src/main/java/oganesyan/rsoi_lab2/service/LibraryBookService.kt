package oganesyan.rsoi_lab2.service

import oganesyan.rsoi_lab2.model.library_book.CreateLibraryBookRequest
import oganesyan.rsoi_lab2.model.library_book.LibraryBookInfo
import oganesyan.rsoi_lab2.model.library_book.LibraryBookInfoResponse
import org.springframework.transaction.annotation.Transactional

interface LibraryBookService {
    @Transactional(readOnly = true)
    fun getBooksIdByLibraryId(library_id: Int?): LibraryBookInfoResponse
    @Transactional(readOnly = true)
    fun getAvailableCountByBookUidAndLibraryUid(book_uid: String?, library_uid: String?): LibraryBookInfo

    fun putLibraryBook(createLibraryBookRequest: CreateLibraryBookRequest)
}