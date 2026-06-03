<template>
  <div class="machine-type-tree-manage-container">
    <el-row :gutter="20">
      <el-col :span="4" class="left-panel">
        <el-card class="type-card">
          <div class="type-list-item type-add"
            @click="openAddParentMachineTypeModal">
            <i class="el-icon-plus"></i>{{ $t('machineTypeTree.addParent') }}
          </div>
          <machine-type-tree ref="machineTypeTree" @switchType="handleSwitchType"></machine-type-tree>
        </el-card>
      </el-col>
      <el-col :span="20" class="right-panel">
        <el-row>
          <el-col :span="24">
            <el-card>
              <div slot="header" class="flex justify-between">
                <span>{{ $t('machineTypeTree.detail') }}</span>
                <div>
                  <template v-if="currentTypeId">
                    <el-button size="small" @click="openAddMachineTypeModal">
                      {{ $t('machineTypeTree.addChild') }}
                    </el-button>
                    <el-button size="small" @click="openEditMachineTypeModel">
                      {{ $t('machineTypeTree.edit') }}
                    </el-button>
                    <el-button size="small" type="danger" @click="openDeleteMachineTypeModel">
                      {{ $t('machineTypeTree.delete') }}
                    </el-button>
                  </template>
                </div>
              </div>

              <div v-if="currentTypeId" class="detail-wrapper margin-top">
                <el-row :gutter="10">
                  <el-col :span="6">
                    <div class="detail-item">
                      <span class="detail-label">{{ $t('machineTypeTree.machineTypeName') }}：</span>
                      <span>{{ machineTypeInfo.machineTypeName }}</span>
                    </div>
                  </el-col>
                  <el-col :span="6">
                    <div class="detail-item">
                      <span class="detail-label">{{ $t('machineTypeTree.machineTypeCode') }}：</span>
                      <span>{{ machineTypeInfo.machineTypeCode }}</span>
                    </div>
                  </el-col>
                  <el-col :span="6">
                    <div class="detail-item">
                      <span class="detail-label">{{ $t('machineTypeTree.status') }}：</span>
                      <span>{{ machineTypeInfo.isEnable == '1000' ? $t('machineTypeTree.enable') : $t('machineTypeTree.disable') }}</span>
                    </div>
                  </el-col>
                  <el-col :span="6">
                    <div class="detail-item">
                      <span class="detail-label">{{ $t('machineTypeTree.importanceLevel') }}：</span>
                      <template v-for="(item, index) in importanceLevels">
                        <span v-if="machineTypeInfo.importanceLevel == item.statusCd" :key="index">
                          {{ item.name }}
                        </span>
                      </template>
                    </div>
                  </el-col>
                  <el-col :span="6">
                    <div class="detail-item">
                      <span class="detail-label">{{ $t('machineTypeTree.seq') }}：</span>
                      <span>{{ machineTypeInfo.seq }}</span>
                    </div>
                  </el-col>
                  <el-col :span="6">
                    <div class="detail-item">
                      <span class="detail-label">{{ $t('machineTypeTree.unit') }}：</span>
                      <span>{{ machineTypeInfo.unit }}</span>
                    </div>
                  </el-col>
                  <el-col :span="6">
                    <div class="detail-item">
                      <span class="detail-label">{{ $t('machineTypeTree.warrantyDeadline') }}：</span>
                      <span>{{ machineTypeInfo.warrantyDeadline }}</span>
                    </div>
                  </el-col>
                  <el-col :span="6">
                    <div class="detail-item">
                      <span class="detail-label">{{ $t('machineTypeTree.remark') }}：</span>
                      <span>{{ machineTypeInfo.remark }}</span>
                    </div>
                  </el-col>
                </el-row>
              </div>

              <div v-else class="text-center" style="padding: 40px 0; color: #909399;">
                {{ $t('machineTypeTree.noTypeSelected') }}
              </div>
            </el-card>
          </el-col>
        </el-row>
      </el-col>
    </el-row>

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
      if (!data.typeId) {
        this.machineTypeInfo = {}
        return
      }
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

  .left-panel {
    .type-card {
      .type-list-item {
        padding: 10px;
        text-align: center;
        cursor: pointer;
        border-radius: 4px;
        margin-bottom: 5px;
        transition: all 0.3s;
        font-size: 14px;
        color: #606266;

        &:hover {
          background-color: #f5f7fa;
        }

        &.selected {
          background-color: #409eff;
          color: white;
        }

        &.type-add {
          color: #409eff;
          border: 1px dashed #409eff;
          margin-bottom: 10px;

          &:hover {
            background-color: #ecf5ff;
          }
        }
      }

      ::v-deep .tree-container {
        border: none;
        box-shadow: none;
        padding: 0;

        .el-card__body {
          padding: 0;
        }
      }
    }
  }

  .right-panel {
    padding-left: 5px;

    .detail-wrapper {
      .detail-item {
        padding: 8px 0;
        font-size: 14px;
        color: #606266;
      }

      .detail-label {
        margin-right: 4px;
      }
    }
  }
}

.margin-top {
  margin-top: 15px;
}
</style>