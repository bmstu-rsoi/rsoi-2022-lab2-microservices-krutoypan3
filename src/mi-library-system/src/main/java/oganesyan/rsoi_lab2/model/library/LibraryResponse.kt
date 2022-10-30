package oganesyan.rsoi_lab2.model.library

import oganesyan.rsoi_lab2.model.library.LibraryInfo

data class LibraryResponse(
    val page: Int?,
    val pageSize: Int?,
    val totalElements: Int?,
    val items: List<LibraryInfo>
)