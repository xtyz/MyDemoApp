package cz.pochoto.mydemoapp.data.repository

import com.squareup.moshi.JsonDataException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException
import javax.net.ssl.SSLException

abstract class BaseRepository {

    suspend fun <T> apiRequest(suspendBlock: suspend () -> T) = withContext(Dispatchers.IO) {
        try {
            suspendBlock.invoke()
        } catch (e: Throwable) {
            when (e) {
                is CancellationException -> throw e
                is UnknownHostException,
                is TimeoutException,
                is SocketTimeoutException,
                is SocketException,
                is SSLException -> throw Error.NetworkException(e)
                is HttpException -> throw Error.NetworkException(e) // TODO HttpErrorResponse.createHttpException(e) for parsing custom errors from server
                is JsonDataException -> throw Error.ModelParseException(e)
                else -> throw Error.UnknownNetworkException(e)
            }
        }
    }
}