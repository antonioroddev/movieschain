<template>
  <div class="min-h-screen flex items-center justify-center bg-secondary">
    <div class="bg-white p-8 rounded-2xl shadow-xl w-full max-w-md text-center">
      <h1 class="text-3xl font-bold text-primary mb-6">Registro</h1>

      <div class="mb-4 text-left">
        <label for="nome" class="block text-sm font-medium text-gray-700 mb-1">Nome *</label>
        <input v-model="nome" id="nome" type="text" placeholder="Digite seu nome"
          class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary" />
      </div>

      <div class="mb-4 text-left">
        <label for="cpf" class="block text-sm font-medium text-gray-700 mb-1">CPF *</label>
        <input v-model="cpf" id="cpf" type="text" maxlength="14" placeholder="Digite seu CPF"
          @input="cpf = formatCpf(cpf)"
          class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary" />
      </div>

      <div class="mb-6 text-left">
        <label for="senha" class="block text-sm font-medium text-gray-700 mb-1">Senha *</label>
        <input v-model="senha" id="senha" type="password" placeholder="Digite sua senha"
          class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary" />
      </div>

      <button @click="registrarComWallet"
        class="w-full bg-primary text-white py-2 rounded-lg font-semibold hover:bg-orange-500 transition">
        Registrar com Wallet
      </button>

      <p v-if="erro" class="mt-4 text-center text-red-600 text-sm">{{ erro }}</p>

      <p class="mt-6 text-center text-sm text-gray-600">
        Já tem conta?
        <router-link to="/login" class="text-primary font-medium hover:underline">
          Fazer login
        </router-link>
      </p>
    </div>
  </div>
</template>

<script setup lang="ts">
import api from '@/services/api'
import { useAuthStore } from '@/stores/auth'
import { ethers } from 'ethers'
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'

const nome = ref('')
const cpf = ref('')
const senha = ref('')
const erro = ref('')
const auth = useAuthStore()
const router = useRouter()

interface EthereumWindow extends Window {
  ethereum?: any;
}

const windowTyped = window as EthereumWindow;

const formatCpf = (value: string) => {
  return value
    .replace(/\D/g, '')
    .replace(/(\d{3})(\d)/, '$1.$2')
    .replace(/(\d{3})(\d)/, '$1.$2')
    .replace(/(\d{3})(\d{1,2})$/, '$1-$2')
}

async function registrarComWallet() {
  try {
    erro.value = ''

    if (!nome.value || !cpf.value || !senha.value) {
      erro.value = 'Todos os campos são obrigatórios.'
      return
    }

    if (!windowTyped.ethereum) {
      erro.value = 'MetaMask não encontrada.'
      return
    }

    const provider = new ethers.BrowserProvider(windowTyped.ethereum)
    const accounts = await provider.send('eth_requestAccounts', [])
    const walletAddress = accounts[0]

    const response = await api.post('/auth/register-wallet', {
      nome: nome.value,
      cpf: cpf.value.replace(/\D/g, ''),
      senha: senha.value,
      wallet: walletAddress
    })

    auth.login(response.data.token, response.data.user || 'Usuário')

    router.push('/')
  } catch (err) {
    console.error(err)
    erro.value = 'Erro ao registrar. Verifique se CPF ou Wallet já estão em uso.'
  }
}

onMounted(() => {
  document.title = 'MoviesChain - Registro'
})
</script>

<script lang="ts">
export default {
  name: 'RegisterPage'
}
</script>
