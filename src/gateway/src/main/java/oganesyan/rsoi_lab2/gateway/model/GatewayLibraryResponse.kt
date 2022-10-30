package oganesyan.rsoi_lab2.model

data class GatewayLibraryResponse(
    val page: Int?,
    val pageSize: Int?,
    val totalElements: Int?,
    val items: List<GatewayLibraryInfo>,
)