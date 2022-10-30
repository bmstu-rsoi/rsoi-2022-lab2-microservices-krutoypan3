package oganesyan.rsoi_lab2.gateway.service

import oganesyan.rsoi_lab2.gateway.model.GatewayBookResponse
import oganesyan.rsoi_lab2.gateway.model.GatewayBooksByLibraryRequest
import oganesyan.rsoi_lab2.gateway.model.GatewayLibraryRequest
import oganesyan.rsoi_lab2.gateway.model.GatewayLibraryResponse
import org.springframework.transaction.annotation.Transactional

interface GatewayLibraryService {
    @Transactional(readOnly = true)
    fun getLibraryByCity(libraryRequest: GatewayLibraryRequest): GatewayLibraryResponse

    @Transactional(readOnly = true)
    fun getBooksByLibrary(gatewayBooksByLibraryRequest: GatewayBooksByLibraryRequest): GatewayBookResponse
}