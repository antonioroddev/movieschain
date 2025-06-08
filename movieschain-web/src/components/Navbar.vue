<script setup lang="ts">
import { useAuthStore } from '@/stores/auth'
import { computed } from 'vue'
import { useRouter } from 'vue-router'

const auth = useAuthStore()
const router = useRouter()

const logout = () => {
    auth.logout()
    router.push('/login')
}

const firstName = computed(() => {
    const parts = auth.userName.split(' ')
    return parts.length > 0 ? parts[0] : auth.userName
})
</script>


<template>
    <nav class="fixed top-0 left-0 w-full z-50 bg-secondary border-b border-primary shadow-sm">
        <div class="max-w-7xl mx-auto px-6 py-4 flex justify-between items-center">
            <router-link to="/" class="flex items-center space-x-3">
                <img src="../assets/image/movie.png" alt="MoviesChain" class="h-8 w-8" />
                <span class="text-white text-xl font-bold">MoviesChain</span>
            </router-link>

            <div class="flex items-center space-x-6 text-white text-base">
                <template v-if="!auth.isLoggedIn">
                    <router-link to="/login" class="hover:text-primary transition">Login</router-link>
                    <router-link to="/registro" class="hover:text-primary transition">Registro</router-link>
                </template>

                <template v-else>
                    <span>Olá, {{ firstName }}</span>
                    <button @click="logout"
                        class="bg-primary hover:bg-orange-500 text-white py-1 px-4 rounded transition font-semibold">
                        Sair
                    </button>
                </template>
            </div>
        </div>
    </nav>
</template>
