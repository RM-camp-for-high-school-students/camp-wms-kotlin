import java.sql.*

fun testDatabaseConnection(inputDatabaseUrl:String, inputDatabaseUserName:String, inputDatabasePassword:String): Boolean{
    Class.forName("com.mysql.cj.jdbc.Driver")
    return try {
        val connection = DriverManager.getConnection(inputDatabaseUrl, inputDatabaseUserName, inputDatabasePassword)
        connection.close()
        true
    } catch (e:SQLException) {
        false
    }
}