package com.example.lociapp.ai

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import com.example.lociapp.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL

private const val PROMPT =
    "Describe the main object in this photo in one short sentence (max 15 words) " +
            "for an item-tracker app. Mention where it is if that is visible. " +
            "Reply with only the description."

/**
 * Sends the photo to Claude and returns a short description.
 * Call it from a coroutine (see EditPhotoScreen).
 */
suspend fun generateDescription(context: Context, photoUri: Uri): Result<String> =
    withContext(Dispatchers.IO) {
        runCatching {
            val apiKey = BuildConfig.ANTHROPIC_API_KEY
            require(apiKey.isNotBlank()) { "Missing API key. Add ANTHROPIC_API_KEY to local.properties." }

            // Build the request: 1 image + 1 text prompt
            val imageBlock = JSONObject()
                .put("type", "image")
                .put(
                    "source", JSONObject()
                        .put("type", "base64")
                        .put("media_type", "image/jpeg")
                        .put("data", photoToBase64(context, photoUri))
                )
            val textBlock = JSONObject().put("type", "text").put("text", PROMPT)

            val body = JSONObject()
                .put("model", "claude-haiku-4-5-20251001")
                .put("max_tokens", 150)
                .put(
                    "messages", JSONArray().put(
                        JSONObject()
                            .put("role", "user")
                            .put("content", JSONArray().put(imageBlock).put(textBlock))
                    )
                )

            val connection = (URL("https://api.anthropic.com/v1/messages").openConnection() as HttpURLConnection).apply {
                requestMethod = "POST"
                connectTimeout = 15_000
                readTimeout = 30_000
                doOutput = true
                setRequestProperty("content-type", "application/json")
                setRequestProperty("x-api-key", apiKey)
                setRequestProperty("anthropic-version", "2023-06-01")
            }
            connection.outputStream.use { it.write(body.toString().toByteArray()) }

            val code = connection.responseCode
            val stream = if (code in 200..299) connection.inputStream else connection.errorStream
            val response = stream.bufferedReader().use { it.readText() }
            if (code !in 200..299) error("AI request failed ($code)")

            JSONObject(response)
                .getJSONArray("content")
                .getJSONObject(0)
                .getString("text")
                .trim()
        }
    }

// Shrinks the photo (big photos are slow to upload) and converts it to Base64 text.
private fun photoToBase64(context: Context, uri: Uri): String {
    val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
    context.contentResolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it, null, bounds) }

    var sample = 1
    while (bounds.outWidth / sample > 1600 || bounds.outHeight / sample > 1600) sample *= 2

    val bitmap = context.contentResolver.openInputStream(uri)?.use {
        BitmapFactory.decodeStream(it, null, BitmapFactory.Options().apply { inSampleSize = sample })
    } ?: error("Could not read the photo")

    val out = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out)
    return Base64.encodeToString(out.toByteArray(), Base64.NO_WRAP)
}
