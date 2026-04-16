<template>
  <el-dialog
    :title="$t('org.confirmYourAction')"
    :visible.sync="visible"
    width="30%"
  >
    <span>{{ $t('org.confirmDeleteOrg') }}</span>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleConfirm">{{ $t('common.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteOrg } from '@/api/org/orgApi'

export default {
  name: 'DeleteOrg',
  data() {
    return {
      visible: false,
      currentOrg: null
    }
  },
  methods: {
    show(org) {
      this.currentOrg = org
      this.visible = true
    },
    handleConfirm() {
      deleteOrg({
        orgId: this.currentOrg.id
      }).then(response => {
          console.log(response)
        this.$message.success(response.msg)
        this.visible = false
        this.$emit('refresh')
      },error => {
        console.log(error)
        this.$message.error(error)
      })
    }
  }
}
</script>