package com.nadin.climewatch.data.exptions

import androidx.annotation.StringRes
import com.nadin.climewatch.R

class ServerException(
    @StringRes val messageRes: Int = R.string.server_error_message
) : Exception()