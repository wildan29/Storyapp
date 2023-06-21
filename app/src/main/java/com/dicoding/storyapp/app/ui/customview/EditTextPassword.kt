package com.dicoding.storyapp.app.ui.customview

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.storyapp.R

class EditTextPassword @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = com.google.android.material.R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyle) {

    private val _isWrong = MutableLiveData(false)
    val isWrong: LiveData<Boolean>
        get() = _isWrong

    init {
        stateActive()
    }

    private fun stateActive() {
        doOnTextChanged { text, _, _, _ ->
            if (!text.isNullOrEmpty()) {
                if (text.length < 8) {
                    setError(
                        resources.getString(R.string.error_message), null
                    )
                    _isWrong.postValue(false)
                } else {
                    error = null
                    _isWrong.postValue(true)
                }
            }
        }
    }
}