<template>
  <div class="flex flex-col min-h-screen bg-secondary text-white">
    <Navbar />

    <main class="flex-grow px-4 py-8">
      <router-view />
    </main>

    <Footer />
  </div>
</template>

<script setup lang="ts">
import Footer from '@/components/Footer.vue';
import Navbar from '@/components/Navbar.vue';
</script>

<style scoped></style>