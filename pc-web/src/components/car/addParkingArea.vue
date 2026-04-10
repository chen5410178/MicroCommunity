<template>
  <el-dialog :title="$t('parkingAreaManage.addTitle')" :visible.sync="visible" width="50%" @close="closeDialog">
    <el-form ref="form" :model="addParkingAreaInfo" label-width="150px" label-position="left">
      <el-form-item :label="$t('parkingAreaManage.parkingNum')" prop="num"
        :rules="[{ required: true, message: $t('parkingAreaManage.numRequired'), trigger: 'blur' }]">
        <el-input v-model.trim="addParkingAreaInfo.num" :placeholder="$t('parkingAreaManage.inputParkingNum')" />
      </el-form-item>

      <el-form-item :label="$t('parkingAreaManage.parkingType')" prop="typeCd"
        :rules="[{ required: true, message: $t('parkingAreaManage.typeRequired'), trigger: 'change' }]">
        <el-select v-model="addParkingAreaInfo.typeCd" :placeholder="$t('parkingAreaManage.selectParkingType')"
          style="width: 100%">
          <el-option v-for="item in parkingTypes" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>

      <div v-for="(item, index) in addParkingAreaInfo.attrs" :key="index">
        <el-form-item v-if="item.specType === '2233'" :label="item.specName" :prop="`attrs[${index}].value`"
          :rules="item.required ? [{ required: true, message: $t('common.required'), trigger: 'blur' }] : []">
          <el-input v-model="item.value" :placeholder="item.specHoldplace" />
        </el-form-item>

        <el-form-item v-if="item.specType === '3344'" :label="item.specName" :prop="`attrs[${index}].value`"
          :rules="item.required ? [{ required: true, message: $t('common.required'), trigger: 'change' }] : []">
          <el-select v-model="item.value" :placeholder="item.specHoldplace" style="width: 100%">
            <el-option v-for="value in item.values" :key="value.value" :label="value.valueName" :value="value.value" />
          </el-select>
        </el-form-item>
      </div>

      <el-form-item :label="$t('parkingAreaManage.remark')">
        <el-input v-model="addParkingAreaInfo.remark" type="textarea" :rows="3"
          :placeholder="$t('parkingAreaManage.remarkPlaceholder')" />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="saveParkingAreaInfo" :loading="saving">
        {{ $t('common.save') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { saveParkingArea } from '@/api/car/parkingAreaManageApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'AddParkingArea',
  data() {
    return {
      visible: false,
      saving: false,
      addParkingAreaInfo: {
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
    open() {
      this.visible = true
      this.resetForm()
      this._loadParkingAreaAttrSpec()
    },

    closeDialog() {
      this.$refs.form.resetFields()
    },

    resetForm() {
      this.addParkingAreaInfo = {
        num: '',
        typeCd: '',
        remark: '',
        attrs: []
      }
    },

    async saveParkingAreaInfo() {
      this.$refs.form.validate(async valid => {
        if (!valid) return

        try {
          this.saving = true
          const params = {
            ...this.addParkingAreaInfo,
            communityId: getCommunityId()
          }

          await saveParkingArea(params)
          this.$message.success(this.$t('common.operationSuccess'))
          this.visible = false
          this.$emit('success')
        } catch (error) {
          console.error('添加停车场失败:', error)
          this.$message.error(this.$t('parkingAreaManage.addFailed'))
        } finally {
          this.saving = false
        }
      })
    },

    async _loadParkingAreaAttrSpec() {
      try {
        // 实际项目中从API获取属性配置
        // 这里使用模拟数据
        this.addParkingAreaInfo.attrs = [
        ]
      } catch (error) {
        console.error('加载停车场属性失败:', error)
      }
    }
  }
}
</script>