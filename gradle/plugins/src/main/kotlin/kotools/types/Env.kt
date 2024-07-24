package kotools.types

internal object Env {
    internal val gitEmail: String? by lazy { env("GIT_EMAIL") }
    internal val gitUser: String? by lazy { env("GIT_USER") }
    internal val gpgPassword: String? by lazy { env("GPG_PASSWORD") }
    internal val gpgPrivateKey: String? by lazy { env("GPG_PRIVATE_KEY") }
    internal val mavenPassword: String? by lazy { env("MAVEN_PASSWORD") }
    internal val mavenUsername: String? by lazy { env("MAVEN_USERNAME") }

    internal fun sonatypeRepositoryIdentifierOrNull(): String? =
        this.env("SONATYPE_REPOSITORY_IDENTIFIER")

    private fun env(name: String): String? = System.getenv(name)
}
