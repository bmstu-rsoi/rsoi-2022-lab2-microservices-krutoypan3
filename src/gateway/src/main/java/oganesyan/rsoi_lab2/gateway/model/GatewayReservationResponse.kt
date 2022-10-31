package oganesyan.rsoi_lab2.gateway.model

data class GatewayReservationResponse(
    var status: String,
    var startDate: String,
    var tillDate: String,
    var reservation_uid: String,

    var book: GatewayBookInfo,
    var library: GatewayLibraryInfo,
)