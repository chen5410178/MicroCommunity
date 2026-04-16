<template>
  <div class="margin-top text-left">
    <div v-if="photos.length > 0" class="row padding">
      <div class="text-title">{{ $t('repairDetail.orderPhotos') }}</div>
      <div v-for="(item, index) in photos" :key="index" class="form-group margin-left">
        <el-image style="width: 120px; height: 120px;" :src="item.url" :preview-src-list="[item.url]" fit="cover"
          @click="openFile(item)"></el-image>
      </div>
    </div>

    <div v-if="beforePhotos.length > 0" class="row padding">
      <div class="text-title">{{ $t('repairDetail.beforeRepairPhotos') }}</div>
      <div v-for="(item, index) in beforePhotos" :key="index" class="form-group margin-left">
        <el-image style="width: 120px; height: 120px;" :src="item.url" :preview-src-list="[item.url]" fit="cover"
          @click="openFile(item)"></el-image>
      </div>
    </div>

    <div v-if="afterPhotos.length > 0" class="row padding">
      <div class="text-title">{{ $t('repairDetail.afterRepairPhotos') }}</div>
      <div v-for="(item, index) in afterPhotos" :key="index" class="form-group margin-left">
        <el-image style="width: 120px; height: 120px;" :src="item.url" :preview-src-list="[item.url]" fit="cover"
          @click="openFile(item)"></el-image>
      </div>
    </div>
  </div>
</template>

<script>
import { getRepairPhotos } from '@/api/work/repairDetailApi'

export default {
  name: 'RepairDetailPhotos',
  props: {
    repairId: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      photos: [],
      beforePhotos: [],
      afterPhotos: []
    }
  },
  methods: {
    async loadData() {
      try {
        console.log(this.repairId)
        const data = await getRepairPhotos(this.repairId)
        this.photos = data.photos
        this.beforePhotos = data.beforePhotos
        this.afterPhotos = data.afterPhotos
      } catch (error) {
        this.$message.error(error.message)
      }
    },
    openFile(item) {
      this.$emit('show-image', item)
    }
  }
}
</script>

<style scoped>
.padding {
  padding: 10px 0;
}

.text-title {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 10px;
}

.margin-left {
  margin-left: 10px;
  display: inline-block;
}
</style>