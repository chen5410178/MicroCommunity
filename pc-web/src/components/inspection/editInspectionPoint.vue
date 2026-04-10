<template>
  <el-dialog :title="$t('editInspectionPoint.title')" :visible.sync="dialogVisible" width="60%" top="5vh"
    @close="resetForm">
    <el-form ref="editForm" :model="editInspectionPointInfo" :rules="rules" label-width="120px" label-position="right">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('editInspectionPoint.inspectionName')" prop="inspectionName">
            <el-input v-model="editInspectionPointInfo.inspectionName"
              :placeholder="$t('editInspectionPoint.placeholder.inspectionName')" clearable />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('editInspectionPoint.pointObjType')" prop="pointObjType">
            <el-select v-model="editInspectionPointInfo.pointObjType"
              :placeholder="$t('editInspectionPoint.placeholder.pointObjType')" style="width: 100%;"
              @change="_pointObjTypeChange" disabled>
              <el-option v-for="item in editInspectionPointInfo.pointObjTypes" :key="item.statusCd" :label="item.name"
                :value="item.statusCd" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20" v-if="editInspectionPointInfo.pointObjType === '2002'">
        <el-col :span="12">
          <el-form-item :label="$t('editInspectionPoint.pointObjName')" prop="pointObjName">
            <el-input v-model="editInspectionPointInfo.pointObjName"
              :placeholder="$t('editInspectionPoint.placeholder.pointObjName')" clearable />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20" v-if="editInspectionPointInfo.pointObjType === '1001'">
        <el-col :span="24">
          <el-form-item :label="$t('editInspectionPoint.machine')" prop="pointObjId">
            <machine-select2 ref="machineSelect" :parent-modal="true" @notify="handleMachineSelect" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('editInspectionPoint.itemId')" prop="itemId">
            <el-select v-model="editInspectionPointInfo.itemId"
              :placeholder="$t('editInspectionPoint.placeholder.itemId')" style="width: 100%;">
              <el-option v-for="item in editInspectionPointInfo.items" :key="item.itemId" :label="item.itemName"
                :value="item.itemId" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('editInspectionPoint.nfcCode')">
            <el-input v-model="editInspectionPointInfo.nfcCode"
              :placeholder="$t('editInspectionPoint.placeholder.nfcCode')" clearable />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('editInspectionPoint.lng')">
            <el-input v-model="editInspectionPointInfo.lng" :placeholder="$t('editInspectionPoint.placeholder.lng')"
              disabled />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('editInspectionPoint.lat')">
            <el-input v-model="editInspectionPointInfo.lat" :placeholder="$t('editInspectionPoint.placeholder.lat')"
              disabled />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="$t('editInspectionPoint.remark')">
            <el-input v-model="editInspectionPointInfo.remark" type="textarea" :rows="3"
              :placeholder="$t('editInspectionPoint.placeholder.remark')" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="24">
          <select-map-pos ref="selectMapPos" @position-change="handlePositionChange" />

        </el-col>
      </el-row>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">{{ $t('common.cancel') }}</el-button>
      <el-button type="primary" @click="updateInspectionPoint">{{ $t('common.save') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { updateInspectionPoint } from '@/api/inspection/inspectionPointApi'
import { listInspectionItems } from '@/api/inspection/inspectionPointApi'
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'
import MachineSelect2 from '@/components/inspection/machineSelect2'
import SelectMapPos from '@/components/inspection/selectMapPos'

export default {
  name: 'EditInspectionPoint',
  components: {
    MachineSelect2,
    SelectMapPos
  },
  data() {
    return {
      dialogVisible: false,
      editInspectionPointInfo: {
        inspectionId: '',
        inspectionName: '',
        pointObjId: '',
        pointObjType: '',
        pointObjTypes: [],
        pointObjName: '',
        remark: '',
        items: [],
        itemId: '',
        nfcCode: '',
        lng: '',
        lat: '',
        communityId: ''
      },
      rules: {
        inspectionName: [
          { required: true, message: this.$t('editInspectionPoint.rules.inspectionName'), trigger: 'blur' },
          { max: 100, message: this.$t('editInspectionPoint.rules.inspectionNameMax'), trigger: 'blur' }
        ],
        pointObjType: [
          { required: true, message: this.$t('editInspectionPoint.rules.pointObjType'), trigger: 'change' }
        ],
        pointObjName: [
          { required: true, message: this.$t('editInspectionPoint.rules.pointObjName'), trigger: 'blur' }
        ],
        pointObjId: [
          { required: true, message: this.$t('editInspectionPoint.rules.pointObjId'), trigger: 'change' }
        ],
        itemId: [
          { required: true, message: this.$t('editInspectionPoint.rules.itemId'), trigger: 'change' }
        ],
        remark: [
          { max: 200, message: this.$t('editInspectionPoint.rules.remarkMax'), trigger: 'blur' }
        ]
      },
      communityId: ''
    }
  },
  methods: {
    open(inspectionPoint) {
      this.dialogVisible = true
      this.resetForm()

      this.$nextTick(() => {
        this.getDictData()
        this._listEditInspectionItems()

        // 填充表单数据
        Object.assign(this.editInspectionPointInfo, inspectionPoint)

        // 设置设备选择器（如果是设备类型）
        if (inspectionPoint.pointObjType === '1001') {
          this.$nextTick(() => {
            if (this.$refs.machineSelect) {
              this.$refs.machineSelect.setMachine({
                machineId: inspectionPoint.pointObjId,
                machineName: inspectionPoint.pointObjName
              })
            }
          })
        }
      })
      setTimeout(() => {
        this.$refs.selectMapPos.initMap({})
      }, 1000)
    },

    async getDictData() {
      try {
        const data = await getDict('inspection_point', 'point_obj_type')
        this.editInspectionPointInfo.pointObjTypes = data
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },

    async _listEditInspectionItems() {
      try {
        const params = {
          communityId: this.communityId,
          page: 1,
          row: 100
        }

        const response = await listInspectionItems(params)
        this.editInspectionPointInfo.items = response.data
      } catch (error) {
        console.error('获取巡检项目失败:', error)
      }
    },

    _pointObjTypeChange() {
      this.editInspectionPointInfo.pointObjId = ''
      this.editInspectionPointInfo.pointObjName = ''

      if (this.editInspectionPointInfo.pointObjType === '1001') {
        this.$refs.machineSelect.clearMachine()
      }
    },

    handleMachineSelect(data) {
      if (data && data.machineId && data.machineName) {
        this.editInspectionPointInfo.pointObjId = data.machineId
        this.editInspectionPointInfo.pointObjName = data.machineName
      }
    },

    handlePositionChange(position) {
      this.editInspectionPointInfo.lat = position.lat
      this.editInspectionPointInfo.lng = position.lng
    },

    updateInspectionPoint() {
      this.$refs.editForm.validate(async valid => {
        if (!valid) return

        // 特殊验证
        if (this.editInspectionPointInfo.pointObjType === '1001' && !this.editInspectionPointInfo.pointObjId) {
          this.$message.error(this.$t('editInspectionPoint.rules.machineRequired'))
          return
        }

        if (this.editInspectionPointInfo.pointObjType === '2002' && !this.editInspectionPointInfo.pointObjName) {
          this.$message.error(this.$t('editInspectionPoint.rules.locationRequired'))
          return
        }

        try {
          this.editInspectionPointInfo.communityId = this.communityId

          const response = await updateInspectionPoint(this.editInspectionPointInfo)

          if (response.code === 0) {
            this.$message.success(this.$t('common.operationSuccess'))
            this.dialogVisible = false
            this.$emit('success')
          } else {
            this.$message.error(response.msg || this.$t('editInspectionPoint.updateFailed'))
          }
        } catch (error) {
          console.error('更新巡检点失败:', error)
          this.$message.error(this.$t('editInspectionPoint.updateFailed'))
        }
      })
    },

    resetForm() {
      this.editInspectionPointInfo = {
        inspectionId: '',
        inspectionName: '',
        pointObjId: '',
        pointObjType: '',
        pointObjName: '',
        remark: '',
        items: [],
        itemId: '',
        nfcCode: '',
        lng: '',
        lat: '',
        communityId: ''
      }

      if (this.$refs.editForm) {
        this.$refs.editForm.resetFields()
      }

      if (this.$refs.machineSelect) {
        this.$refs.machineSelect.clearMachine()
      }

      if (this.$refs.mapSelector) {
        this.$refs.mapSelector.updatePosition(39.916527, 116.397128)
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
  }
}
</script>