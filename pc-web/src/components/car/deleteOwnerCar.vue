<template>
  <el-dialog 
    :title="$t('listOwnerCar.confirmDelete')" 
    :visible.sync="visible" 
    width="500px">
    <p>{{ $t('listOwnerCar.deleteCarConfirm') }}</p>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="danger" @click="confirmDelete" :loading="loading">
        {{ $t('common.confirm') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { deleteOwnerCars } from '@/api/car/listOwnerCarApi'

export default {
  name: 'DeleteOwnerCar',
  data() {
    return {
      visible: false,
      loading: false,
      carInfo: null
    }
  },
  methods: {
    open(carInfo) {
      this.carInfo = carInfo
      this.visible = true
    },
    
    async confirmDelete() {
      console.log(this.carInfo)
      if (!this.carInfo || !this.carInfo.memberId) return
      
      this.loading = true
      try {
        await deleteOwnerCars({ carId: this.carInfo.carId,memberId:this.carInfo.memberId })
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.visible = false
      } catch (error) {
        console.error('删除车辆失败:', error)
        this.$message.error(error.message || this.$t('listOwnerCar.deleteError'))
      } finally {
        this.loading = false
      }
    }
  }
}
</script>