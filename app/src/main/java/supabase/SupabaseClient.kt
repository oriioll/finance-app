package supabase

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth // Import correcto
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.serializer.KotlinXSerializer
import kotlinx.serialization.json.Json

val SUPABASE = createSupabaseClient(
    supabaseUrl = "https://fjmlgcqyrsbnoqetksvp.supabase.co",
    supabaseKey = "sb_publishable_B_zxo1kB7G2Jak2VFGk72g_Uk0lq2Gw"
) {
    // 1. Instalar Auth
    install(Auth)

    // 2. Instalar Base de datos
    install(Postgrest)

    // 3. OBLIGATORIO: Configurar el serializador para que Kotlin entienda el JSON
    defaultSerializer = KotlinXSerializer(Json {
        ignoreUnknownKeys = true // Evita crash si Supabase envía campos extra
    })
}