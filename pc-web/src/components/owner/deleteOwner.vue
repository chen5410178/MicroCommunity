<template>
  <el-dialog :title="$t('listOwner.buttons.delete')" :visible.sync="visible" width="500px">
    <p>{{ $t('listOwner.confirmDelete') }}</p>

    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('listOwner.buttons.cancel') }}
      </el-button>
      <el-button type="primary" @click="confirmDelete">
        {{ $t('common.confirm') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { deleteOwner } from '@/api/owner/ownerApi'

export default {
  name: 'DeleteOwner',
  data() {
    return {
      visible: false,
      owner: {}
    }
  },
  methods: {
    open(owner) {
      this.owner = { ...owner }
      this.visible = true
    },

    async confirmDelete() {
      try {
        await deleteOwner({
          ownerId: this.owner.ownerId,
          memberId: this.owner.memberId
        })
        this.$message.success(this.$t('common.operationSuccess'))
        this.visible = false
        this.$emit('success')
      } catch (error) {
        this.$message.error(error)
      }
    }
  }
}
</script>