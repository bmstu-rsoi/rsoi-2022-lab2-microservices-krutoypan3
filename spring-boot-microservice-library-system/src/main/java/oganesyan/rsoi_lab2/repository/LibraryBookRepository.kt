package oganesyan.rsoi_lab2.repository

import oganesyan.rsoi_lab2.database.LibraryBooksEntites
import org.springframework.data.jpa.repository.JpaRepository

interface LibraryBookRepository: JpaRepository<LibraryBooksEntites, Int> {}