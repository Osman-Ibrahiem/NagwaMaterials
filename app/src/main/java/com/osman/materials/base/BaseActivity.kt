package com.osman.materials.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.domain.core.viewstate.BaseVS
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import java.lang.reflect.ParameterizedType

@ExperimentalCoroutinesApi
@Suppress("UNCHECKED_CAST")
open class BaseActivity<VM : BaseViewModel<I>, I : BaseIntent> : DefaultBaseActivity {

    lateinit var viewModel: VM
    private val viewModelClass: Class<VM>

    constructor() : super() {
        this.viewModelClass =
            (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<VM>
    }

    constructor(viewModelClass: Class<VM>) : super() {
        this.viewModelClass = viewModelClass
    }

    constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId) {
        this.viewModelClass =
            (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<VM>
    }

    constructor(
        viewModelClass: Class<VM>,
        @LayoutRes contentLayoutId: Int,
    ) : super(contentLayoutId) {
        this.viewModelClass = viewModelClass
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[viewModelClass]

        lifecycleScope.launchWhenStarted { viewModel.states.collect(::render) }
    }

    open fun render(state: BaseVS) {
//        if (state != Idle) viewModel.sendState(Idle)
    }

    val intents: Channel<I> get() = viewModel.intentsChannel

    fun sendIntent(intent: I) {
        lifecycleScope.launchWhenStarted {
            intents.send(intent)
        }
    }


}