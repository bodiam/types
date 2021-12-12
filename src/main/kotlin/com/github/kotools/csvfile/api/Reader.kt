package com.github.kotools.csvfile.api

import com.github.kotools.csvfile.core.reader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

/**
 * Creates a [reader][Reader] with the given [config] and reads the
 * corresponding [file][Csv.file]'s content.
 *
 * Returns `null` if the [config] is invalid or if something goes wrong in the
 * process.
 */
public suspend fun csvReader(config: Reader.() -> Unit):
        List<Map<String, String>>? = withContext(IO) { reader(config) }

/**
 * Creates a [reader][Reader] with the given [config] and reads the
 * corresponding [file][Csv.file]'s content **asynchronously**.
 *
 * Returns `null` if the [config] is invalid or if something goes wrong in the
 * process.
 */
public infix fun CoroutineScope.csvReaderAsync(config: Reader.() -> Unit):
        Deferred<List<Map<String, String>>?> = async(IO) { reader(config) }

/** Scope for reading CSV files. */
public interface Reader : Csv
