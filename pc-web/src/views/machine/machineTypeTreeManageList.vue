<template>
  <div class="machine-type-tree-manage-container">
    <div class="box-card">

      <el-row :gutter="20">
        <el-col :span="4" style="padding-right:0">
          <div class="">
            <el-button-group>
              <el-button type="text" size="small" @click="openAddParentMachineTypeModal">
                <i class="el-icon-plus"></i>{{ $t('machineTypeTree.addParent') }}
              </el-button>
              <el-button type="text" size="small" @click="openAddMachineTypeModal">
                <i class="el-icon-plus"></i>{{ $t('machineTypeTree.addChild') }}
              </el-button>
              <el-button type="text" size="small" @click="openEditMachineTypeModel">
                <i class="el-icon-edit"></i>{{ $t('machineTypeTree.edit') }}
              </el-button>
              <el-button type="text" size="small" @click="openDeleteMachineTypeModel">
                <i class="el-icon-delete"></i>{{ $t('machineTypeTree.delete') }}
              </el-button>
            </el-button-group>
          </div>
          <div>
            <machine-type-tree ref="machineTypeTree" @switchType="handleSwitchType"></machine-type-tree>
          </div>
        </el-col>
        <el-col :span="20">
          <el-card class="box-card">
            <div slot="header" class="flex justify-between">
              <span>{{ $t('machineTypeTree.detail') }}</span>
            </div>
            <div class="detail-content">
              <el-form label-width="120px">
                <el-form-item :label="$t('machineTypeTree.machineTypeName')">
                  <span>{{ machineTypeInfo.machineTypeName }}</span>
                </el-form-item>
                <el-form-item :label="$t('machineTypeTree.machineTypeCode')">
                  <span>{{ machineTypeInfo.machineTypeCode }}</span>
                </el-form-item>
                <el-form-item :label="$t('machineTypeTree.status')">
                  <el-radio-group v-model="machineTypeInfo.isEnable" disabled>
                    <el-radio :label="'1000'">{{ $t('machineTypeTree.enable') }}</el-radio>
                    <el-radio :label="'2000'">{{ $t('machineTypeTree.disable') }}</el-radio>
                  </el-radio-group>
                </el-form-item>
                <el-form-item :label="$t('machineTypeTree.importanceLevel')">
                  <template v-for="(item, index) in importanceLevels">
                    <span v-if="machineTypeInfo.importanceLevel == item.statusCd" :key="index">
                      {{ item.name }}
                    </span>
                  </template>
                </el-form-item>
                <el-form-item :label="$t('machineTypeTree.seq')">
                  <span>{{ machineTypeInfo.seq }}</span>
                </el-form-item>
                <el-form-item :label="$t('machineTypeTree.unit')">
                  <span>{{ machineTypeInfo.unit }}</span>
                </el-form-item>
                <el-form-item :label="$t('machineTypeTree.warrantyDeadline')">
                  <span>{{ machineTypeInfo.warrantyDeadline }}</span>
                </el-form-item>
                <el-form-item :label="$t('machineTypeTree.remark')">
                  <span>{{ machineTypeInfo.remark }}</span>
                </el-form-item>
              </el-form>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <add-machine-type ref="addMachineType" @success="handleSuccess"></add-machine-type>
    <edit-machine-type ref="editMachineType" @success="handleSuccess"></edit-machine-type>
    <delete-machine-type ref="deleteMachineType" @success="handleSuccess"></delete-machine-type>
  </div>
</template>

<script>
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'
import MachineTypeTree from '@/components/machine/machineTypeTree'
import AddMachineType from '@/components/machine/addMachineType'
import EditMachineType from '@/components/machine/editMachineType'
import DeleteMachineType from '@/components/machine/deleteMachineType'
import { listMachineType } from '@/api/machine/machineTypeTreeManageApi'

export default {
  name: 'MachineTypeTreeManageList',
  components: {
    MachineTypeTree,
    AddMachineType,
    EditMachineType,
    DeleteMachineType
  },
  data() {
    return {
      machineTypeInfo: {},
      importanceLevels: [],
      currentTypeId: '',
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.getDictData()
  },
  methods: {
    async getDictData() {
      try {
        const data = await getDict('machine_type', 'importance_level')
        this.importanceLevels = data
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },
    handleSwitchType(data) {
      this.currentTypeId = data.typeId
      this.machineTypeInfo = {
        ...data
      }
      this._loadMachienType()
    },
    openAddParentMachineTypeModal() {
      this.$refs.addMachineType.open({})
    },
    openAddMachineTypeModal() {
      if (!this.currentTypeId) {
        this.$message.warning(this.$t('machineTypeTree.selectTypeFirst'))
        return
      }
      this.$refs.addMachineType.open({ typeId: this.currentTypeId })
    },
    openEditMachineTypeModel() {
      if (!this.currentTypeId) {
        this.$message.warning(this.$t('machineTypeTree.selectTypeFirst'))
        return
      }
      this.$refs.editMachineType.open(this.machineTypeInfo)
    },
    openDeleteMachineTypeModel() {
      if (!this.currentTypeId) {
        this.$message.warning(this.$t('machineTypeTree.selectTypeFirst'))
        return
      }
      this.$refs.deleteMachineType.open(this.machineTypeInfo)
    },
    async _loadMachienType() {
      const res = await listMachineType({ communityId: getCommunityId(), typeId: this.machineTypeInfo.typeId, page: 1, row: 1 });
      this.machineTypeInfo = { ...res.data[0] }
    },
    handleSuccess() {
      this.$refs.machineTypeTree.refreshTree()
      this.handleSwitchType(this.machineTypeInfo)

    }
  }
}
</script>

<style lang="scss" scoped>
.machine-type-tree-manage-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .detail-content {
    padding: 20px;

    span {
      line-height: 40px;
    }
  }

  .el-row {
    width: 100%;
  }

}
</style>