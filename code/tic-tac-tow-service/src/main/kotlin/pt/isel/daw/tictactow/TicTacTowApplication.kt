package pt.isel.daw.tictactow

import org.jdbi.v3.core.Jdbi
import org.postgresql.PGProperty
import org.postgresql.ds.PGSimpleDataSource
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import pt.isel.daw.tictactow.http.pipeline.AuthenticationInterceptor
import pt.isel.daw.tictactow.http.pipeline.UserArgumentResolver
import pt.isel.daw.tictactow.repository.jdbi.configure
import pt.isel.daw.tictactow.utils.Sha256TokenEncoder
import java.time.Instant

@SpringBootApplication
class TicTacTowApplication {
    @Bean
    fun jdbi(): Jdbi {
        val postgresUri =
            System.getenv("POSTGRES_URI")
                ?: "jdbc:postgresql://localhost:5432/db?user=dbuser&password=changeit"

        val dataSource = PGSimpleDataSource().apply {
            setURL(postgresUri)
        }

        // Be careful not to disclose the credentials.
        logger.info(
            "Using PostgreSQL located at '{}:{}'",
            dataSource.getProperty(PGProperty.PG_HOST),
            dataSource.getProperty(PGProperty.PG_PORT),
        )

        return Jdbi.create(dataSource).configure()
    }

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

    @Bean
    fun tokenEncoder() = Sha256TokenEncoder()

    @Bean
    fun clock() = object : Clock {
        override fun now() = Instant.now()
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(TicTacTowApplication::class.java)
    }
}

// QUESTION: why cannot this be in TicTacTowApplication
@Configuration
class PipelineConfigurer(
    val authenticationInterceptor: AuthenticationInterceptor,
    val userArgumentResolver: UserArgumentResolver,
) : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(authenticationInterceptor)
    }

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(userArgumentResolver)
    }
}

fun main(args: Array<String>) {
    runApplication<TicTacTowApplication>(*args)
}