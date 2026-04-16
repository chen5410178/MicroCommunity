<template>
  <el-dialog :title="$t('addInspectionPoint.title')" :visible.sync="dialogVisible" width="60%" top="5vh"
    @close="resetForm">
    <el-form ref="addForm" :model="addInspectionPointInfo" class="text-left" :rules="rules" label-width="120px" label-position="right">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('addInspectionPoint.inspectionName')" prop="inspectionName">
            <el-input v-model="addInspectionPointInfo.inspectionName"
              :placeholder="$t('addInspectionPoint.placeholder.inspectionName')" clearable />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('addInspectionPoint.pointObjType')" prop="pointObjType">
            <el-select v-model="addInspectionPointInfo.pointObjType"
              :placeholder="$t('addInspectionPoint.placeholder.pointObjType')" style="width: 100%;"
              @change="_pointObjTypeChange">
              <el-option v-for="item in addInspectionPointInfo.pointObjTypes" :key="item.statusCd" :label="item.name"
                :value="item.statusCd" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20" v-if="addInspectionPointInfo.pointObjType === '2002'">
        <el-col :span="12">
          <el-form-item :label="$t('addInspectionPoint.pointObjName')" prop="pointObjName">
            <el-input v-model="addInspectionPointInfo.pointObjName"
              :placeholder="$t('addInspectionPoint.placeholder.pointObjName')" clearable />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20" v-if="addInspectionPointInfo.pointObjType === '1001'">
        <el-col :span="12">
          <el-form-item :label="$t('addInspectionPoint.machine')" prop="pointObjId">
            <machine-select2 ref="machineSelect" :parent-modal="true" @notify="handleMachineSelect" />
            <div class="tip-text">{{ $t('addInspectionPoint.tip.addMachine') }}</div>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('addInspectionPoint.itemId')" prop="itemId">
            <el-select v-model="addInspectionPointInfo.itemId"
              :placeholder="$t('addInspectionPoint.placeholder.itemId')" style="width: 100%;">
              <el-option v-for="item in addInspectionPointInfo.items" :key="item.itemId" :label="item.itemName"
                :value="item.itemId" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('addInspectionPoint.nfcCode')">
            <el-input v-model="addInspectionPointInfo.nfcCode"
              :placeholder="$t('addInspectionPoint.placeholder.nfcCode')" clearable />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('addInspectionPoint.lng')">
            <el-input v-model="addInspectionPointInfo.lng" :placeholder="$t('addInspectionPoint.placeholder.lng')"
              disabled />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('addInspectionPoint.lat')">
            <el-input v-model="addInspectionPointInfo.lat" :placeholder="$t('addInspectionPoint.placeholder.lat')"
              disabled />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="$t('addInspectionPoint.remark')">
            <el-input v-model="addInspectionPointInfo.remark" type="textarea" :rows="3"
              :placeholder="$t('addInspectionPoint.placeholder.remark')" />
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
      <el-button type="primary" @click="saveInspectionPointInfo">{{ $t('common.save') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { saveInspectionPoint } from '@/api/inspection/inspectionPointApi'
import { listInspectionItems } from '@/api/inspection/inspectionPointApi'
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'
import MachineSelect2 from '@/components/inspection/machineSelect2'
import SelectMapPos from '@/components/inspection/selectMapPos'


export default {
  name: 'AddInspectionPoint',
  components: {
    MachineSelect2,
    SelectMapPos
  },
  data() {
    return {
      dialogVisible: false,
      addInspectionPointInfo: {
        inspectionName: '',
        pointObjId: '-1',
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
          { required: true, message: this.$t('addInspectionPoint.rules.inspectionName'), trigger: 'blur' },
          { max: 100, message: this.$t('addInspectionPoint.rules.inspectionNameMax'), trigger: 'blur' }
        ],
        pointObjType: [
          { required: true, message: this.$t('addInspectionPoint.rules.pointObjType'), trigger: 'change' }
        ],
        pointObjName: [
          { required: true, message: this.$t('addInspectionPoint.rules.pointObjName'), trigger: 'blur' }
        ],
        pointObjId: [
          { required: true, message: this.$t('addInspectionPoint.rules.pointObjId'), trigger: 'change' }
        ],
        itemId: [
          { required: true, message: this.$t('addInspectionPoint.rules.itemId'), trigger: 'change' }
        ],
        remark: [
          { max: 200, message: this.$t('addInspectionPoint.rules.remarkMax'), trigger: 'blur' }
        ]
      },
      map: null,
      marker: null,
      communityId: ''
    }
  },
  methods: {
    open() {
      this.dialogVisible = true
      this.resetForm()
      this.getDictData()

      this.$nextTick(() => {
        this.initMap()
        this._listAddInspectionItems()
      })
    },

    async getDictData() {
      try {
        const data = await getDict('inspection_point', 'point_obj_type')
        this.addInspectionPointInfo.pointObjTypes = data
        console.log(this.addInspectionPointInfo.pointObjTypes)
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },

    async _listAddInspectionItems() {
      try {
        const params = {
          communityId: this.communityId,
          page: 1,
          row: 100
        }

        const response = await listInspectionItems(params)
        this.addInspectionPointInfo.items = response.data
      } catch (error) {
        console.error('获取巡检项目失败:', error)
      }
    },

    initMap() {
      // 这里简化了地图初始化，实际项目中需要根据地图API实现
      setTimeout(() => {
        this.$refs.selectMapPos.initMap({})
      }, 1000)
    },

    _pointObjTypeChange() {
      this.addInspectionPointInfo.pointObjId = ''
      this.addInspectionPointInfo.pointObjName = ''

      if (this.addInspectionPointInfo.pointObjType === '1001') {
        setTimeout(() => {
          this.$refs.machineSelect.searchMachines()
        }, 500)
      }
    },

    handleMachineSelect(data) {
      if (data && data.machineId && data.machineName) {
        this.addInspectionPointInfo.pointObjId = data.machineId
        this.addInspectionPointInfo.pointObjName = data.machineName
      }
    },

    saveInspectionPointInfo() {
      this.$refs.addForm.validate(async valid => {
        if (!valid) return
        if (this.addInspectionPointInfo.pointObjType == '2002') {
          this.addInspectionPointInfo.pointObjId = '-1'
        }
        // 特殊验证
        if (this.addInspectionPointInfo.pointObjType === '1001' && !this.addInspectionPointInfo.pointObjId) {
          this.$message.error(this.$t('addInspectionPoint.rules.machineRequired'))
          return
        }

        if (this.addInspectionPointInfo.pointObjType === '2002' && !this.addInspectionPointInfo.pointObjName) {
          this.$message.error(this.$t('addInspectionPoint.rules.locationRequired'))
          return
        }

        try {
          this.addInspectionPointInfo.communityId = this.communityId

          const response = await saveInspectionPoint(this.addInspectionPointInfo)

          if (response.code === 0) {
            this.$message.success(this.$t('common.operationSuccess'))
            this.dialogVisible = false
            this.$emit('success')
          } else {
            this.$message.error(response.msg || this.$t('addInspectionPoint.saveFailed'))
          }
        } catch (error) {
          console.error('保存巡检点失败:', error)
          this.$message.error(this.$t('addInspectionPoint.saveFailed'))
        }
      })
    },

    resetForm() {
      this.addInspectionPointInfo = {
        inspectionName: '',
        pointObjId: '-1',
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

      if (this.$refs.addForm) {
        this.$refs.addForm.resetFields()
      }

      setTimeout(() => {
        if (this.$refs.machineSelect) {
          this.$refs.machineSelect.clearMachine()
        }
      }, 500)
    },
    handlePositionChange(data) {
      this.addInspectionPointInfo.lng = data.lng
      this.addInspectionPointInfo.lat = data.lat
    }
  },
  created() {
    this.communityId = getCommunityId()
  }
}
</script>

<style scoped>
.tip-text {
  font-size: 12px;
  color: #909399;
  margin-top: 5px;
}
</style>