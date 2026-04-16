<template>
  <el-dialog
    :title="$t('equipmentAccount.moveEquipment')"
    :visible.sync="dialogVisible"
    width="50%"
    @close="handleClose">
    <el-form ref="form" :model="formData" label-width="150px">
      <el-form-item :label="$t('equipmentAccount.newLocation')" prop="locationObjId" required>
        <el-input 
          v-model="formData.locationObjName" 
          :placeholder="$t('equipmentAccount.selectLocation')"
          readonly
          @click.native="showSpaceTree">
          <el-button slot="append" icon="el-icon-search" @click="showSpaceTree"></el-button>
        </el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ $t('common.confirm') }}</el-button>
    </span>

    <!-- 空间树选择器 -->
    <space-tree-dialog 
      ref="spaceTreeDialog"
      @select="handleSpaceSelect">
    </space-tree-dialog>
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { moveEquipment } from '@/api/machine/equipmentAccountApi'
import SpaceTreeDialog from '@/components/common/SpaceTreeDialog'

export default {
  name: 'MoveEquipment',
  components: {
    SpaceTreeDialog
  },
  data() {
    return {
      dialogVisible: false,
      communityId: '',
      formData: {
        machineId: '',
        locationObjId: '',
        locationObjName: '',
        communityId: ''
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    open(data) {
      this.formData = {
        machineId: data.machineId,
        locationObjId: '',
        locationObjName: '',
        communityId: this.communityId
      }
      this.dialogVisible = true
    },
    handleClose() {
      this.$refs.form.resetFields()
    },
    showSpaceTree() {
      this.$refs.spaceTreeDialog.open()
    },
    handleSpaceSelect(space) {
      this.formData.locationObjId = space.id
      this.formData.locationObjName = space.text
    },
    async handleSubmit() {
      try {
        if (!this.formData.locationObjId) {
          this.$message.warning(this.$t('equipmentAccount.selectLocationFirst'))
          return
        }

        await moveEquipment(this.formData)
        this.$message.success(this.$t('common.operationSuccess'))
        this.dialogVisible = false
        this.$emit('success')
      } catch (error) {
        console.error('移动设备失败:', error)
      }
    }
  }
}
</script>