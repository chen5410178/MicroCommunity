<template>
  <el-dialog :title="$t('writeOweFeeCallable.title')" :visible.sync="dialogVisible" width="40%"
    :before-close="handleClose">
    <div class="ibox-content">
      <el-form label-width="120px" class="text-left">
        <el-form-item :label="$t('writeOweFeeCallable.room')">
          <el-input v-model="writeOweFeeCallableInfo.roomName" readonly
            :placeholder="$t('writeOweFeeCallable.roomPlaceholder')"></el-input>
        </el-form-item>
        <el-form-item :label="$t('writeOweFeeCallable.fees')">
          <el-checkbox-group v-model="writeOweFeeCallableInfo.feeIds">
            <el-checkbox v-for="(item, index) in writeOweFeeCallableInfo.fees" :key="index" :label="item.feeId">
              {{ item.feeName }}
            </el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item :label="$t('writeOweFeeCallable.remark')">
          <el-input type="textarea" :rows="5" v-model="writeOweFeeCallableInfo.remark"
            :placeholder="$t('writeOweFeeCallable.remarkPlaceholder')"></el-input>
        </el-form-item>
      </el-form>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">{{ $t('writeOweFeeCallable.cancel') }}</el-button>
      <el-button type="primary" @click="_wirteCallable">{{ $t('writeOweFeeCallable.submit') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { writeOweFeeCallable, listFee } from '@/api/fee/writeOweFeeCallableApi'

export default {
  name: 'WriteOweFeeCallable',
  data() {
    return {
      dialogVisible: false,
      writeOweFeeCallableInfo: {
        roomId: '',
        roomName: '',
        fees: [],
        feeIds: [],
        remark: ''
      }
    }
  },
  created() {
    this._initEvent()
  },
  methods: {
    _initEvent() {
      this.$on('openWriteOweFeeCallableModal', this.open)
    },
    open(param) {
      Object.assign(this.writeOweFeeCallableInfo, param)
      this._loadWriteOweRoomFees()
      this.dialogVisible = true
    },
    handleClose(done) {
      this.clearWriteOweFeeCallable()
      done()
    },
    async _wirteCallable() {
      try {
        const params = {
          ...this.writeOweFeeCallableInfo,
          communityId: getCommunityId()
        }
        const res = await writeOweFeeCallable(params)
        if (res.code === 0) {
          this.dialogVisible = false
          this.clearWriteOweFeeCallable()
          this.$emit('listOweFeeCallable', {})
          this.$emit('listOwnerData', {})
          this.$emit('success', {})
          this.$message.success(this.$t('common.operationSuccess'))
        } else {
          this.$message.error(res.msg)
        }
      } catch (error) {
        console.error('Request failed:', error)
        this.$message.error(error.message)
      }
    },
    clearWriteOweFeeCallable() {
      this.writeOweFeeCallableInfo = {
        roomId: '',
        roomName: '',
        fees: [],
        feeIds: [],
        remark: ''
      }
    },
    async _loadWriteOweRoomFees() {
      try {
        const res = await listFee({
          page: 1,
          row: 100,
          communityId: getCommunityId(),
          payerObjId: this.writeOweFeeCallableInfo.roomId,
          state: '2008001'
        })
        this.writeOweFeeCallableInfo.fees = res.fees
      } catch (error) {
        console.error('Request failed:', error)
      }
    }
  }
}
</script>