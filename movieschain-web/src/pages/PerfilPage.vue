<template>
  <div class="p-6 max-w-4xl mx-auto">
    <h1 class="text-2xl font-bold mb-4">Meu Perfil</h1>

    <div v-if="usuario" class="mb-8 bg-white p-4 shadow rounded">
      <p><strong>CPF:</strong> {{ usuario.cpf }}</p>
      <p><strong>Wallet:</strong> {{ usuario.wallet || 'Não vinculada' }}</p>
    </div>

    <h2 class="text-xl font-semibold mb-2">Minhas Avaliações</h2>
    <div v-if="avaliacoes.length" class="space-y-4">
      <div
        v-for="avaliacao in avaliacoes"
        :key="avaliacao.id"
        class="bg-white p-4 shadow rounded"
      >
        <p><strong>Filme:</strong> {{ avaliacao.tituloFilme }}</p>
        <p><strong>Nota:</strong> {{ avaliacao.nota }}</p>
        <p><strong>Comentário:</strong> {{ avaliacao.comentario }}</p>
        <p v-if="avaliacao.txHash"><strong>Tx Hash:</strong> {{ avaliacao.txHash }}</p>
      </div>
    </div>
    <p v-else class="text-gray-500">Você ainda não avaliou nenhum filme.</p>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import api from '@/services/api'

const usuario = ref<{ cpf: string; wallet: string } | null>(null)
const avaliacoes = ref<
  {
    id: number
    tituloFilme: string
    nota: number
    comentario: string
    txHash: string | null
  }[]
>([])

onMounted(async () => {
  try {
    document.title = 'MoviesChain - Perfil'
    const userRes = await api.get('/auth/me')
    usuario.value = userRes.data

    const avaliacoesRes = await api.get('/avaliacoes/minhas')
    avaliacoes.value = avaliacoesRes.data
  } catch (err) {
    console.error('Erro ao carregar perfil ou avaliações', err)
  }
})
</script>
