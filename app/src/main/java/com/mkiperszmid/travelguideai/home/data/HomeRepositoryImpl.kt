package com.mkiperszmid.travelguideai.home.data

import com.mkiperszmid.travelguideai.home.data.remote.ChatgptApi
import com.mkiperszmid.travelguideai.home.data.remote.dto.ChatRequestDto
import com.mkiperszmid.travelguideai.home.domain.HomeRepository
import com.mkiperszmid.travelguideai.home.domain.model.HomeFilterSettings
import com.mkiperszmid.travelguideai.home.domain.model.Place
import com.mkiperszmid.travelguideai.home.domain.model.Region

class HomeRepositoryImpl(
    private val api: ChatgptApi
) : HomeRepository {
    override suspend fun getTravelGuide(
        location: String,
        settings: HomeFilterSettings
    ): Result<String> {
        return try {
            var places = ""
            if (settings.hospitales) places += "Hospitales/Clinicas, "
            if (settings.veterinarias) places += "Veterinarias, "

            val placesToVisit = if (places.isNotEmpty()) "y quiero visitar: $places" else ""

            val request = ChatRequestDto(
                maxTokens = 1500,
                model = "text-davinci-003",
                "Soy un doctor. Te voy a dar mi ubicación, y me vas a sugerir todos los hospitales o clinicas y veterinarias cerca. También te voy a dar los tipo de lugares que quiero visitar, que estén cerca de mi primera ubicación. Estoy en  $location $placesToVisit. Solo quiero la direccion de cada lugar. Ordenarlos por tipo de lugar. No repitas los lugares. Muestra en un formato \"Nombre:\"/n\"Ubicacion:\"",

                temperature = 0.7
            )
            val information = api.getTravelInformation(request)
            Result.success(information.choices.first().text)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

//    override suspend fun getPopularPlaces(): Result<List<Place>> {
//        return Result.success(
//            listOf(
//                Place("USA", "New York", Region.AMERICA, "https://images.pexels.com/photos/2224861/pexels-photo-2224861.png?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"),
//                Place("Argentina", "Salta", Region.AMERICA, "https://images.pexels.com/photos/13430634/pexels-photo-13430634.jpeg?auto=compress"),
//                Place("España", "Barcelona", Region.EUROPA, "https://www.fodors.com/wp-content/uploads/2022/03/Hero-UPDATEBarcelona-iStock-1320014700-1.jpg"),
//                Place("Australia", "Sydney", Region.OCEANIA, "https://images.squarespace-cdn.com/content/v1/55ee34aae4b0bf70212ada4c/1577545161018-1F9Z9ZZQG9JO2O4WCWQX/keith-zhu-qaNcz43MeY8-unsplash+%281%29.jpg"),
//                Place("Japon", "Tokio", Region.ASIA, "https://lonelyplanetes.cdnstatics2.com/sites/default/files/styles/max_1300x1300/public/fotos/japon_tokio_shibuya_shutterstock_666197236_f11photo_shutterstock.jpg"),
//                Place("Italia", "Roma", Region.EUROPA, "https://www.thediaryofanomad.com/wp-content/w3-webp/uploads/2020/11/rome-for-3-days-in-rome-itinerary-vatican-dome-view.jpgw3.webp")
//            )
//        )
//    }
}
