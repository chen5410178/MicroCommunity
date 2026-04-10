<template>
  <el-dialog
    :title="$t('common.delete')"
    :visible.sync="visible"
    width="30%"
    @close="closeDialog"
  >
    <div class="text-center">
      <p>{{ $t('deleteReserveDining.confirmDelete') }}</p>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button @click="closeDialog">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="confirmDelete">{{ $t('common.confirmDelete') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { deleteReserveGoods } from '@/api/scm/doDiningApi'

export default {
  name: 'DeleteReserveDining',
  data() {
    return {
      visible: false,
      form: {
        goodsId: '',
        communityId: ''
      }
    }
  },
  methods: {
    open(params) {
      this.form = Object.assign({}, params)
      this.form.communityId = getCommunityId()
      this.visible = true
    },
    closeDialog() {
      this.visible = false
      this.form = {
        goodsId: '',
        communityId: ''
      }
    },
    confirmDelete() {
      deleteReserveGoods(this.form)
        .then(response => {
          if (response.code === 0) {
            this.$message.success(this.$t('common.operationSuccess'))
            this.closeDialog()
            this.$emit('success')
          } else {
            this.$message.error(response.msg)
          }
        })
        .catch(error => {
          console.error('请求失败:', error)
        })
    }
  }
}
</script>

<style scoped>
.text-center {
  text-align: center;
}
</style>