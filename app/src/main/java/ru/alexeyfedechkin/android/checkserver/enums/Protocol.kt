package ru.alexeyfedechkin.android.checkserver.enums

enum class Protocol(val protocol: String,
                    val defaultPort: Int,
                    val position:Int) {

    HTTP("http", 80, 0),
    HTTPS("https", 443,1),
    FTP("ftp", 21,2),
    SFTP("sftp", 22,3),
    TELNET("telnet",23, 4 ),
    SSH("ssh", 22,5);

    companion object{
        /**
         * check port for default
         * @param port port from user input
         * @return true if port known as default port for one of support protocol
         */
        fun isDefaultPort(port:Int):Protocol?{
            for (protocol in Protocol.values()){
                if (protocol.defaultPort == port){
                    return protocol
                }
            }
            return null
        }
    }
}