package com.osman.materials.base

import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment

open class DefaultBaseDialogFragment : DialogFragment {

    constructor() : super()
    constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId)

}