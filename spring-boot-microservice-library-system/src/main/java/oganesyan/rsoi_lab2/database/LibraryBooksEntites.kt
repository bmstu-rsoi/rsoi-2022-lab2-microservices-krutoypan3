package oganesyan.rsoi_lab2.database

import javax.persistence.*

@Entity
@Table(name = "library_books")
class LibraryBooksEntites {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null

    @Column
    var bookId: Int? = null

    @Column
    var libraryId: Int? = null

    @Column
    var availableCount: Int? = null // Count in library
}