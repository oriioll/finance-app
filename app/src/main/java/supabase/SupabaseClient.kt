package supabase

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest

val SUPABASE = createSupabaseClient(
    supabaseUrl = "https://fjmlgcqyrsbnoqetksvp.supabase.co",
    supabaseKey = "sb_publishable_B_zxo1kB7G2Jak2VFGk72g_Uk0lq2Gw"
) {
    install(Auth)
    install(Postgrest)
}