<script setup>

import {onMounted, ref} from "vue";

const msg = ref('d')

onMounted(()=> {
  fetch("/api/hello")
      .then((response) => response.text())
      .then((data) => {
        msg.value = data;
      });})
</script>

<template>
  <button class="btn btn-danger"></button>
  <main>
    {{ msg }}
  </main>
</template>
