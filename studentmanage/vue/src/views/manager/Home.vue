<template>
  <div>

    <div class="card" style="line-height: 30px">
      <div>欢迎您，<span style="color:red;">{{ user.name }}</span> <span style="margin-right: 15px">,祝你天天开心</span></div>
    </div>

  </div>
</template>

<script setup>
  import request from "@/utils/request";
  import {reactive} from "vue";

  const user = JSON.parse(localStorage.getItem('student-user') || '{}')
</script>