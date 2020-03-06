<template>
  <v-card>
    <v-flex sm10 xs12>
      <v-tree :isEdit="isEdit"
              @handleAdd="handleAdd"
              @handleClick="handleClick"
              @handleDelete="handleDelete"
              @handleEdit="handleEdit"
              url="/item/category/list"
      />
    </v-flex>
  </v-card>
</template>

<script>
  import {treeData} from "../../mockDB";

  export default {
    name: "category",
    data() {
      return {
        isEdit: true,
        treeData
      }
    },
    methods: {
      handleAdd(node) {
        console.log("add .... ");
        console.log(node);
      },
      handleEdit(id, name) {
        console.log("edit... id: " + id + ", name: " + name)
      },
      handleDelete(id) {
        console.log("delete ... " + id)
      },
      handleClick(node) {
        console.log(node)
      }
    }
  };
</script>

<style scoped>

</style>
