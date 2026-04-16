<template>
  <el-dialog
    :title="$t('room.addFloor.title')"
    :visible.sync="visible"
    width="50%"
    :before-close="handleClose"
  >
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-form-item :label="$t('room.addFloor.floorNum')" prop="floorNum">
        <el-input v-model="form.floorNum" :placeholder="$t('room.addFloor.floorNumPlaceholder')" />
      </el-form-item>
      
      <el-form-item :label="$t('room.addFloor.name')" prop="name">
        <el-input v-model="form.name" :placeholder="$t('room.addFloor.namePlaceholder')" />
      </el-form-item>
      
      <el-form-item :label="$t('room.addFloor.floorArea')" prop="floorArea">
        <el-input v-model="form.floorArea" :placeholder="$t('room.addFloor.floorAreaPlaceholder')">
          <template slot="append">m²</template>
        </el-input>
      </el-form-item>
      
      <el-form-item :label="$t('room.addFloor.seq')" class="text-left" prop="seq">
        <el-input-number v-model="form.seq" :min="1" />
      </el-form-item>
      
      <el-form-item :label="$t('room.addFloor.remark')" prop="remark">
        <el-input v-model="form.remark" type="textarea" :placeholder="$t('room.addFloor.remarkPlaceholder')" />
      </el-form-item>
      
    </el-form>
    
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSave">{{ $t('common.save') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { addFloor } from '@/api/room/addFloorApi'

export default {
  name: 'AddFloor',
  props: {
    callBackListener: String,
    callBackFunction: String
  },
  data() {
    return {
      visible: false,
      form: {
        communityId: this.getCommunityId(),
        floorNum: '',
        name: '',
        floorArea: '',
        seq: 1,
        remark: ''
      },
      errorInfo: '',
      rules: {
        floorNum: [
          { required: true, message: this.$t('room.addFloor.floorNumRequired'), trigger: 'blur' },
          { min: 1, max: 64, message: this.$t('room.addFloor.floorNumLength'), trigger: 'blur' }
        ],
        name: [
          { required: true, message: this.$t('room.addFloor.nameRequired'), trigger: 'blur' },
          { min: 2, max: 64, message: this.$t('room.addFloor.nameLength'), trigger: 'blur' }
        ],
        floorArea: [
          { required: true, message: this.$t('room.addFloor.floorAreaRequired'), trigger: 'blur' },
          { pattern: /^\d+(\.\d{1,2})?$/, message: this.$t('room.addFloor.floorAreaFormat'), trigger: 'blur' }
        ],
        seq: [
          { required: true, message: this.$t('room.addFloor.seqRequired'), trigger: 'blur' },
          { type: 'number', message: this.$t('room.addFloor.seqNumber'), trigger: 'blur' }
        ],
        remark: [
          { max: 200, message: this.$t('room.addFloor.remarkMaxLength'), trigger: 'blur' }
        ]
      }
    }
  },
  watch: {
    'form.floorNum': function(newVal) {
      if (newVal) {
        this.form.name = `${newVal}${this.$t('room.addFloor.buildingSuffix')}`
      } else {
        this.form.name = ''
      }
    }
  },
  methods: {
    open() {
      this.visible = true
      this.resetForm()
    },
    handleClose() {
      this.visible = false
      this.resetForm()
    },
    resetForm() {
      this.form = {
        communityId: this.getCommunityId(),
        floorNum: '',
        name: '',
        floorArea: '',
        seq: 1,
        remark: ''
      }
      this.errorInfo = ''
      this.$refs.form.resetFields()
    },
    async handleSave() {
      this.$refs.form.validate(async (valid) => {
        if (!valid) return
        
        try {

          const response = await addFloor(this.form)
          
          if (response.code == 0) {
            this.$message.success(this.$t('common.operationSuccess'))
            this.handleClose()
            this.$emit('handleRefreshTree', {})
          } else {
            this.errorInfo = response.msg || this.$t('room.addFloor.saveFailed')
          }
        } catch (error) {
          this.errorInfo = this.$t('room.addFloor.saveError')
        }
      })
    },
  }
}
</script>