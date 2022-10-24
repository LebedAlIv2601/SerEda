package com.disgust.sereda.auth.phone.interaction

import com.disgust.sereda.utils.base.BaseUIEvent

sealed class PhoneEnterUIEvent : BaseUIEvent {
    class ButtonGetCodeClick(val phone: String) : PhoneEnterUIEvent()
}