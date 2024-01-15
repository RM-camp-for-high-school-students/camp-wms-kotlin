import java.sql.*

fun testDatabaseConnection(inputDatabaseUrl:String, inputDatabaseUserName:String, inputDatabasePassword:String, inputCurrentDatabase:String): Boolean{
    Class.forName("com.mysql.cj.jdbc.Driver")
    return try {
        val connection = DriverManager.getConnection(inputDatabaseUrl, inputDatabaseUserName, inputDatabasePassword)
        val statement = connection.createStatement()
        statement.execute("use $inputCurrentDatabase")
        connection.close()
        true
    } catch (e:SQLException) {
        false
    }
}