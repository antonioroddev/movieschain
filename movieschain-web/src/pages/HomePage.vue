<template>
  <div class="p-6 bg-secondary min-h-screen pt-10">
    <h1 class="text-4xl font-bold text-primary mb-5 text-center">Filmes</h1>

    <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
      <MovieCard v-for="filme in filmes" :key="filme.id" :filme="filme" @avaliar="irParaAvaliacao" />
    </div>
  </div>
</template>

<script setup lang="ts">
import MovieCard from '@/components/MovieCard.vue'
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import api from '../services/api'

interface Filme {
  id: number
  titulo: string
  descricao: string
  imagemUrl: string
  notaUsuario: number | null
  mediaNotas: number | null
}

const router = useRouter()
const filmes = ref<Filme[]>([])

const irParaAvaliacao = (id: number) => {
  router.push(`/avaliar/${id}`)
}

onMounted(async () => {
  try {
    document.title = 'MoviesChain - Home'
    const response = await api.get('/filmes')
    filmes.value = response.data
  } catch (err) {
    console.error('Erro ao carregar filmes:', err)
  }
})
</script>
