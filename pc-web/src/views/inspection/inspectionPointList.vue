<template>
  <div class="inspection-point-container">
    <el-row :gutter="20">
      <el-col :span="4" class="left-panel">
        <el-card class="point-card">
          <div style="display: flex; align-items: center; margin-bottom: 12px; gap: 6px;">
            <el-input
              :placeholder="$t('inspectionPoint.placeholder.inspectionName')"
              v-model="inspectionPointInfo.conditions.inspectionName"
              clearable
              size="small"
              style="flex: 1;"
            />
            <el-button type="text" size="small" @click="_queryInspectionPointMethod()">
              {{ $t('common.search') }}
            </el-button>
            <el-button type="text" size="small" @click="_openAddInspectionPointModal()">
              {{ $t('common.add') }}
            </el-button>
          </div>

          <div class="point-list-wrapper">
            <ul class="point-list">
              <li v-for="(point, index) in inspectionPointInfo.points" :key="index"
                @click="_switchInspectionPoint(point)"
                :class="{ 'selected': point.inspectionId == inspectionPointInfo.point.inspectionId }"
                class="point-list-item">
                {{ point.inspectionName }}
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>
      <el-col :span="20" class="right-panel">
        <el-row v-if="inspectionPointInfo.point.inspectionId">
          <el-col :span="24">
            <el-card>
              <div slot="header" class="flex justify-between">
                <span>{{ $t('inspectionPoint.title') }}</span>
                <div class="ibox-tools">
                  <el-button size="small" class="margin-left-xs"
                    @click="_openEditInspectionPointModel(inspectionPointInfo.point)">
                    {{ $t('common.edit') }}
                  </el-button>
                  <el-button size="small" type="danger" class="margin-left-xs"
                    @click="_openDeleteInspectionPointModel(inspectionPointInfo.point)">
                    {{ $t('common.delete') }}
                  </el-button>
                  <el-button size="small" class="margin-left-xs" @click="_pointQrCode(inspectionPointInfo.point)">
                    {{ $t('inspectionPoint.qrCode') }}
                  </el-button>
                </div>
              </div>

              <div class="detail-wrapper">
                <el-row :gutter="10">
                  <el-col :span="6">
                    <div class="detail-item">
                      <span class="detail-label">{{ $t('inspectionPoint.inspectionPoint') }}：</span>
                      <span>{{ inspectionPointInfo.point.inspectionName }}</span>
                    </div>
                  </el-col>
                  <el-col :span="6">
                    <div class="detail-item">
                      <span class="detail-label">{{ $t('inspectionPoint.inspectionType') }}：</span>
                      <span>{{ inspectionPointInfo.point.pointTypeName }}</span>
                    </div>
                  </el-col>
                  <el-col :span="6">
                    <div class="detail-item">
                      <span class="detail-label">{{ $t('inspectionPoint.inspectionLocation') }}：</span>
                      <span>{{ inspectionPointInfo.point.pointObjName }}</span>
                    </div>
                  </el-col>
                  <el-col :span="6">
                    <div class="detail-item">
                      <span class="detail-label">{{ $t('inspectionPoint.inspectionItem') }}：</span>
                      <span>{{ inspectionPointInfo.point.itemName }}</span>
                    </div>
                  </el-col>
                  <el-col :span="6">
                    <div class="detail-item">
                      <span class="detail-label">NFC：</span>
                      <span>{{ inspectionPointInfo.point.nfcCode }}</span>
                    </div>
                  </el-col>
                </el-row>
              </div>
            </el-card>
          </el-col>
        </el-row>

        <el-row v-if="inspectionPointInfo.point.inspectionId" class="margin-top">
          <el-col :span="24">
            <el-card>
              <el-tabs v-model="inspectionPointInfo._currentTab"
                @tab-click="changeTab(inspectionPointInfo._currentTab)">
                <el-tab-pane name="pointTaskDetail" :label="$t('inspectionPoint.tabs.pointTaskDetail')">
                  <point-task-detail v-if="inspectionPointInfo._currentTab === 'pointTaskDetail'"
                    ref="pointTaskDetail" />
                </el-tab-pane>
                <el-tab-pane name="pointRoute" :label="$t('inspectionPoint.tabs.pointRoute')">
                  <point-route v-if="inspectionPointInfo._currentTab === 'pointRoute'" ref="pointRoute" />
                </el-tab-pane>
                <el-tab-pane name="pointPlan" :label="$t('inspectionPoint.tabs.pointPlan')">
                  <point-plan v-if="inspectionPointInfo._currentTab === 'pointPlan'" ref="pointPlan" />
                </el-tab-pane>
                <el-tab-pane name="pointTask" :label="$t('inspectionPoint.tabs.pointTask')">
                  <point-task v-if="inspectionPointInfo._currentTab === 'pointTask'" ref="pointTask" />
                </el-tab-pane>
                <el-tab-pane name="itemQuestions" :label="$t('inspectionPoint.tabs.itemQuestions')">
                  <div v-if="inspectionPointInfo._currentTab === 'itemQuestions'" v-loading="questionsLoading" class="margin-top">
                    <el-table :data="questionsData" border style="width:100%">
                      <el-table-column prop="titleId" :label="$t('inspectionItemTitleManage.titleId')" align="center" />
                      <el-table-column prop="itemTitle" :label="$t('inspectionItemTitleManage.title')" align="center" />
                      <el-table-column prop="titleType" :label="$t('inspectionItemTitleManage.titleType')" align="center">
                        <template slot-scope="scope">
                          {{ _getTitleTypeName(scope.row.titleType) }}
                        </template>
                      </el-table-column>
                      <el-table-column prop="seq" :label="$t('inspectionItemTitleManage.seq')" align="center" />
                      <el-table-column prop="createTime" :label="$t('inspectionItemTitleManage.createTime')" align="center" />
                    </el-table>
                  </div>
                </el-tab-pane>
              </el-tabs>
            </el-card>
          </el-col>
        </el-row>
      </el-col>
    </el-row>
    <!-- 模态框组件 -->
    <add-inspection-point ref="addInspectionPoint" @success="_listInspectionPoints" />
    <edit-inspection-point ref="editInspectionPoint" @success="_listInspectionPoints" />
    <delete-inspection-point ref="deleteInspectionPoint" @success="_listInspectionPoints" />
    <inspection-point-qr-code ref="inspectionPointQrCode" @success="_listInspectionPoints" />
  </div>
