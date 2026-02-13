// Top-level build file
plugins {
    // AÑADIMOS version "8.3.1" (o la que use tu Android Studio, 8.x.x suele ir bien)
    id("com.android.application") version "8.3.1" apply false

    // AÑADIMOS version "1.9.23" (Versión estable de Kotlin)
    id("org.jetbrains.kotlin.android") version "1.9.23" apply false

    // IMPORTANTE PARA SUPABASE: Plugin de serialización (debe coincidir con la versión de Kotlin)
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.23" apply false
}