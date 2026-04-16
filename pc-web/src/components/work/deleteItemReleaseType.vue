<template>
  <el-dialog
    :title="$t('itemReleaseTypeManage.delete.title')"
    :visible.sync="visible"
    width="30%"
    center
  >
    <div class="text-center">
      <p>{{ $t('itemReleaseTypeManage.delete.confirmText') }}</p>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="danger" @click="handleConfirm">
        {{ $t('common.confirm') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteItemReleaseType } from '@/api/work/itemReleaseTypeManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'DeleteItemReleaseType',
  data() {
    return {
      visible: false,
      form: {
        typeId: '',
        communityId: ''
      }
    }
  },
  methods: {
    open(row) {
      this.form = {
        typeId: row.typeId,
        communityId: getCommunityId()
      }
      this.visible = true
    },
    async handleConfirm() {
      try {
        await deleteItemReleaseType(this.form)
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        this.$message.error(this.$t('itemReleaseTypeManage.delete.error'))
      }
    }
  }
}
</script>