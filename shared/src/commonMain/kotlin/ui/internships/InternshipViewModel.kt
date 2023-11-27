package ui.internships

import com.rickclephas.kmm.viewmodel.KMMViewModel
import domain.usecase.StreamInternshipUseCase

class InternshipViewModel(private val streamInternshipUseCase: StreamInternshipUseCase) : KMMViewModel() {
    var internships = streamInternshipUseCase.invoke("")

    fun searchInternships(query: String) {
        internships = streamInternshipUseCase.invoke(query)
    }
}