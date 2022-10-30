package oganesyan.rsoi_lab2.service

import oganesyan.rsoi_lab2.model.GatewayLibraryRequest
import oganesyan.rsoi_lab2.model.GatewayLibraryResponse
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
open class GatewayLibraryServiceImpl(restTemplateBuilder: RestTemplateBuilder): GatewayLibraryService {

    private val restTemplate = restTemplateBuilder.build()

    override fun getLibraryByCity(libraryRequest: GatewayLibraryRequest): GatewayLibraryResponse {
        val url =
            "http://localhost:8060/library-system/getLibraryByCity?city=${libraryRequest.city}&page=${libraryRequest.page}&size=${libraryRequest.size}"
        return restTemplate.getForObject(url, GatewayLibraryResponse::class.java)!!
    }
}