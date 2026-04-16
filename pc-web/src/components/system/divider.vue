<template>
       <div class="gradient-line-container">
    <div class="gradient-line"></div>
  </div>
</template>
<style lang="scss" scoped>  
.gradient-line-container {
  margin: 20px 0;
  display: flex;
  justify-content: center;
}

.gradient-line {
  height: 2px;
  width: 100%;
  background: linear-gradient(
    90deg,
    rgba(64, 158, 255, 0) 0%,
    rgba(64, 158, 255, 0.3) 20%,
    rgba(64, 158, 255, 1) 50%,
    rgba(64, 158, 255, 0.3) 80%,
    rgba(64, 158, 255, 0) 100%
  );
}
</style>