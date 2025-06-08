<template>
  <div class="p-6 max-w-6xl mx-auto pt-10">
    <div v-if="filme" class="grid grid-cols-1 md:grid-cols-2 gap-8">

      <div class="text-center">
        <h2 class="text-3xl font-bold text-primary mb-4">{{ filme.titulo }}</h2>

        <img v-if="filme.imagemUrl" :src="filme.imagemUrl" class="mx-auto rounded-xl max-h-72 object-cover mb-4" />

        <div class="flex flex-col items-center mb-4">
          <p class="text-sm text-primary mb-1">Nota: {{ formatarNota(filme.mediaNotas) }}/5</p>
        </div>

        <p class="text-base text-gray-300 mb-6">{{ filme.descricao }}</p>

        <div class="flex justify-center mb-6">
          <div class="flex space-x-1">
            <template v-for="n in 5" :key="n">
              <svg v-if="filme.notaUsuario !== null" :class="[
                'h-10 w-10',
                filme.notaUsuario >= n ? 'fill-primary' : 'fill-gray-300'
              ]" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20">
                <path
                  d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.286 3.946a1 1 0 00.95.69h4.15c.969 0 1.371 1.24.588 1.81l-3.36 2.44a1 1 0 00-.364 1.118l1.285 3.946c.3.921-.755 1.688-1.539 1.118l-3.36-2.44a1 1 0 00-1.175 0l-3.36 2.44c-.783.57-1.838-.197-1.539-1.118l1.286-3.946a1 1 0 00-.364-1.118l-3.36-2.44c-.783-.57-.38-1.81.588-1.81h4.15a1 1 0 00.95-.69l1.286-3.946z" />
              </svg>
              <svg v-else @click="nota = n" @mouseover="hoverNota = n" @mouseleave="hoverNota = 0" :class="[
                (hoverNota >= n || nota >= n) ? 'fill-primary' : 'fill-gray-300',
                'h-10 w-10 cursor-pointer transition-transform duration-200 hover:scale-125'
              ]" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20">
                <path
                  d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.286 3.946a1 1 0 00.95.69h4.15c.969 0 1.371 1.24.588 1.81l-3.36 2.44a1 1 0 00-.364 1.118l1.285 3.946c.3.921-.755 1.688-1.539 1.118l-3.36-2.44a1 1 0 00-1.175 0l-3.36 2.44c-.783.57-1.838-.197-1.539-1.118l1.286-3.946a1 1 0 00-.364-1.118l-3.36-2.44c-.783-.57-.38-1.81.588-1.81h4.15a1 1 0 00.95-.69l1.286-3.946z" />
              </svg>
            </template>
          </div>
        </div>

        <div class="mb-6">
          <textarea v-if="filme.notaUsuario === null" v-model="comentario"
            class="w-full p-4 rounded-lg border border-gray-300 focus:outline-none focus:ring-2 focus:ring-primary text-black"
            rows="4" placeholder="Deixe um comentário sobre o filme..."></textarea>

          <p v-else class="text-gray-300">
            {{ comentario || 'Sem comentário.' }}
          </p>
        </div>

        <div v-if="filme.notaUsuario === null" class="flex justify-center">
          <button @click="avaliar" :disabled="isLoading || !metamaskDisponivel || nota === 0" class="bg-primary text-white px-6 py-2 rounded font-semibold transition hover:bg-orange-500 
             disabled:bg-gray-400 disabled:cursor-not-allowed disabled:hover:bg-gray-400">
            {{ !metamaskDisponivel ? 'MetaMask não encontrada' : (isLoading ? 'Enviando...' : 'Enviar Avaliação') }}
          </button>
        </div>

        <div v-if="txHash || filme?.txHash" class="mt-6 text-center">
          <a :href="`https://sepolia.etherscan.io/tx/${txHash || filme.txHash}`" target="_blank"
            class="inline-block mt-1 text-primary hover:text-orange-500 font-semibold underline transition-all">
            Ver sua transação no Etherscan
          </a>
        </div>

      </div>

      <div class="bg-secondary p-6 rounded-2xl shadow flex flex-col gap-6">
        <h2 class="text-2xl font-bold text-primary mb-4 text-center">Avaliações</h2>

        <div v-for="avaliacao in avaliacoes" :key="avaliacao.txHash"
          class="p-4 border border-gray-700 rounded-lg bg-secondary">
          <p class="text-lg font-semibold text-primary mb-2">{{ avaliacao.nomeUsuario }}</p>

          <div class="flex space-x-1 mb-2">
            <template v-for="n in 5" :key="n">
              <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" :class="[
                'h-5 w-5',
                avaliacao.nota >= n ? 'fill-primary' : 'fill-gray-300'
              ]">
                <path
                  d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.286 3.946a1 1 0 00.95.69h4.15c.969 0 1.371 1.24.588 1.81l-3.36 2.44a1 1 0 00-.364 1.118l1.285 3.946c.3.921-.755 1.688-1.539 1.118l-3.36-2.44a1 1 0 00-1.175 0l-3.36 2.44c-.783.57-1.838-.197-1.539-1.118l1.286-3.946a1 1 0 00-.364-1.118l-3.36-2.44c-.783-.57-.38-1.81.588-1.81h4.15a1 1 0 00.95-.69l1.286-3.946z" />
              </svg>
            </template>
          </div>

          <p v-if="avaliacao.comentario" class="text-gray-300 text-sm mb-2 italic">
            "{{ avaliacao.comentario }}"
          </p>

          <p class="text-gray-400 text-xs mb-2">
            {{ formatarDataHora(avaliacao.dataCriacao) }}
          </p>

          <a :href="`https://sepolia.etherscan.io/tx/${avaliacao.txHash}`" target="_blank"
            class="text-primary hover:text-orange-500 font-semibold underline text-sm transition-all">
            Ver transação
          </a>
        </div>

      </div>

    </div>

    <div v-else class="text-center text-gray-400">Carregando filme...</div>
  </div>