</template>

<script>
import { listInspectionPoints } from '@/api/inspection/inspectionPointApi'
import { listInspectionItemTitle } from '@/api/inspection/inspectionItemTitleManageApi'
import { getDict } from '@/api/community/communityApi'
import { getCommunityId } from '@/api/community/communityApi'
import PointTaskDetail from '@/components/inspection/pointTaskDetail'
import PointRoute from '@/components/inspection/pointRoute'
import PointPlan from '@/components/inspection/pointPlan'
import PointTask from '@/components/inspection/pointTask'
import AddInspectionPoint from '@/components/inspection/addInspectionPoint'
import EditInspectionPoint from '@/components/inspection/editInspectionPoint'
import DeleteInspectionPoint from '@/components/inspection/deleteInspectionPoint'
import InspectionPointQrCode from '@/components/inspection/inspectionPointQrCode'

export default {
  name: 'InspectionPointList',
  components: {
    PointTaskDetail,
    PointRoute,
    PointPlan,
    PointTask,
    AddInspectionPoint,
    EditInspectionPoint,
    DeleteInspectionPoint,
    InspectionPointQrCode
  },
  data() {
    return {
      inspectionPointInfo: {
        points: [],
        point: {},
        total: 0,
        records: 1,
        moreCondition: false,
        inspectionName: '',
        pointObjTypes: [],
        _currentTab: 'pointTaskDetail',
        conditions: {
          inspectionId: '',
          machineId: '',
          inspectionName: '',
          machineCode: '',
          pointObjType: '',
          communityId: '',
          page: 1,
          row: 1000
        }
      },
      communityId: '',
      questionsData: [],
      questionsLoading: false
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.inspectionPointInfo.conditions.communityId = this.communityId
    this.getDictData()
    this._listInspectionPoints()
  },
  methods: {
    async getDictData() {
      try {
        const data = await getDict('inspection_point', 'point_obj_type')
        this.inspectionPointInfo.pointObjTypes = data
      } catch (error) {
        console.error('获取字典数据失败:', error)
      }
    },
    async _listInspectionPoints() {
      try {
        const response = await listInspectionPoints(this.inspectionPointInfo.conditions)
        this.inspectionPointInfo.points = response.inspectionPoints
        this.inspectionPointInfo.total = response.total
        this.inspectionPointInfo.records = response.records

        if (response.inspectionPoints && response.inspectionPoints.length > 0) {
          if (this.inspectionPointInfo.point.inspectionId) {
            var found = response.inspectionPoints.find(function (p) {
              return p.inspectionId === this.inspectionPointInfo.point.inspectionId
            }.bind(this))
            if (found) {
              this.inspectionPointInfo.point = found
              this.changeTab(this.inspectionPointInfo._currentTab)
            } else {
              this._switchInspectionPoint(response.inspectionPoints[0])
            }
          } else {
            this._switchInspectionPoint(response.inspectionPoints[0])
          }
        }
      } catch (error) {
        console.error('获取巡检点列表失败:', error)
      }
    },
    _queryInspectionPointMethod() {
      this.inspectionPointInfo.conditions.page = 1
      this._listInspectionPoints()
    },
    _switchInspectionPoint(point) {
      this.inspectionPointInfo.point = point
      this.changeTab(this.inspectionPointInfo._currentTab)
    },
    changeTab(tab) {
      this.inspectionPointInfo._currentTab = tab

      if (tab === 'itemQuestions') {
        this._loadQuestions()
        return
      }

      // 触发子组件加载数据
      setTimeout(() => {
        if (this.$refs[tab]) {
          this.$refs[tab].loadData(this.inspectionPointInfo.point)
        }
      }, 500)

    },
    async _loadQuestions() {
      var itemId = this.inspectionPointInfo.point.itemId
      if (!itemId) return

      this.questionsLoading = true
      try {
        var res = await listInspectionItemTitle({
          itemId: itemId,
          page: 1,
          row: 100,
          communityId: this.communityId
        })
        this.questionsData = res.data || []
      } catch (error) {
        console.error('获取项目题目失败:', error)
      } finally {
        this.questionsLoading = false
      }
    },
    _getTitleTypeName(type) {
      var types = {
        '1001': this.$t('inspectionItemTitleManage.singleChoice'),
        '2002': this.$t('inspectionItemTitleManage.multipleChoice'),
        '3003': this.$t('inspectionItemTitleManage.shortAnswer')
      }
      return types[type] || type
    },
    _openAddInspectionPointModal() {
      this.$refs.addInspectionPoint.open()
    },
    _openEditInspectionPointModel(point) {
      this.$refs.editInspectionPoint.open(point)
    },
    _openDeleteInspectionPointModel(point) {
      this.$refs.deleteInspectionPoint.open(point)
    },
    _pointQrCode(point) {
      console.log(point)
      this.$refs.inspectionPointQrCode.open(point)
    }
  }
}
</script>

<style lang="scss" scoped>
.inspection-point-container {
  padding: 20px;
}

.margin-top {
  margin-top: 15px;
}

.left-panel {
  .point-card {
    height: 700px;

    .point-list-wrapper {
      height: 630px;
      overflow-y: auto;

      &::-webkit-scrollbar {
        width: 4px;
      }

      &::-webkit-scrollbar-thumb {
        background: #dcdfe6;
        border-radius: 2px;
      }

      &::-webkit-scrollbar-track {
        background: transparent;
      }

      .point-list {
        list-style: none;
        padding: 0 5px;
        margin: 0;

        .point-list-item {
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
        }
      }
    }
  }
}

.right-panel {
  padding-left: 5px;
}

.ibox-tools {
  float: right;
}

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
</style>