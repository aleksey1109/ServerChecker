package ru.alexeyfedechkin.android.checkserver.network

/**
 *
 */
object Http {

    /**
     * list of all default http response code
     */
    private val responsesCodes = listOf(
        //Informational
        100,
        101,
        102,
        //success
        200,
        201,
        202,
        203,
        204,
        205,
        206,
        207,
        208,
        226,
        //redirection
        300,
        301,
        302,
        303,
        304,
        305,
        306,
        307,
        308,
        //client error
        401,
        402,
        403,
        404,
        405,
        406,
        407,
        408,
        409,
        410,
        411,
        412,
        413,
        414,
        415,
        416,
        417,
        418,
        419,
        421,
        422,
        423,
        424,
        426,
        428,
        429,
        431,
        449,
        451,
        452,
        499,
        //server error
        500,
        501,
        502,
        503,
        504,
        505,
        506,
        507,
        508,
        509,
        510,
        511,
        520,
        521,
        522,
        523,
        524,
        525,
        526)

    private const val MAX_PORT_NUMBER = 65535

    /**
     * validate http code
     * @param responseCode response code from user input
     * @return true if this number is a http response code
     */
    fun validateResponseCode(responseCode:Int):Boolean{
        return responsesCodes.contains(responseCode)
    }

    /**
     * validate port
     * @param port port from user input
     * @return true if port is valid
     */
    fun validatePort(port:Int):Boolean{
        return port in 0..MAX_PORT_NUMBER
    }
}