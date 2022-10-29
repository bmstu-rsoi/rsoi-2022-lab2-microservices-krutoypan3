package oganesyan.rsoi_lab2.service

import oganesyan.rsoi_lab2.model.library.CreateLibraryRequest
import oganesyan.rsoi_lab2.model.library_book.LibraryIdUidResponse
import oganesyan.rsoi_lab2.model.library.LibraryRequest
import oganesyan.rsoi_lab2.model.library.LibraryResponse
import org.springframework.transaction.annotation.Transactional

interface LibraryService {
    @Transactional(readOnly = true)
    fun getLibraryByCity(libraryRequest: LibraryRequest): LibraryResponse

    fun putLibrary(createLibraryRequest: CreateLibraryRequest)

    @Transactional(readOnly = true)
    fun getAllLibrary(page: Int?, size: Int?): LibraryResponse

    @Transactional(readOnly = true)
    fun getLibraryIdByUid(library_uid: String?): LibraryIdUidResponse
}