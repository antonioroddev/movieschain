<template>
  <div
    class="shadow rounded-2xl p-6 flex flex-col h-full items-center text-center transform transition-transform duration-300 hover:scale-105 hover:shadow-2xl bg-secondary">

    <div class="flex flex-col items-center mb-4">
      <p class="text-sm text-primary mb-1">
        Usuários: {{ formatarNota(filme.mediaNotas) }}/5
      </p>
    </div>

    <img :src="filme.imagemUrl" :alt="filme.titulo" class="w-32 h-48 object-contain mb-4 rounded-lg" />

    <h2 class="text-lg font-semibold mb-2 text-primary">{{ filme.titulo }}</h2>

    <!-- Descrição -->
    <p class="text-sm text-white-600 flex-grow mb-4">{{ filme.descricao }}</p>

    <div class="flex space-x-1 mb-4">
      <template v-for="n in 5" :key="`user-${n}`">
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" :class="[
          'h-5 w-5',
          (filme.notaUsuario !== null && filme.notaUsuario >= n) ? 'fill-primary' : 'fill-gray-300'
        ]">
          <path
            d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.286 3.946a1 1 0 00.95.69h4.15c.969 0 1.371 1.24.588 1.81l-3.36 2.44a1 1 0 00-.364 1.118l1.285 3.946c.3.921-.755 1.688-1.539 1.118l-3.36-2.44a1 1 0 00-1.175 0l-3.36 2.44c-.783.57-1.838-.197-1.539-1.118l1.286-3.946a1 1 0 00-.364-1.118l-3.36-2.44c-.783-.57-.38-1.81.588-1.81h4.15a1 1 0 00.95-.69l1.286-3.946z" />
        </svg>
      </template>
    </div>


    <!-- Botão -->
    <button @click="$emit('avaliar', filme.id)"
      class="mt-auto bg-primary text-white px-6 py-2 rounded-lg hover:bg-orange-500 hover:scale-105 transition-all font-semibold">
      {{ filme.notaUsuario !== null ? 'Visualizar' : 'Avaliar' }}
    </button>
  </div>
</template>

<script setup lang="ts">
const props = defineProps<{
  filme: {
    id: number
    titulo: string
    descricao: string
    imagemUrl: string
    notaUsuario: number | null
    mediaNotas: number | null
  }
}>()

function getStarType(index: number, media: number) {
  if (media >= index) {
    return 'fill-primary'
  } else if (media >= index - 0.5) {
    return 'fill-primary opacity-50'
  } else {
    return 'fill-gray-300'
  }
}

function formatarNota(nota: number | null) {
  if (nota === null) return '0'
  return Number.isInteger(nota) ? nota.toString() : nota.toFixed(1)
}

</script>
