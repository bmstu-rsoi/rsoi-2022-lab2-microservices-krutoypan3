package oganesyan.rsoi_lab2.service

import oganesyan.rsoi_lab2.model.library_book.CreateLibraryBookRequest
import oganesyan.rsoi_lab2.model.library_book.LibraryBookInfo
import oganesyan.rsoi_lab2.model.library_book.LibraryBookInfoResponse
import oganesyan.rsoi_lab2.repository.LibraryBookRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@Transactional
@Service
open class LibraryBooksServiceImpl @Autowired constructor(
    private val entityManager: EntityManager, private val libraryBookRepository: LibraryBookRepository
): LibraryBookService {
    override fun getBooksIdByLibraryId(library_id: Int?): LibraryBookInfoResponse {
        entityManager.joinTransaction()
        val entities = entityManager.createNativeQuery("SELECT book_id, library_id, available_count FROM library_books WHERE library_id = '$library_id'").resultList
        val list = LibraryBookInfoResponse(arrayListOf())
        entities.forEach {
            val objectArray: Array<Any?>? = it as Array<Any?>? // ПРОСТО АДСКИЕ КОСТЫЛИ,ЧТОБЫ ПРЕОБРАЗОВАТЬ ПОЛУЧЕННЫЙ
            list.books.add(LibraryBookInfo(                    // МАССИВ ОБЪЕКТОВ В ЧЕЛОВЕЧЕСКИЙ ВИД
                objectArray?.get(0)?.toString()?.toInt(),
                objectArray?.get(1)?.toString()?.toInt(),
                objectArray?.get(2)?.toString()?.toInt(),
            ))
        }
        return list
    }

    override fun putLibraryBook(createLibraryBookRequest: CreateLibraryBookRequest) {
        entityManager.createNativeQuery("INSERT INTO library_books (book_id, library_id, available_count) VALUES (?,?,?)")
            .setParameter(1, createLibraryBookRequest.book_id)
            .setParameter(2, createLibraryBookRequest.library_id)
            .setParameter(3, createLibraryBookRequest.available_count)
            .executeUpdate()
    }
}