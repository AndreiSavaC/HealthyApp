package com.proiectpdm.plugins

import com.proiectpdm.di.appModule
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin

fun Application.configureDI() {
    install(Koin) {
        modules(appModule)
    }
}