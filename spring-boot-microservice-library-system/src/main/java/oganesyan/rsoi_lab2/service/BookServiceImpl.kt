package oganesyan.rsoi_lab2.service

import oganesyan.rsoi_lab2.database.BookEntities
import oganesyan.rsoi_lab2.model.book.BookInfo
import oganesyan.rsoi_lab2.model.book.BookRequest
import oganesyan.rsoi_lab2.model.book.BookResponse
import oganesyan.rsoi_lab2.model.book.CreateBookRequest
import oganesyan.rsoi_lab2.model.library_book.LibraryBookInfoResponse
import oganesyan.rsoi_lab2.model.library_book.LibraryBookResponse
import oganesyan.rsoi_lab2.repository.BookRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import javax.persistence.EntityManager


@Transactional
@Service
open class BookServiceImpl @Autowired constructor(
    private val entityManager: EntityManager,
    private val bookRepository: BookRepository,
    restTemplateBuilder: RestTemplateBuilder,
) : BookService {

    private val REQUEST_SIZE: Int = 10
    private val REQUEST_PAGE: Int = 0
    private val restTemplate = restTemplateBuilder.build();

    override fun getBooksByLibrary(bookRequest: BookRequest): BookResponse {

        // Здесь отправляем запрос на получение ID библиотеки по UID
        var url = "http://localhost:8100/library-system/getLibraryIDByUID?library_uid=${bookRequest.library_uid}"
        val library_id = restTemplate.getForObject(url, LibraryBookResponse::class.java)?.library_id
        library_id?.let { libraryID ->
            // Здесь отправляет запрос на получение ID книг по ID библиотеки
            url = "http://localhost:8100/library-system/library-books/getBooksIdByLibraryId?library_id=$libraryID"

            val booksID = restTemplate.getForObject(url, LibraryBookInfoResponse::class.java)?.books
            if (booksID?.isNotEmpty() == true) {
                // Здесь запрашиваем книги по их ID
                val books: ArrayList<BookEntities> = arrayListOf()
                val booksCount: ArrayList<Int?> = arrayListOf()
                booksID.forEach { book ->
                    books.add(bookRepository.findById(book.book_id ?: -1).get())
                    booksCount.add(book.available_count)
                }
                return entitiesToResponse(bookEntities = books, bookRequest = bookRequest, count = booksCount)
            }
        }
        return entitiesToResponse(bookEntities = null, bookRequest = bookRequest)
    }

    override fun putBooks(createBookRequest: CreateBookRequest) {
        insertWithQuery(createBookRequest)
    }

    @Transactional
    open fun insertWithQuery(bookRequest: CreateBookRequest) {
        entityManager.joinTransaction()
        entityManager.createNativeQuery("INSERT INTO books (book_uid, name, author, genre, condition) VALUES (?,?,?,?,?)")
            .setParameter(1, UUID.randomUUID().toString())
            .setParameter(2, bookRequest.name)
            .setParameter(3, bookRequest.author)
            .setParameter(4, bookRequest.genre)
            .setParameter(5, bookRequest.condition)
            .executeUpdate()
    }


    private fun entitiesToResponse(
        bookEntities: List<BookEntities>?,
        bookRequest: BookRequest,
        count: ArrayList<Int?> = arrayListOf(),
    ): BookResponse {
        bookEntities?.let {
            // Если в БД пусто - возвращаем пустой список
            if (bookEntities.isEmpty())
                return@let
            // Если пользователь в запросе не указал кол-во возвращаемых элементов в запросе, ставим стандартное значение
            val requestSize = bookRequest.size ?: REQUEST_SIZE

            // Если пользователь в запросе не указал станицу в запросе, ставим стандартное значение
            val requestPage = bookRequest.page ?: REQUEST_PAGE

            // TODO ТУТ МЫ ДОЛЖНЫ ПОСЛЕ ЭТОГО ПРОВЕРЯТЬ ЗАБРОНИРОВАННЫЕ КНИГИ // Надо тут это обдумать \\ Ну или не тут
            val showAll = bookRequest.showAll ?: false

            // Вычисляем реальный размер который мы можем вернуть
            val size = if (requestSize <= bookEntities.size) requestSize else bookEntities.size

            // Вычисляем есть ли страница с запрошенным номером, если нет, возвращаем пустой список
            val page =
                if (requestPage <= (bookEntities.size / size + if ((bookEntities.size % size) > 0) 1 else 0)) requestPage else return@let

            // Вырезаем из общего списка элементы с x по y
            val bookSubList = bookEntities.subList(
                page * size,
                if (page * size + size <= bookEntities.size) (page * size + size) else bookEntities.size
            )

            // Превращаем наши BookEntities в BookInfo
            val bookInfoItems = mutableListOf<BookInfo>()
            bookSubList.forEachIndexed { index, book ->
                if (!(bookRequest.showAll != true && ((count.getOrNull(index) ?: 0) == 0)))
                    bookInfoItems.add(
                        BookInfo(
                            book.book_uid,
                            book.name,
                            book.author,
                            book.genre,
                            book.condition,
                            count.getOrNull(index)?.toLong()
                                ?: 0 // TODO ТУТ надо считать кол-во оставшихся таких-же книг, а не общее кол-во в библиотеке
                        )
                    )
            }
            return BookResponse(page, bookInfoItems.size, bookEntities.size, bookInfoItems)
        }
        return BookResponse(-1, 0, bookEntities?.size ?: 0, emptyList())
    }
}