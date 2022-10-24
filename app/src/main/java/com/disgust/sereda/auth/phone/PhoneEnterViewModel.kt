package com.disgust.sereda.auth.phone

import com.disgust.sereda.auth.phone.interaction.PhoneEnterUIEvent
import com.disgust.sereda.utils.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class PhoneEnterViewModel : BaseViewModel<PhoneEnterUIEvent>() {

    override fun onEvent(event: PhoneEnterUIEvent) {
        when (event) {
            is PhoneEnterUIEvent.ButtonGetCodeClick -> {

            }
        }
    }

}