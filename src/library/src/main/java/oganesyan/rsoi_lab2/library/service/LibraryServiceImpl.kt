package oganesyan.rsoi_lab2.service

import oganesyan.rsoi_lab2.database.LibraryEntities
import oganesyan.rsoi_lab2.model.library.CreateLibraryRequest
import oganesyan.rsoi_lab2.model.library.LibraryInfo
import oganesyan.rsoi_lab2.model.library.LibraryRequest
import oganesyan.rsoi_lab2.model.library.LibraryResponse
import oganesyan.rsoi_lab2.model.library_book.LibraryIdUidResponse
import oganesyan.rsoi_lab2.repository.LibraryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID
import javax.persistence.EntityManager

@Transactional
@Service
open class LibraryServiceImpl @Autowired constructor(
    private val entityManager: EntityManager, private val libraryRepository: LibraryRepository,
) : LibraryService {

    private val REQUEST_SIZE: Int = 10
    private val REQUEST_PAGE: Int = 0

    @Transactional(readOnly = true)
    override fun getLibraryByCity(libraryRequest: LibraryRequest): LibraryResponse {
        val libraryEntities = libraryRequest.city?.let { libraryRepository.findByCity(it) }
        return entitiesToResponse(libraryEntities, libraryRequest.page, libraryRequest.size)
    }

    override fun putLibrary(createLibraryRequest: CreateLibraryRequest) {
        insertWithQuery(createLibraryRequest)
    }

    override fun getAllLibrary(page: Int?, size: Int?) = entitiesToResponse(libraryRepository.findAll(), page, size)

    override fun getLibraryIdByUid(library_uid: String?): LibraryIdUidResponse {
        entityManager.joinTransaction()
        val entities2 = entityManager.createNativeQuery("SELECT id FROM library WHERE library_uid = '$library_uid'").resultList
        return if (entities2.isNotEmpty())
            LibraryIdUidResponse(library_id = entities2[0].toString().toInt(), library_uid = library_uid)
        else
            LibraryIdUidResponse(library_id = null, library_uid = library_uid)
    }

    @Transactional
    open fun insertWithQuery(library: CreateLibraryRequest) {
        entityManager.joinTransaction()
        entityManager.createNativeQuery("INSERT INTO library (library_uid, name, city, address) VALUES (?,?,?,?)")
            .setParameter(1, UUID.randomUUID().toString())
            .setParameter(2, library.name)
            .setParameter(3, library.city)
            .setParameter(4, library.address)
            .executeUpdate()
    }

    /** Преобразует LibraryEntities в LibraryRequest с фильтрацией по страницам и кол-ву */
    private fun entitiesToResponse(
        entities: List<LibraryEntities>?,
        _requestPage: Int?,
        _requestSize: Int?,
    ): LibraryResponse {
        entities?.let {
            // Если в БД пусто - возвращаем пустой список
            if (entities.isEmpty())
                return@let

            // Если пользователь в запросе не указал кол-во возвращаемых элементов в запросе, ставим стандартное значение
            val requestSize = _requestSize ?: REQUEST_SIZE

            // Если пользователь в запросе не указал станицу в запросе, ставим стандартное значение
            val requestPage = _requestPage ?: REQUEST_PAGE

            // Вычисляем реальный размер который мы можем вернуть
            val size = if (requestSize <= entities.size) requestSize else entities.size

            // Вычисляем есть ли страница с запрошенным номером, если нет, возвращаем пустой список
            val page =
                if (requestPage <= (entities.size / size + if ((entities.size % size) > 0) 1 else 0)) requestPage else return@let

            // Вырезаем из общего списка элементы с x по y
            val librarySubList = entities.subList(
                page * size,
                if (page * size + size <= entities.size) (page * size + size) else entities.size
            )

            // Превращаем наши LibraryEntities в LibraryInfo
            val libraryInfoItems = mutableListOf<LibraryInfo>()
            librarySubList.forEach {
                libraryInfoItems.add(LibraryInfo(it.library_uid, it.name, it.address, it.city))
            }
            return LibraryResponse(page, libraryInfoItems.size, entities.size, libraryInfoItems)
        }
        // Возвращаем пустой список с кол-вом элементов
        return LibraryResponse(-1, 0, entities?.size ?: 0, emptyList())
    }
}