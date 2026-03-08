package com.nadin.climewatch.data.exptions

import androidx.annotation.StringRes
import com.nadin.climewatch.R

class NetworkException(
    @StringRes val messageRes: Int = R.string.network_error_message
) : Exception()