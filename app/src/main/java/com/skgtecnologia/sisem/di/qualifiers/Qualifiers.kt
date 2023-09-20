@file:Suppress("MatchingDeclarationName")

package com.skgtecnologia.sisem.di.qualifiers

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BasicAuthentication

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BearerAuthentication
