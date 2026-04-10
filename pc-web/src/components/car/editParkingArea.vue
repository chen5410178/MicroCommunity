<template>
  <el-dialog 
    :title="$t('parkingAreaManage.editTitle')" 
    :visible.sync="visible" 
    width="50%"
    @close="closeDialog"
  >
    <el-form ref="form" :model="editParkingAreaInfo" label-width="150px" label-position="left">
      <el-form-item 
        :label="$t('parkingAreaManage.parkingNum')" 
        prop="num"
        :rules="[{ required: true, message: $t('parkingAreaManage.numRequired'), trigger: 'blur' }]"
      >
        <el-input 
          v-model.trim="editParkingAreaInfo.num" 
          :placeholder="$t('parkingAreaManage.inputParkingNum')"
        />
      </el-form-item>
      
      <el-form-item 
        :label="$t('parkingAreaManage.parkingType')" 
        prop="typeCd"
        :rules="[{ required: true, message: $t('parkingAreaManage.typeRequired'), trigger: 'change' }]"
      >
        <el-select 
          v-model="editParkingAreaInfo.typeCd" 
          :placeholder="$t('parkingAreaManage.selectParkingType')"
          style="width: 100%"
        >
          <el-option 
            v-for="item in parkingTypes" 
            :key="item.value" 
            :label="item.label" 
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      
      <div v-for="(item, index) in editParkingAreaInfo.attrs" :key="index">
        <el-form-item 
          v-if="item.specType === '2233'" 
          :label="item.specName"
          :prop="`attrs[${index}].value`"
          :rules="item.required ? [{ required: true, message: $t('common.required'), trigger: 'blur' }] : []"
        >
          <el-input 
            v-model="item.value" 
            :placeholder="item.specHoldplace"
          />
        </el-form-item>
        
        <el-form-item 
          v-if="item.specType === '3344'" 
          :label="item.specName"
          :prop="`attrs[${index}].value`"
          :rules="item.required ? [{ required: true, message: $t('common.required'), trigger: 'change' }] : []"
        >
          <el-select 
            v-model="item.value" 
            :placeholder="item.specHoldplace"
            style="width: 100%"
          >
            <el-option 
              v-for="value in item.values" 
              :key="value.value" 
              :label="value.valueName" 
              :value="value.value"
            />
          </el-select>
        </el-form-item>
      </div>
      
      <el-form-item :label="$t('parkingAreaManage.remark')">
        <el-input 
          v-model="editParkingAreaInfo.remark" 
          type="textarea" 
          :rows="3" 
          :placeholder="$t('parkingAreaManage.remarkPlaceholder')"
        />
      </el-form-item>
    </el-form>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="editParkingArea" :loading="saving">
        {{ $t('common.save') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { updateParkingArea } from '@/api/car/parkingAreaManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'EditParkingArea',
  data() {
    return {
      visible: false,
      saving: false,
      editParkingAreaInfo: {
        paId: '',
        num: '',
        typeCd: '',
        remark: '',
        attrs: []
      },
      parkingTypes: [
        { value: '1001', label: this.$t('parkingAreaManage.aboveGround') },
        { value: '2001', label: this.$t('parkingAreaManage.underground') }
      ]
    }
  },
  methods: {
    open(row) {
      this.visible = true
      this.editParkingAreaInfo = JSON.parse(JSON.stringify(row))
      this.$nextTick(() => {
        this.$refs.form.clearValidate()
      })
    },
    
    closeDialog() {
      this.$refs.form.resetFields()
    },
    
    async editParkingArea() {
      this.$refs.form.validate(async valid => {
        if (!valid) return
        
        try {
          this.saving = true
          const params = {
            ...this.editParkingAreaInfo,
            communityId: getCommunityId()
          }
          
          await updateParkingArea(params)
          this.$message.success(this.$t('common.operationSuccess'))
          this.visible = false
          this.$emit('success')
        } catch (error) {
          console.error('修改停车场失败:', error)
          this.$message.error(this.$t('parkingAreaManage.editFailed'))
        } finally {
          this.saving = false
        }
      })
    }
  }
}
</script>