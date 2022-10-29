package oganesyan.rsoi_lab2.service

import oganesyan.rsoi_lab2.model.GatewayLibraryRequest
import oganesyan.rsoi_lab2.model.GatewayLibraryResponse
import org.springframework.transaction.annotation.Transactional

interface GatewayLibraryService {
    @Transactional(readOnly = true)
    fun getLibraryByCity(libraryRequest: GatewayLibraryRequest): GatewayLibraryResponse
}