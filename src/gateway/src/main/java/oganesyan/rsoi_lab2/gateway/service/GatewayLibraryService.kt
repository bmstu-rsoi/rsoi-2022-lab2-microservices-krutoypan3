package oganesyan.rsoi_lab2.gateway.service

import oganesyan.rsoi_lab2.gateway.model.*
import org.springframework.transaction.annotation.Transactional

interface GatewayLibraryService {
    fun getLibraryByCity(libraryRequest: GatewayLibraryRequest): GatewayLibraryResponse

    fun getBooksByLibrary(gatewayBooksByLibraryRequest: GatewayBooksByLibraryRequest): GatewayBookResponse

    fun getRating(username: String): GatewayRatingResponse
}