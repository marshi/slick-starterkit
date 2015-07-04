object CodeGen {

  def main(args: Array[String]) {
    slick.codegen.SourceCodeGenerator.main(
      Array(
        "slick.driver.PostgresDriver",
        "org.postgresql.Driver",
        "jdbc:postgresql://localhost/postgres",
        "src/main/scala",
        "entity",
        "postgres",
        "postgres")
    )
  }

}
