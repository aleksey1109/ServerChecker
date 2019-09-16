package ru.alexeyfedechkin.android.checkserver.enums

enum class Protocol(val protocol: String,
                    val defaultPort: Int,
                    val position:Int) {
    HTTP("http", 80, 0),
    HTTPS("https", 443,1),
    FTP("ftp", 21,2),
    SFTP("sftp", 22,3);


    companion object{
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