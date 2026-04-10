<template>
  <el-select
    v-model="value"
    filterable
    remote
    :remote-method="remoteMethod"
    :loading="loading"
    :placeholder="$t('parkingAreaSelect2.parkingLotPlaceholder')"
    style="width:100%"
    @change="handleChange"
  >
    <el-option
      v-for="item in options"
      :key="item.paId"
      :label="item.num"
      :value="item.paId"
    />
  </el-select>
</template>

<script>
import { listParkingAreas } from '@/api/fee/carCreateFeeApi'

export default {
  name: 'ParkingAreaSelect2',
  props: {
    value: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      options: [],
      loading: false
    }
  },
  methods: {
    async remoteMethod(query) {
      if (query) {
        this.loading = true
        try {
          const res = await listParkingAreas({
            num: query,
            page: 1,
            row: 50
          })
          this.options = res.parkingAreas || []
        } catch (error) {
          console.error('获取停车场失败:', error)
        } finally {
          this.loading = false
        }
      } else {
        this.options = []
      }
    },
    handleChange(value) {
      this.$emit('change', value)
    }
  }
}
</script>