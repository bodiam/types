package com.github.kotools.csvfile.api

import com.github.kotools.csvfile.core.reader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

/**
 * Creates a [reader][ReaderApi] with the given [configuration][config] and
 * reads the corresponding [file][ReaderApi.file]'s content.
 *
 * Returns `null` if the [file][ReaderApi.file] doesn't exist.
 */
public suspend fun csvReader(config: ReaderApi.() -> Unit):
        List<Map<String, String>>? = withContext(IO) { reader(config) }

/**
 * Creates a [reader][ReaderApi] with the given [configuration][config] and
 * reads the corresponding [file][ReaderApi.file]'s content **asynchronously**.
 *
 * Returns `null` if the [file][ReaderApi.file] doesn't exist.
 */
public infix fun CoroutineScope.csvReaderAsync(config: ReaderApi.() -> Unit):
        Deferred<List<Map<String, String>>?> = async(IO) { reader(config) }

/** Scope for reading CSV files. */
public abstract class ReaderApi : CsvApi()
