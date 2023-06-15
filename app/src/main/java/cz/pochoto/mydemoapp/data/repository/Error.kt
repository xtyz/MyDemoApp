package cz.pochoto.mydemoapp.data.repository

sealed class Error(e: Throwable) : Throwable(e){

    class NetworkException(e: Throwable): Error(e)

    class ModelParseException(e: Throwable): Error(e)

    class HttpException(e: Throwable): Error(e)

    class UnknownNetworkException(e: Throwable): Error(e)

}
