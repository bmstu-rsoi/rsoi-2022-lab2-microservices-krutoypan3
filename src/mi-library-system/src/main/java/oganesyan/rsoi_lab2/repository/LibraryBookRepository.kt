package oganesyan.rsoi_lab2.repository

import oganesyan.rsoi_lab2.database.LibraryBooksEntites
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param

interface LibraryBookRepository: JpaRepository<LibraryBooksEntites, Int> {
    fun findFirstByBookIdAndLibraryId(@Param("book_id") book_id: Int, @Param("library_id") library_id: Int): LibraryBooksEntites
}