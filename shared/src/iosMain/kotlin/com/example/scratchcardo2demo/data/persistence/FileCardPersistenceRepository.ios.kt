package com.example.scratchcardo2demo.data.persistence

import com.example.scratchcardo2demo.data.persistence.mapper.toDomain
import com.example.scratchcardo2demo.data.persistence.mapper.toDto
import com.example.scratchcardo2demo.data.persistence.model.ScratchCardDto
import com.example.scratchcardo2demo.domain.ScratchCard
import com.example.scratchcardo2demo.domain.`interface`.ScratchCardPersistenceRepository
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json
import platform.Foundation.NSApplicationSupportDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSString
import platform.Foundation.NSURL
import platform.Foundation.NSURLIsExcludedFromBackupKey
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.NSUserDomainMask
import platform.Foundation.stringWithContentsOfFile
import platform.Foundation.writeToURL

@OptIn(ExperimentalForeignApi::class)
class FileScratchCardPersistenceRepository(private val json: Json) :
    ScratchCardPersistenceRepository {

    private val key = "scratch_card_data"
    private val preferencesUrl: NSURL
    private val mutex = Mutex()

    init {
        val fileManager = NSFileManager.defaultManager

        val appSupport = requireNotNull(
            fileManager.URLsForDirectory(
                NSApplicationSupportDirectory,
                NSUserDomainMask,
            ).firstOrNull() as? NSURL
        ) {
            "Failed to get the App support directory."
        }

        val directory =
            appSupport.URLByAppendingPathComponent(
                "datastore",
                isDirectory = true,
            )

        check(directory != null && directory.path != null) {
            "Failed to create datastore directory."
        }

        val isCreated = fileManager.createDirectoryAtURL(
            url = directory,
            withIntermediateDirectories = true,
            attributes = null,
            error = null,
        )

        check(isCreated || fileManager.fileExistsAtPath(directory.path!!)) {
            "Failed to create datastore directory."
        }

        directory.setResourceValue(
            value = true,
            forKey = NSURLIsExcludedFromBackupKey,
            error = null,
        )

        preferencesUrl = requireNotNull(
            directory.URLByAppendingPathComponent(
                "app_preferences",
                isDirectory = false,
            )
        ) {
            "Failed to create App preferences."
        }
    }

    override suspend fun get(): ScratchCard? = mutex.withLock {
        val cardData = readAll()[key] ?: return@withLock null
        json.decodeFromString<ScratchCardDto>(cardData).toDomain()
    }

    override suspend fun update(card: ScratchCard) = mutex.withLock {
        val content = readAll()
        content[key] = json.encodeToString(card.toDto())
        writeAll(content)
    }

    private fun readAll(): MutableMap<String, String> {
        val path = requireNotNull(preferencesUrl.path)

        if (!NSFileManager.defaultManager.fileExistsAtPath(path)) {
            return mutableMapOf()
        }

        val content = NSString.stringWithContentsOfFile(
            path,
            encoding = NSUTF8StringEncoding,
            error = null,
        ) ?: error("Failed to read from App preferences.")
        return json.decodeFromString<MutableMap<String, String>>(content)
    }

    private fun writeAll(values: Map<String, String>) {
        val content = json.encodeToString(values)
        require(
            (content as NSString).writeToURL(
                url = preferencesUrl,
                atomically = true,
                encoding = NSUTF8StringEncoding,
                error = null,
            )
        ) {
            "Failed to write to App references."
        }
    }
}