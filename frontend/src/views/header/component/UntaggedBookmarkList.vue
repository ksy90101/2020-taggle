<template>
  <v-list class="grow">
    <v-list-item @click="push">
      <v-list-item-icon class="mx-0">
        <v-icon>turned_in_not</v-icon>
      </v-list-item-icon>
      <v-list-item-content class="font-weight-black text-h7 mx-4">
        Untagged
      </v-list-item-content>
    </v-list-item>
  </v-list>
</template>

<script>
export default {
  methods: {
    push() {
      this.$router.push({ name: 'bookmarks', query: { tag: 'none' } }).catch(() => {});
    },
  },
};
</script>
