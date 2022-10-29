package oganesyan.rsoi_lab2.repository

import oganesyan.rsoi_lab2.database.BookEntities
import oganesyan.rsoi_lab2.database.LibraryEntities
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param

interface BookRepository: JpaRepository<BookEntities, Int>