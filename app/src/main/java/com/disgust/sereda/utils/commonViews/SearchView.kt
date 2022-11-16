package com.disgust.sereda.utils.commonViews

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction

@ExperimentalComposeUiApi
@Composable
fun SearchView(
    value: String,
    onSearch: () -> Unit,
    onValueChange: (String) -> Unit,
    showKeyboardValue: Boolean,
    setShowKeyboard: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboard = LocalSoftwareKeyboardController.current

    TextField(
        value = value,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
            focusManager.clearFocus()
            onSearch()
        }),
        onValueChange = onValueChange,
        modifier = Modifier.focusRequester(focusRequester)
    )

    LaunchedEffect(focusRequester) {
        if (showKeyboardValue) {
            focusRequester.requestFocus()
            keyboard?.show()
            setShowKeyboard()
        }
    }
}