</template>


<script setup lang="ts">
import abi from '@/abi/AvaliacaoFilme.json'
import { AVALIACAO_CONTRACT_ADDRESS } from '@/constants/contract'
import api from '@/services/api'
import { ethers } from 'ethers'
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'

interface EthereumWindow extends Window {
  ethereum?: any
}

declare const window: EthereumWindow

const route = useRoute()
const filmeId = Number(route.params.id)

const filme = ref<any | null>(null)
const nota = ref<number>(0)
const comentario = ref<string>('')
const txHash = ref<string | null>(null)
const isLoading = ref(false)
const error = ref<string | null>(null)
const metamaskDisponivel = ref(true)
const hoverNota = ref<number>(0)
const success = ref<string | null>(null)
const avaliacoes = ref<AvaliacaoResponseDTO[]>([])


function formatarDataHora(data: string) {
  const d = new Date(data)
  return d.toLocaleDateString('pt-BR') + ' ' + d.toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' })
}


async function fetchFilme() {
  try {
    const response = await api.get(`/filmes/${filmeId}`)
    filme.value = response.data

    if (filme.value?.notaUsuario) {
      nota.value = filme.value.notaUsuario
      comentario.value = filme.value.comentario || ''
      txHash.value = filme.value.txHash || null
    }

    const avaliacoesResponse = await api.get(`/avaliacoes/${filmeId}`)
    avaliacoes.value = avaliacoesResponse.data

  } catch (err) {
    error.value = 'Erro ao carregar filme.'
  }
}

interface AvaliacaoResponseDTO {
  nomeUsuario: string
  nota: number
  comentario: string | null
  txHash: string
  dataCriacao: string
}

async function avaliar() {
  isLoading.value = true
  error.value = null
  txHash.value = null

  try {
    if (!window.ethereum) {
      throw new Error('MetaMask não encontrada.')
    }

    const provider = new ethers.BrowserProvider(window.ethereum)
    const signer = await provider.getSigner()
    const contract = new ethers.Contract(
      AVALIACAO_CONTRACT_ADDRESS,
      abi.abi,
      signer
    )

    const tx = await contract.avaliar(filmeId, nota.value, comentario.value)
    const receipt = await tx.wait()

    txHash.value = receipt.hash

    await api.post('/avaliacoes', {
      idFilme: filmeId,
      nota: nota.value,
      comentario: comentario.value,
      txHash: receipt.hash
    })

    await fetchFilme()
    success.value = 'Avaliação registrada com sucesso!'
  } catch (err: any) {
    console.error(err)

    if (err.message?.includes('MetaMask')) {
      error.value = 'MetaMask não encontrada. Instale a extensão para continuar.'
    } else if (err.message?.includes('user rejected')) {
      error.value = 'Transação recusada pelo usuário.'
    } else if (err.message?.includes('insufficient funds')) {
      error.value = 'Saldo insuficiente para realizar a avaliação.'
    } else if (err.message?.includes('execution reverted')) {
      error.value = 'Não foi possível realizar a avaliação. Verifique se já avaliou esse filme.'
    } else {
      error.value = 'Erro inesperado ao enviar a avaliação.'
    }
  } finally {
    isLoading.value = false
  }
}


function formatarNota(nota: number | null) {
  if (nota === null) return '0'
  return Number.isInteger(nota) ? nota.toString() : nota.toFixed(1)
}

function getStarType(index: number, media: number) {
  if (media >= index) {
    return 'fill-primary'
  } else if (media >= index - 0.5) {
    return 'fill-primary opacity-50'
  } else {
    return 'fill-gray-300'
  }
}

onMounted(() => {
  fetchFilme()
  metamaskDisponivel.value = !!window.ethereum
})
</script>
