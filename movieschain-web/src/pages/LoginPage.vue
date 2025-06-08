<template>
  <div class="min-h-screen flex items-center justify-center bg-secondary">
    <div class="bg-white p-8 rounded-2xl shadow-xl w-full max-w-md text-center">
      <h1 class="text-3xl font-bold text-primary mb-6">Login com Carteira</h1>

      <button @click="loginComWallet"
        class="w-full bg-primary text-white py-3 rounded-lg font-semibold hover:bg-orange-500 transition mb-4">
        Conectar Carteira
      </button>

      <p v-if="erro" class="mt-4 text-center text-red-600 text-sm">{{ erro }}</p>

      <p class="mt-6 text-center text-sm text-gray-600">
        Ainda não tem conta?
        <router-link to="/registro" class="text-primary font-medium hover:underline">
          Registre-se
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

const erro = ref('')
const auth = useAuthStore()
const router = useRouter()

interface EthereumWindow extends Window {
  ethereum?: any;
}

const windowTyped = window as EthereumWindow;

async function loginComWallet() {
  try {
    erro.value = ''

    if (!windowTyped.ethereum) {
      erro.value = 'MetaMask não encontrada.'
      return
    }

    const provider = new ethers.BrowserProvider(windowTyped.ethereum)
    const accounts = await provider.send('eth_requestAccounts', [])
    const address = accounts[0]

    const message = `Login no MoviesChain - ${new Date().toISOString()}`

    const signer = await provider.getSigner()
    const signature = await signer.signMessage(message)

    const response = await api.post('/auth/wallet-login', {
      wallet: address,
      signedMessage: signature,
      originalMessage: message
    })

    auth.login(response.data.token, response.data.user || 'Usuário')

    router.push('/')
  } catch (err) {
    console.error(err)
    erro.value = 'Erro ao conectar a carteira.'
  }
}


onMounted(() => {
  document.title = 'MoviesChain - Login'
})
</script>

<script lang="ts">
export default {
  name: 'LoginPage',
}
</script>
