import java.sql.*

fun testDatabaseConnection(inputDatabaseUrl:String, inputDatabaseUserName:String, inputDatabasePassword:String): Boolean{
    Class.forName("com.mysql.cj.jdbc.Driver")
    return try {
        val conn = DriverManager.getConnection(inputDatabaseUrl, inputDatabaseUserName, inputDatabasePassword)
        conn.close()
        true
    } catch (e:SQLException) {
        false
    }
}